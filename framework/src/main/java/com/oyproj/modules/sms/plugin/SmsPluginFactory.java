package com.oyproj.modules.sms.plugin;

import cn.hutool.json.JSONUtil;
import com.oyproj.common.exception.ServiceException;
import com.oyproj.modules.sms.entity.enums.SmsEnum;
import com.oyproj.modules.sms.plugin.impl.AliSmsPlugin;
import com.oyproj.modules.system.entity.dos.Setting;
import com.oyproj.modules.system.entity.dto.SmsSetting;
import com.oyproj.modules.system.entity.enums.SettingEnum;
import com.oyproj.modules.system.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 短息服务抽象工厂，直接返回操作类
 *
 * @author oywq3000
 * @since 2026-01-24
 */
@Component
public class SmsPluginFactory {
    @Autowired
    private SettingService settingService;

    /**
     * 获取oss client
     */
    public SmsPlugin smsPlugin(){
        SmsSetting smsSetting = null;
        try{
            Setting setting = settingService.get(SettingEnum.SMS_SETTING.name());
            smsSetting = JSONUtil.toBean(setting.getSettingValue(), SmsSetting.class);
            switch (SmsEnum.valueOf(smsSetting.getType())) {
                case ALI:
                    return new AliSmsPlugin(smsSetting);
                case TENCENT:
                    return null;
                case HUAWEI:
                    return null;
                default:
                    throw new ServiceException();
            }}
        catch (Exception e){
            throw new ServiceException();
        }
    }

}
