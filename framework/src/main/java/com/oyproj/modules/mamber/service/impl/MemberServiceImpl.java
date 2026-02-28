package com.oyproj.modules.mamber.service.impl;


import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyproj.cache.Cache;
import com.oyproj.common.context.ThreadContextHolder;
import com.oyproj.common.security.AuthUser;
import com.oyproj.common.security.context.UserContext;
import com.oyproj.common.enums.ResultCode;
import com.oyproj.common.event.TransactionCommitSendMQEvent;
import com.oyproj.common.exception.ServiceException;
import com.oyproj.common.properties.RocketmqCustomProperties;
import com.oyproj.common.security.enums.UserEnums;
import com.oyproj.common.security.token.Token;
import com.oyproj.common.utils.CookieUtil;
import com.oyproj.common.utils.SnowFlake;
import com.oyproj.common.vo.PageVO;
import com.oyproj.modules.connect.entity.Connect;
import com.oyproj.modules.connect.entity.dto.ConnectAuthUser;
import com.oyproj.modules.mamber.entity.dos.Member;
import com.oyproj.modules.mamber.entity.dto.*;
import com.oyproj.modules.mamber.entity.vo.MemberSearchVO;
import com.oyproj.modules.mamber.entity.vo.MemberVO;
import com.oyproj.modules.mamber.entity.vo.QRCodeLoginSessionVO;
import com.oyproj.modules.mamber.entity.vo.QRLoginResultVO;
import com.oyproj.modules.mamber.mapper.MemberMapper;
import com.oyproj.modules.mamber.service.MemberService;
import com.oyproj.modules.mamber.token.MemberTokenGenerate;
import com.oyproj.modules.connect.service.ConnectService;
import com.oyproj.rocketmq.tags.MemberTagsEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author oywq3000
 * @since 2026-01-24
 */
@Service
@RequiredArgsConstructor
public class MemberServiceImpl extends ServiceImpl<MemberMapper,Member> implements MemberService {
    private final Cache cache;
    private final ApplicationEventPublisher eventPublisher;

    private final RocketmqCustomProperties rocketmqCustomProperties;
    private final MemberTokenGenerate memberTokenGenerate;
    private final ConnectService connectService;
    /**
     * 获得当前登入用户信息
     */
    @Override
    public Member getUserInfo() {
        AuthUser tokenUser = UserContext.getCurrentUser();
        if(tokenUser!=null){
            Member member = this.findByUsername(tokenUser.getUsername());
            if(member!=null&&!member.getDisabled()){
                throw new ServiceException(ResultCode.USER_STATUS_ERROR);
            }
            return member;
        }
        throw new ServiceException(ResultCode.USER_NOT_LOGIN);
    }

    /**
     * 通过手机获取用户
     *
     * @param mobile
     */
    @Override
    public Member findByMobile(String mobile) {
        return null;
    }

    @Override
    public boolean findByMobile(String uuid, String mobile) {
        return false;
    }

