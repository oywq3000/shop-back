package com.oyproj;
import com.oyproj.common.event.TransactionCommitSendMQEvent;
import com.oyproj.common.properties.RocketmqCustomProperties;
import com.oyproj.common.utils.SnowFlake;
import com.oyproj.modules.mamber.entity.dos.Member;
import com.oyproj.modules.mamber.service.MemberService;
import com.oyproj.rocketmq.tags.MemberTagsEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * Unit test for simple App.
 */

@SpringBootTest
public class AppTest {

    @Autowired
    private MemberService memberService;

    @Test
    public void shouldAnswerWithTrue() {
        Member admin = memberService.findByUsername("admin");
        System.out.println(admin);
    }
}
