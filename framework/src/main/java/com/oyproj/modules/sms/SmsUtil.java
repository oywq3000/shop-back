package com.oyproj.modules.sms;

import com.oyproj.modules.verification.entity.enums.VerificationEnum;

import java.util.List;

public interface SmsUtil {
    /**
     * 验证码发送
     */
    void sendSmsCode(String mobile, VerificationEnum verificationEnum,String uuid);

    /**
     * 验证码验证
     *
     * @param mobile
     * @param verificationEnums
     * @param uuid
     * @param code
     * @return
     */
    boolean verifyCode(String mobile, VerificationEnum verificationEnums, String uuid, String code);

    /**
     * 短信批量发送
     *
     * @param mobile       接收手机号
     * @param signName     签名
     * @param templateCode 模版code
     */
    void sendBatchSms(String signName, List<String> mobile, String templateCode);
}
