package com.oyproj.controller.passport.connect;

import com.oyproj.common.enums.ResultCode;
import com.oyproj.common.exception.ServiceException;
import com.oyproj.modules.connect.entity.Connect;
import com.oyproj.modules.connect.service.ConnectService;
import com.oyproj.properties.OAuth2ClientProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;
import java.util.Map;


@Slf4j
@Controller
@RequestMapping("/buyer/passport/connect/github")
public class GithubBuyerController {
    private final RestTemplate restTemplate;
    private final ConnectService connectService;

    private final OAuth2ClientProperties.Provider.Github githubProviderConfig;
    private final OAuth2ClientProperties.Registration.Github githubRegistrationConfig;


    public GithubBuyerController(RestTemplate restTemplate,
                                 ConnectService connectService,
                                 OAuth2ClientProperties oAuth2ClientProperties){
        this.restTemplate = restTemplate;
        this.connectService = connectService;
        //init github login param
        this.githubProviderConfig = oAuth2ClientProperties.getProvider().getGithub();
        this.githubRegistrationConfig = oAuth2ClientProperties.getRegistration().getGithub();
    }

    @GetMapping("/authorize")
    public RedirectView authorize(){
        String param = String.format(
                "?response_type=code&client_id=%s&redirect_uri=%s&scope=read:user",
                githubRegistrationConfig.getClientId(),
                githubRegistrationConfig.getRedirectUri()
        );
        String authorizeUrl = githubProviderConfig.getAuthorizationUri()+param;
        return new RedirectView(authorizeUrl);
    }

    @GetMapping("/callback")
    public String callback(@RequestParam String code, @RequestParam(required = false) String error){
        try{
            //使用配置类获取参数
            if(error!=null){
                log.error("GitHub OAuth2授权失败: {}", error);
                return "redirect:/login?error=github_auth_failed";
            }
            //获取access_token
            String accessToken = getAccessToken(code);
            if(accessToken==null){
                return "redirect:/login?error=token_failed";
            }
            //获取用户信息
            Map<String,Object> userInfo = getUserInfo(accessToken);
            if(userInfo==null){
                return "redirect:/login?error=user_info_failed";
            }
            System.out.println(userInfo);
            return null;
        }catch (Exception e){
            log.error("GitHub OAuth2回调处理异常:",e);
            return "redirect:/login?error=system_error";
        }
    }
    private String getAccessToken(String code){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Accept","application/json");
            MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
            body.add("client_id",githubRegistrationConfig.getClientId());
            body.add("client_secret",githubRegistrationConfig.getClientSecret());
            body.add("code",code);
            HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(body,headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    githubProviderConfig.getTokenUri(),request,Map.class
            );
            if(response.getStatusCode()== HttpStatus.OK){
                Map<String,Object> responseBody = response.getBody();
                return (String)responseBody.get("access_token");
            }
        }catch (Exception e){
            throw new ServiceException("获取GitHub access_token失败");
        }
        return null;
    }

    private Map<String,Object> getUserInfo(String accessToken){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "token " + accessToken);
            headers.set("Accept", "application/json");

            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    githubProviderConfig.getUserInfoUri(),HttpMethod.GET,request,Map.class
            );
            if(response.getStatusCode()==HttpStatus.OK){
                return response.getBody();
            }
        }catch (Exception e){
            log.error("获取GitHub用户信息失败", e);
            throw new ServiceException("获取GitHub用户信息失败");
        }
        return null;
    }

    private String handleUserLogin(Map<String,Object> userInfo,String accessToken){
        String githubId = String.valueOf(userInfo.get("id"));
        String username = (String) userInfo.get("login");
        String email = (String) userInfo.get("email");
        String avatar = (String) userInfo.get("avatar_url");
        log.info("GitHub用户登录: id={}, username={}, email={}", githubId, username, email);
        return null;
    }
}
