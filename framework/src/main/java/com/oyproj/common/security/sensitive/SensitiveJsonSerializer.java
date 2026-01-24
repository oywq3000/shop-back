package com.oyproj.common.security.sensitive;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.oyproj.common.context.UserContext;
import com.oyproj.common.properties.SystemSettingProperties;
import com.oyproj.common.security.AuthUser;
import com.oyproj.common.security.enums.UserEnums;
import com.oyproj.common.security.sensitive.enums.SensitiveStrategy;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.util.Objects;

/**
 * @author oywq3000
 * @since 2026-01-12
 */
public class SensitiveJsonSerializer
        extends JsonSerializer<String>
        implements ContextualSerializer, ApplicationContextAware {
    //脱敏策略
    private SensitiveStrategy strategy;
    //系统配置
    private SystemSettingProperties systemSettingProperties;


    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        // 字段序列化处理
        jsonGenerator.writeString(strategy.desensitizer().apply(s));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        //判定是否需要脱敏处理
        if(desensitization()){
            //获取敏感枚举
            Sensitive annotation = beanProperty.getAnnotation(Sensitive.class);
            //如果有敏感注解，则加入脱敏规则
            if(Objects.nonNull(annotation)&&Objects.equals(String.class,beanProperty.getType().getRawClass())) {
                this.strategy = annotation.strategy();
                return this;
            }
        }
        return serializerProvider.findValueSerializer(beanProperty.getType(),beanProperty);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        systemSettingProperties = applicationContext.getBean(SystemSettingProperties.class);
    }

    /**
     * 是否需要脱敏处理
     */
    private boolean desensitization(){
        //当前用户
        AuthUser currentUser = UserContext.getCurrentUser();
        //默认脱敏
        if(currentUser==null){
            return false;
        }
        //如果是店铺
        if(currentUser.getRole().equals(UserEnums.STORE)){
            //店铺需要进行脱敏，则脱敏处理
            return systemSettingProperties.getSensitiveLevel() == 2;
        }
        //如果是店铺
        if(currentUser.getRole().equals(UserEnums.MANAGER)){
            //店铺需要进行脱敏，则脱敏处理
            return systemSettingProperties.getSensitiveLevel() >= 1;
        }
        return false;
    }
}
