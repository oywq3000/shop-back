package com.oyproj.modules.system.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyproj.modules.system.entity.dos.Setting;
import com.oyproj.modules.system.mapper.SettingMapper;
import com.oyproj.modules.system.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author oywq3000
 * @since 2026-01-12
 */
@Service
public class SettingServiceImpl extends ServiceImpl<SettingMapper,Setting> implements SettingService {
    /**
     * 通过key获取
     *
     * @param key
     */
    @Override
    public Setting get(String key) {
        return this.getById(key);
    }
    /**
     * 修改
     *
     * @param setting
     */
    @Override
    public boolean saveUpdate(Setting setting) {
        return saveOrUpdate(setting);
    }
}
