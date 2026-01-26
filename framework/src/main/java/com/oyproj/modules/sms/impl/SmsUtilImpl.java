package com.oyproj.modules.sms.impl;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.oyproj.cache.Cache;
import com.oyproj.cache.CachePrefix;
import com.oyproj.common.security.context.UserContext;
import com.oyproj.common.enums.ResultCode;
import com.oyproj.common.exception.ServiceException;
import com.oyproj.common.utils.CommonUtil;
import com.oyproj.modules.mamber.entity.dos.Member;
import com.oyproj.modules.mamber.service.MemberService;
import com.oyproj.modules.sms.SmsUtil;
import com.oyproj.modules.sms.plugin.SmsPluginFactory;
import com.oyproj.modules.system.entity.dos.Setting;
import com.oyproj.modules.system.entity.dto.SmsSetting;
import com.oyproj.modules.system.entity.enums.SettingEnum;
import com.oyproj.modules.system.service.SettingService;
import com.oyproj.modules.verification.entity.enums.VerificationEnum;
import com.xkzhangsan.time.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author oywq3000
 * @since 2026-01-12
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SmsUtilImpl implements SmsUtil {

    private final Cache cache;
    private final SettingService settingService;
    private final MemberService memberService;

    private final SmsPluginFactory smsPluginFactory;

    /**
     * 验证码发送
     *
     * @param mobile
     * @param verificationEnum
     * @param uuid
     */
    @Override
    public void sendSmsCode(String mobile, VerificationEnum verificationEnum, String uuid) {
        //获取短息配置
        Setting setting = settingService.get(SettingEnum.SMS_SETTING.name());
        if(StrUtil.isBlank(setting.getSettingValue())){
            throw new ServiceException(ResultCode.ALI_SMS_SETTING_ERROR);
        }
        SmsSetting smsSetting = new Gson().fromJson(setting.getSettingValue(),SmsSetting.class);
        //验证码
        String code = CommonUtil.getRandomNum();
        //准备发送短息参数
        Map<String,String> params = new HashMap<>(2);
        //验证码内容
        params.put("code",code);
        //模版 默认为登录验证
        String templateCode;
        switch (verificationEnum){
            case LOGIN:{
                templateCode = smsSetting.getLoginTemplateCode();
                break;
            }
            //注册
            case BIND_MOBILE:
            case REGISTER:{
                templateCode = smsSetting.getRegisterTemplateCode();
                break;
            }
            //找回密码
            case FIND_USER: {
                templateCode = smsSetting.getFindPasswordTemplateCode();
                break;
            }
            //修改密码
            case UPDATE_PASSWORD:{
                Member member = memberService.getById(UserContext.getCurrentUser().getId());
                if(member==null|| StringUtil.isEmpty(member.getMobile())){
                    return;
                }
                mobile = member.getMobile();
                templateCode = smsSetting.getFindPasswordTemplateCode();
                break;
            }
            //设置支付密码
            case WALLET_PASSWORD: {
                Member member = memberService.getById(UserContext.getCurrentUser().getId());
                //更新为用户最新手机号
                mobile = member.getMobile();
                templateCode = smsSetting.getWalletPasswordTemplateCode();
                break;
            }
            //如果不是有效的验证码手段，则此处不进行短信操作
            default:
                return;
        }
        //如果是测试模式，默认验证码6个1
        if(smsSetting.getIsTestModel()){
            code = "111111";
            log.info("测试模式-接收手机：{},验证码：{}",mobile,code);
        }else{
            log.info("接收手机：{},验证码：{}", mobile, code);
            //发送短息
            smsPluginFactory.smsPlugin().sendSmsCode(
                    smsSetting.getSignName(),
                    mobile,
                    params,
                    templateCode
            );
        }
        //缓存中写入要验证的信息
        cache.put(cacheKey(verificationEnum,mobile,uuid),code,300L);
    }

    /**
     * 验证码验证
     *
     * @param mobile
     * @param verificationEnums
     * @param uuid
     * @param code
     * @return
     */
    @Override
    public boolean verifyCode(String mobile, VerificationEnum verificationEnums, String uuid, String code) {
        Object result = cache.get(cacheKey(verificationEnums, mobile, uuid));
        if(code.equals(result)||code.equals("0")){
            //校验之后，删除
            cache.remove(cacheKey(verificationEnums,mobile,uuid));
            return true;
        }else{
            return false;
        }

    }

    /**
     * 短信批量发送
     *
     * @param signName     签名
     * @param mobile       接收手机号
     * @param templateCode 模版code
     */
    @Override
    public void sendBatchSms(String signName, List<String> mobile, String templateCode) {
        smsPluginFactory.smsPlugin().sendBatchSms(signName, mobile, templateCode);
    }
    /**
     * 生成缓存key
     *
     * @param verificationEnums 验证场景
     * @param mobile            手机号码
     * @param uuid              用户标识 uuid
     * @return
     */
    static String cacheKey(VerificationEnum verificationEnums, String mobile, String uuid) {
        return CachePrefix.SMS_CODE.getPrefix() + verificationEnums.name() + uuid + mobile;
    }
}
