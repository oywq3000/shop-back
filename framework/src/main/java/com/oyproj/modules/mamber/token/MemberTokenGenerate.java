package com.oyproj.modules.mamber.token;

import com.oyproj.common.context.ThreadContextHolder;
import com.oyproj.common.enums.ClientTypeEnum;
import com.oyproj.common.properties.RocketmqCustomProperties;
import com.oyproj.common.security.AuthUser;
import com.oyproj.common.security.enums.UserEnums;
import com.oyproj.common.security.token.Token;
import com.oyproj.common.security.token.TokenUtil;
import com.oyproj.common.security.token.base.AbstractTokenGenerate;
import com.oyproj.modules.mamber.entity.dos.Member;
import com.oyproj.rocketmq.RocketmqSendCallbackBuilder;
import com.oyproj.rocketmq.tags.MemberTagsEnum;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 会员token生成
 *
 * @author oywq3000
 * @since 2026-01-25
 */
@Component
@RequiredArgsConstructor
public class MemberTokenGenerate extends AbstractTokenGenerate<Member> {


    private final TokenUtil tokenUtil;
    private final RocketmqCustomProperties rocketmqCustomProperties;
    private final RocketMQTemplate rocketMQTemplate;



    /**
     * 生成token
     *
     * @param user
     * @param longTerm
     */
    @Override
    public Token createToken(Member user, Boolean longTerm) {
        ClientTypeEnum clientTypeEnum;
        try{
            //获取客户类型
            String clientType = ThreadContextHolder.getHttpRequest().getHeader("clientType");
            //如果客户端为空，则缺省值为PC，pc第三方登录时不会传递此参数
            if(clientType == null) {
                clientTypeEnum = ClientTypeEnum.PC;
            } else {
                clientTypeEnum = ClientTypeEnum.valueOf(clientType);
            }
        }catch (Exception e){
            clientTypeEnum = ClientTypeEnum.UNKNOWN;
        }
        //记录最后登录时间，客户端类型
        user.setLastLoginDate(new Date());
        user.setClientEnum(clientTypeEnum.name());
        String destination = rocketmqCustomProperties.getMemberTopic()+ ":" + MemberTagsEnum.MEMBER_LOGIN.name();
        rocketMQTemplate.asyncSend(destination,user, RocketmqSendCallbackBuilder.commonCallback());

        AuthUser authUser = AuthUser.builder()
                .username(user.getUsername())
                .face(user.getFace())
                .id(user.getId())
                .role(UserEnums.MEMBER)
                .nickName(user.getNickName())
                .longTerm(longTerm)
                .build();
        //登陆成功生成token
        return tokenUtil.createToken(authUser);
    }

    /**
     * 刷新token
     *
     * @param refreshToken
     */
    @Override
    public Token refreshToken(String refreshToken) {
        return tokenUtil.refreshToken(refreshToken);
    }
}
