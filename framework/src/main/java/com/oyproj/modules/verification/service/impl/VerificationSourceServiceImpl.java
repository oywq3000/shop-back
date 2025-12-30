package com.oyproj.modules.verification.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oyproj.cache.Cache;
import com.oyproj.modules.verification.entity.dos.VerificationSource;
import com.oyproj.modules.verification.entity.dto.VerificationDTO;
import com.oyproj.modules.verification.entity.enums.VerificationSourceEnum;
import com.oyproj.modules.verification.mapper.VerificationSourceMapper;
import com.oyproj.modules.verification.service.VerificationSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author oywq3000
 * @since 2025-12-30
 */
@Service
@RequiredArgsConstructor
public class VerificationSourceServiceImpl implements VerificationSourceService {

    private final VerificationSourceMapper verificationSourceMapper;

    private final Cache<VerificationDTO> cache;
    /**
     * 初始化缓存
     */
    @Override
    public VerificationDTO initCache() {
        List<VerificationSource> dbList = verificationSourceMapper.selectList(new LambdaQueryWrapper<>());
        List<VerificationSource> resourceList = new ArrayList<>();
        List<VerificationSource> sliderList = new ArrayList<>();
        for(VerificationSource item:dbList){
            if(item.getType().equals(VerificationSourceEnum.RESOURCE.name())){
                resourceList.add(item);
            }else if(item.getType().equals(VerificationSourceEnum.SLIDER.name())){
                sliderList.add(item);
            }
        }
        VerificationDTO verificationDTO = new VerificationDTO();
        verificationDTO.setVerificationResources(resourceList);
        verificationDTO.setVerificationSliders(sliderList);
        cache.put(VERIFICATION_CACHE,verificationDTO);
        return verificationDTO;
    }

    /**
     * 获取缓存
     *
     * @return 验证码
     */
    @Override
    public VerificationDTO getVerificationCache() {
        VerificationDTO verificationDTO;
        try {
            verificationDTO = cache.get(VERIFICATION_CACHE);
        }catch (ClassCastException e){
            verificationDTO = null;
        }
        if(verificationDTO==null||verificationDTO.getVerificationResources().size()<= 0||verificationDTO.getVerificationSliders().size() <= 0){
            return initCache();
        }
        return verificationDTO;
    }
}
