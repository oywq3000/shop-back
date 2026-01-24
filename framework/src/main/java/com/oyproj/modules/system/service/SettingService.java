package com.oyproj.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyproj.modules.system.entity.dos.Setting;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * 配置业务层
 *
 * @author oywq3000
 * @since 2026-01-12
 */
@CacheConfig(cacheNames = "{setting}")
public interface SettingService extends IService<Setting> {


    /**
     * 通过key获取
     */
   //@Cacheable(key = "#key") todo recovery
    Setting get(String key);

    /**
     * 修改
     */
    @CacheEvict(key = "#setting.id")
    boolean saveUpdate(Setting setting);
}
