package com.oyproj.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client")
public class OAuth2ClientProperties {
    private Registration registration;
    private Provider provider;
    @Data
    public static class Registration{
        private Github github;

        @Data
        public static class Github{
            private String clientId;
            private String clientSecret;
            private String redirectUri;
        }

    }

    @Data
    public static class Provider{
        private Github github;
        @Data
        public static class Github{
            private String authorizationUri;
            private String tokenUri;
            private String userInfoUri;
            private String userNameAttribute;
        }
    }
}
