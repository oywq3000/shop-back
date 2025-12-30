package com.oyproj.modules.verification.service.impl;

import com.oyproj.cache.Cache;
import com.oyproj.cache.CachePrefix;
import com.oyproj.common.enums.ResultCode;
import com.oyproj.common.exception.ServiceException;
import com.oyproj.common.utils.StringUtils;
import com.oyproj.common.vo.SerializableStream;
import com.oyproj.modules.verification.entity.dos.VerificationSource;
import com.oyproj.modules.verification.entity.dto.VerificationDTO;
import com.oyproj.modules.verification.entity.enums.VerificationEnum;
import com.oyproj.modules.verification.service.VerificationService;
import com.oyproj.modules.verification.service.VerificationSourceService;
import com.oyproj.properties.VerificationCodeProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author oywq3000
 * @since 2025-12-30
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final VerificationSourceService verificationSourceService;

    private final VerificationCodeProperties verificationCodeProperties;

    private final Cache cache;

    /**
     * 创建校验
     * @param verificationEnum
     * @param uuid
     * @return 验证码参数
     */

    @Override
    public Map<String, Object> createVerification(VerificationEnum verificationEnum, String uuid) {
        if(uuid==null){
            throw new ServiceException(ResultCode.ILLEGAL_REQUEST_ERROR);
        }

        //获取验证码配置
        VerificationDTO verificationDTO = verificationSourceService.getVerificationCache();
        List<VerificationSource> verificationResources = verificationDTO.getVerificationResources();
        List<VerificationSource> verificationSliders = verificationDTO.getVerificationSliders();

        Random random = new Random();

        //随机选择需要切的图像下标和剪切模板的下标
        int resourceNum = random.nextInt(verificationResources.size());
        int sliderNum = random.nextInt(verificationSliders.size());
        //随机选择需要切的图片地址
        String originalResource = verificationResources.get(resourceNum).getResource();
        //随机选择剪切模版图片地址
        String sliderResource = verificationSliders.get(sliderNum).getResource();
        //干扰块
        String interfereResource = verificationSliders.get(sliderNum == verificationSliders.size() - 1 ?
                sliderNum - 1 : sliderNum + 1).getResource();

        try{
            //获取缓存中的资源
            SerializableStream originalFile = getInputStream(originalResource);
            SerializableStream sliderFile = getInputStream(sliderResource);
            SerializableStream interfereSliderFile = verificationCodeProperties.getInterfereNum() > 0 ? getInputStream(interfereResource) : null;
            //生成数据

        }



    }

    /**
     * 根据网络地址，获取源文件
     *
     * 这里是将不可序列化的inputstream序列化对象，存入redis缓存
     */

    private SerializableStream getInputStream(String originalResource) throws Exception{
        Object object = cache.get(CachePrefix.VERIFICATION_IMAGE.getPrefix()+originalResource);
        if(object!=null){
            return (SerializableStream) object;
        }
        if(StringUtils.isNotEmpty(originalResource)){
            URL url = new URL(originalResource);
            InputStream inputStream = url.openStream();
            SerializableStream serializableStream = new SerializableStream(inputStream);
            cache.put(CachePrefix.VERIFICATION_IMAGE.getPrefix()+originalResource,serializableStream);
            return serializableStream;
        }
        return null;
    }
}
