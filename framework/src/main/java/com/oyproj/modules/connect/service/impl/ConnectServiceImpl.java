package com.oyproj.modules.connect.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyproj.cache.Cache;
import com.oyproj.common.properties.RocketmqCustomProperties;
import com.oyproj.common.security.token.Token;
import com.oyproj.modules.connect.entity.Connect;
import com.oyproj.modules.connect.entity.dto.ConnectAuthUser;
import com.oyproj.modules.connect.entity.dto.MemberConnectLoginMessage;
import com.oyproj.modules.connect.entity.dto.WechatMPLoginParams;
import com.oyproj.modules.connect.entity.enums.SourceEnum;
import com.oyproj.modules.connect.mapper.ConnectMapper;
import com.oyproj.modules.connect.service.ConnectService;
import com.oyproj.modules.mamber.entity.dos.Member;
import com.oyproj.modules.mamber.entity.dto.ConnectQueryDTO;
import com.oyproj.modules.mamber.service.MemberService;
import com.oyproj.modules.mamber.token.MemberTokenGenerate;
import com.oyproj.modules.system.service.SettingService;
import com.oyproj.rocketmq.RocketmqSendCallbackBuilder;
import com.oyproj.rocketmq.tags.MemberTagsEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectServiceImpl extends ServiceImpl<ConnectMapper, Connect> implements ConnectService {
    static final boolean AUTO_REGION=true;
    private final SettingService settingService;
    @Lazy
    @Autowired
    private  MemberService memberService;
    private final MemberTokenGenerate memberTokenGenerate;
    private final Cache cache;

    /**
     * RocketMQ
     */
    private final RocketMQTemplate rocketMQTemplate;
    /**
     * RocketMQ配置
     */
    private final RocketmqCustomProperties rocketmqCustomProperties;

    /**
     *
     * @param authUser
     * @param uuid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Token unionLoginCallback(ConnectAuthUser authUser, String uuid) {
        return this.unionLoginCallback(authUser,false);
    }

    @Override
    public void bind(String unionId, String type) {

    }

    @Override
    public void unbind(String type) {

    }

    @Override
    public List<String> bindList() {
        return List.of();
    }

    @Override
    public Token miniProgramAutoLogin(WechatMPLoginParams params) {
        return null;
    }

    @Override
    public Connect queryConnect(ConnectQueryDTO connectQueryDTO) {
        return null;
    }

    @Override
    public void deleteByMemberId(String userId) {

    }

    @Override
    public void loginBindUser(String userId, String unionId, String type) {

    }

    /**
     * 第三方联合登入
     * 1.判断是否使用开放平台
     * 1.1如果使用开放平台则使用UnionId进行登入
     * 1.2如果不适用开放平台则使用OpenId进行登入
     * <p>
     * 2.用户登录后判断绑定OpendId
     */
    private Token unionLoginCallback(ConnectAuthUser authUser,boolean longTerm){
        try{
            Member member = null;
            //判断是否传递手机号，如果传递手机号登录
            if(StrUtil.isNotBlank(authUser.getPhone())){
                member =  memberService.findByMobile(authUser.getPhone());
            }
            //如果未查到手机号的会员则使用第三方登录
            if(member==null){
                LambdaQueryWrapper<Connect> queryWrapper = new LambdaQueryWrapper<>();
                //使用UnionId登录
                if(authUser.getToken()!=null&&StrUtil.isNotBlank(authUser.getToken().getUnionId())){
                    queryWrapper.eq(Connect::getUnionId,authUser.getToken().getUnionId())
                            .eq(Connect::getUnionType,authUser.getType());
                }else{
                    //使用OpenId登录
                    SourceEnum sourceEnum = SourceEnum.getSourceEnum(authUser.getSource(), authUser.getType());
                    queryWrapper.eq(Connect::getUnionId, authUser.getUuid())
                            .eq(Connect::getUnionType, sourceEnum.name());
                }

                //查序绑定关系
                Connect connect = this.getOne(queryWrapper);
                if(connect==null){
                    member = memberService.autoRegister(authUser);
                }else {
                    //查序会员
                    member = memberService.getById(connect.getUnionId());
                    //如果未绑定会员，则把刚才查序到的联合登录表数据删除
                    if(member==null){
                        this.remove(queryWrapper);
                        member = memberService.autoRegister(authUser);
                    }
                }
            }

            //发送用户第三方登录消息
            MemberConnectLoginMessage memberConnectLoginMessage = new MemberConnectLoginMessage();
            memberConnectLoginMessage.setMember(member);
            memberConnectLoginMessage.setConnectAuthUser(authUser);
            String destination =
                    rocketmqCustomProperties.getMemberTopic()+":"+ MemberTagsEnum.MEMBER_CONNECT_LOGIN.name();
            //发送用户第三方登录信息
            rocketMQTemplate.asyncSend(destination, JSONUtil.toJsonStr(memberConnectLoginMessage),
                    RocketmqSendCallbackBuilder.commonCallback());
            return memberTokenGenerate.createToken(member,longTerm);
        }catch (Exception e){
            log.error("联合登陆失败：", e);
            throw e;
        }
    }
}
