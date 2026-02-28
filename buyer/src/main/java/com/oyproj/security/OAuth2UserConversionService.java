package com.oyproj.security;

import com.oyproj.common.security.token.Token;
import com.oyproj.modules.mamber.entity.dos.Member;
import com.oyproj.modules.mamber.service.MemberService;
import com.oyproj.modules.mamber.token.MemberTokenGenerate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserConversionService {
    private final MemberService memberService;
    private final MemberTokenGenerate memberTokenGenerate;

    /**
     * 将GitHub OAuth2用户转换系统用户并生成Token
     */
    public Token convertGitHubUserToToken(OAuth2User oAuth2User){
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String githubId = String.valueOf(attributes.get("id"));
        String login = (String) attributes.get("login");
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        log.info("Converting GitHub user to system user: {}", login);
        //查询或者创建系统用户
        Member member = memberService.findByThirdPartyId(githubId);
        if(member==null){

        }

        return null;
    }
    /**
     * 根据Github用户信息查找或创建系统用户
     */
    private Member findOrCreateMemberFromGitHub(String githubId, String login, String email, String name){
        return null;
    }
}