    /**
     * 通过用户名获取用户
     *
     * @param username
     */
    @Override
    public Member findByUsername(String username) {
        LambdaQueryWrapper<Member> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Member::getUsername,username);
        return getOne(lambdaQueryWrapper,false);
    }

    /**
     * 登入：用户名，密码登录
     *
     * @param username
     * @param password
     */
    @Override
    public Token usernameLogin(String username, String password) {
        Member member = this.findMember(username);
        //用户是否存在
        if(member==null||!member.getDisabled()){
            throw new ServiceException(ResultCode.USER_NOT_BINDING);
        }
        //密码是否正确
        if(!new BCryptPasswordEncoder().matches(password,member.getPassword())){
            throw  new ServiceException(ResultCode.USER_PASSWORD_ERROR);
        }
        loginBindUser(member);
        return memberTokenGenerate.createToken(member,false);
    }
    /**
     * 成功登入，则检测cookie中的信息，进行会员绑定
     */
    private void loginBindUser(Member member){
        //获取cookie存储的信息
        String uuid = CookieUtil.getCookie(ConnectService.CONNECT_COOKIE, ThreadContextHolder.getHttpRequest());
        String connectType = CookieUtil.getCookie(ConnectService.CONNECT_TYPE, ThreadContextHolder.getHttpRequest());
        if(CharSequenceUtil.isNotEmpty(uuid)&&CharSequenceUtil.isNotEmpty(connectType)){
            try{
                //获取信息
                ConnectAuthUser connectAuthUser = getConnectAuthUser(uuid,connectType);
                if(connectAuthUser==null){
                    return;
                }
                Connect connect = connectService.queryConnect(
                        ConnectQueryDTO.builder().unionId(connectAuthUser.getUuid()).unionType(connectType).build()
                );
                if(connect==null){
                    connect = new Connect(member.getId(),connectAuthUser.getUuid(),connectType);
                    connectService.save(connect);
                }
            }catch (ServiceException e){
                throw e;
            }catch (Exception e) {
                log.error("绑定第三方联合登陆失败：", e);
            }finally {
                //联合登陆成功与否，都清除掉cookie中的信息
                CookieUtil.delCookie(ConnectService.CONNECT_COOKIE, ThreadContextHolder.getHttpResponse());
                CookieUtil.delCookie(ConnectService.CONNECT_TYPE, ThreadContextHolder.getHttpResponse());
            }
        }
    }
    /**
     * 获取cookie中的联合登录对象
     */
    private ConnectAuthUser getConnectAuthUser(String uuid,String type){
        Object context = cache.get(ConnectService.cacheKey(type,uuid));
        if(context!=null){
            return (ConnectAuthUser) context;
        }
        return null;
    }
    

    /**
     * 商家登录：用户名、密码登录
     *
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    @Override
    public Token usernameStoreLogin(String username, String password) {
        return null;
    }

    /**
     * 电话号码登入
     *
     * @param mobilePhone 用户名
     * @return token
     */
    @Override
    public Token mobilePhoneStoreLogin(String mobilePhone) {
        return null;
    }

    /**
     * 注册：手机号、验证码登录
     *
     * @param mobilePhone 手机号
     * @return token
     */
    @Override
    public Token mobilePhoneLogin(String mobilePhone) {
        return null;
    }

    /**
     * 修改会员信息
     *
     * @param memberEditDTO
     */
    @Override
    public Member editOwn(MemberEditDTO memberEditDTO) {
        return null;
    }

    /**
     * 修改用户密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 操作结果
     */
    @Override
    public Member modifyPass(String oldPassword, String newPassword) {
        return null;
    }

    /**
     * 注册会员
     *
     * @param userName    会员
     * @param password    密码
     * @param mobilePhone mobilePhone
     * @return 处理结果
     */
    @Override
    @Transactional
    public Token register(String userName, String password, String mobilePhone) {
        //测试会员信息
        checkMember(userName,mobilePhone);
        //设置会员信息
        //设置会员信息
        Member member = new Member(userName, new BCryptPasswordEncoder().encode(password), mobilePhone);
        registerHandler(member);
        return memberTokenGenerate.createToken(member, false);
    }

    @Transactional
    public void registerHandler(Member member){
        member.setId(SnowFlake.getIdStr());
        //保存会员
        this.save(member);
        UserContext.settingInviter(member.getId(),cache);
        // 发送会员注册信息
        eventPublisher.publishEvent(new TransactionCommitSendMQEvent("new member register",
                rocketmqCustomProperties.getMemberTopic(),
                MemberTagsEnum.MEMBER_REGISTER.name(),member));
    }

    /**
     * 是否可以初始化密码
     *
     * @return
     */
    @Override
    public boolean canInitPass() {
        return false;
    }

    /**
     * 初始化密码
     *
     * @param password 密码
     * @return 操作结果
     */
    @Override
    public void initPass(String password) {

    }

    /**
     * 注销账号
     *
     * @return 操作结果
     */
    @Override
    public void cancellation() {

    }

    /**
     * 修改当前会员的手机号
     *
     * @param mobile 手机号
     * @return 操作结果
     */
    @Override
    public boolean changeMobile(String mobile) {
        return false;
    }

    /**
     * 修改用户手机号
     *
     * @param memberId 会员ID
     * @param mobile   手机号
     * @return
     */
    @Override
    public boolean changeMobile(String memberId, String mobile) {
        return false;
    }

    /**
     * 通过手机号修改密码
     *
     * @param mobile   手机号
     * @param password 密码
     * @return
     */
    @Override
    public boolean resetByMobile(String mobile, String password) {
        return false;
    }

    /**
     * 后台-添加会员
     *
     * @param memberAddDTO 会员
     * @return 会员
     */
    @Override
    public Member addMember(MemberAddDTO memberAddDTO) {
        return null;
    }

    /**
     * 后台-修改会员
     *
     * @param managerMemberEditDTO 后台修改会员参数
     * @return 会员
     */
    @Override
    public Member updateMember(ManagerMemberEditDTO managerMemberEditDTO) {
        return null;
    }

    /**
     * 获取会员分页
     *
     * @param memberSearchVO 会员搜索VO
     * @param page           分页
     * @return 会员分页
     */
    @Override
    public IPage<MemberVO> getMemberPage(MemberSearchVO memberSearchVO, PageVO page) {
        return null;
    }

    /**
     * 一键注册会员
     *
     * @param authUser 联合登录用户
     * @return Token
     */
    @Override
    public Member autoRegister(ConnectAuthUser authUser) {
        return null;
    }

    /**
     * 刷新token
     *
     * @param refreshToken
     * @return Token
     */
    @Override
    public Token refreshToken(String refreshToken) {
        return memberTokenGenerate.refreshToken(refreshToken);
    }

    /**
     * 刷新token
     *
     * @param refreshToken
     * @return Token
     */
    @Override
    public Token refreshStoreToken(String refreshToken) {
        return null;
    }

    /**
     * 会员积分变动
     *
     * @param point    变动积分
     * @param type     是否增加积分 INCREASE 增加  REDUCE 扣减
     * @param memberId 会员id
     * @param content  变动日志
     * @return 操作结果
     */
    @Override
    public Boolean updateMemberPoint(Long point, String type, String memberId, String content) {
        return null;
    }

    /**
     * 修改会员状态
     *
     * @param memberIds 会员id集合
     * @param status    状态
     * @return 修改结果
     */
    @Override
    public Boolean updateMemberStatus(List<String> memberIds, Boolean status) {
        return null;
    }

    /**
     * 根据条件查询会员总数
     *
     * @param memberSearchVO
     * @return 会员总数
     */
    @Override
    public long getMemberNum(MemberSearchVO memberSearchVO) {
        return 0;
    }

    /**
     * 获取指定会员数据
     *
     * @param columns   指定获取的列
     * @param memberIds 会员ids
     * @return 指定会员数据
     */
    @Override
    public List<Map<String, Object>> listFieldsByMemberIds(String columns, List<String> memberIds) {
        return null;
    }

    /**
     * 登出
     *
     * @param userEnums token角色类型
     */
    @Override
    public void logout(UserEnums userEnums) {

    }

    /**
     * 登出
     *
     * @param userId 用户id
     */
    @Override
    public void logout(String userId) {

    }

    /**
     * 修改会员是否拥有店铺
     *
     * @param haveStore 是否拥有店铺
     * @param storeId   店铺id
     * @param memberIds 会员id
     * @return
     */
    @Override
    public void updateHaveShop(Boolean haveStore, String storeId, List<String> memberIds) {

    }

    /**
     * 重置会员密码为123456
     *
     * @param ids 会员id
     */
    @Override
    public void resetPassword(List<String> ids) {

    }

    /**
     * 获取所有会员的手机号
     *
     * @return 所有会员的手机号
     */
    @Override
    public List<String> getAllMemberMobile() {
        return null;
    }

    /**
     * 更新会员登录时间为最新时间
     *
     * @param memberId 会员id
     * @return 是否更新成功
     */
    @Override
    public boolean updateMemberLoginTime(String memberId) {
        return false;
    }

    /**
     * 获取用户VO
     *
     * @param id 会员id
     * @return 用户VO
     */
    @Override
    public MemberVO getMember(String id) {
        return null;
    }

    @Override
    public QRCodeLoginSessionVO createPcSession() {
        return null;
    }

    @Override
    public Object appScanner(String token) {
        return null;
    }

    @Override
    public boolean appSConfirm(String token, Integer code) {
        return false;
    }

    @Override
    public QRLoginResultVO loginWithSession(String token) {
        return null;
    }

    @Override
    public Member findByThirdPartyId(String thirdPartId) {
        return findByMemberId(thirdPartId);
    }

    private Member findByMemberId(String memberId){
        LambdaQueryWrapper<Member> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Member::getId,memberId);
        return this.getOne(lambdaQueryWrapper,false);
    }


    private Long findMember(String mobilePhone,String userName){
        LambdaQueryWrapper<Member> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Member::getMobile,mobilePhone)
                .or()
                .eq(Member::getUsername,userName);
        return this.baseMapper.selectCount(lambdaQueryWrapper);
    }

    /**
     * 根据用户名查序用户
     * @param username
     * @return
     */
    private Member findMember(String username){
        LambdaQueryWrapper<Member> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Member::getUsername,username);
        return this.getOne(lambdaQueryWrapper,false);
    }



    private void checkMember(String userName,String mobilePhone){
        //判断手机号是否存在
        if(findMember(mobilePhone,userName)>0){
            throw new ServiceException(ResultCode.USER_EXIST);
        }
    }
}
