package com.oyproj.modules.sms.plugin.impl;

import com.oyproj.modules.sms.entity.dos.SmsSign;
import com.oyproj.modules.sms.entity.dos.SmsTemplate;
import com.oyproj.modules.sms.entity.enums.SmsEnum;
import com.oyproj.modules.sms.plugin.SmsPlugin;
import com.oyproj.modules.system.entity.dto.SmsSetting;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 阿里云短息插件
 *
 * @author oywq3000
 * @since 2026-01-24
 */
@Slf4j
public class AliSmsPlugin implements SmsPlugin {
    private SmsSetting smsSetting;
    public AliSmsPlugin(SmsSetting setting){
        this.smsSetting = setting;
    }
    /**
     * 插件名称
     */
    @Override
    public SmsEnum pluginName() {
        return null;
    }

    /**
     * 短息发送
     *
     * @param signName     签名名称
     * @param mobile       接收手机号
     * @param param        参数
     * @param templateCode 模板code
     */
    @Override
    public void sendSmsCode(String signName, String mobile, Map<String, String> param, String templateCode) {

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

    }

    /**
     * 申请短信签名
     *
     * @param smsSign 短信签名
     * @throws Exception 阿里短信签名错误
     */
    @Override
    public void addSmsSign(SmsSign smsSign) throws Exception {

    }

    /**
     * 删除短息签名
     *
     * @param signName 签名名称
     * @throws Exception 阿里短信签名错误
     */
    @Override
    public void deleteSmsSign(String signName) throws Exception {

    }

    /**
     * 查询短信签名申请状态
     *
     * @param signName 签名名称
     * @return 短信签名申请状态
     * @throws Exception 阿里短信签名错误
     */
    @Override
    public Map<String, Object> querySmsSign(String signName) throws Exception {
        return null;
    }

    /**
     * 修改未审核通过的短信签名，并重新提交审核。
     *
     * @param smsSign 短信签名
     * @throws Exception 阿里短信签名错误
     */
    @Override
    public void modifySmsSign(SmsSign smsSign) throws Exception {

    }

    /**
     * 修改未审核通过的短信模板，并重新提交审核。
     *
     * @param smsTemplate 短信模板
     * @throws Exception 阿里短信签名错误
     */
    @Override
    public void modifySmsTemplate(SmsTemplate smsTemplate) throws Exception {

    }

    /**
     * 查看短信模板
     *
     * @param templateCode 短信模板CODE
     * @return 短信模板
     * @throws Exception 阿里短信签名错误
     */
    @Override
    public Map<String, Object> querySmsTemplate(String templateCode) throws Exception {
        return null;
    }

    /**
     * 申请短信模板
     *
     * @param smsTemplate 短信模板
     * @return 短信模板
     * @throws Exception 阿里短信签名错误
     */
    @Override
    public String addSmsTemplate(SmsTemplate smsTemplate) throws Exception {
        return null;
    }

    /**
     * 删除短信模板
     *
     * @param templateCode 短信模板CODE
     * @throws Exception 阿里短信签名错误
     */
    @Override
    public void deleteSmsTemplate(String templateCode) throws Exception {

    }
}
