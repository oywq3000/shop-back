package com.oyproj;
import com.oyproj.common.event.TransactionCommitSendMQEvent;
import com.oyproj.common.properties.RocketmqCustomProperties;
import com.oyproj.common.utils.SnowFlake;
import com.oyproj.modules.mamber.entity.dos.Member;
import com.oyproj.rocketmq.tags.MemberTagsEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Unit test for simple App.
 */

@SpringBootTest
public class AppTest {

    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private RocketmqCustomProperties rocketmqCustomProperties;
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        Member member = new Member();
        eventPublisher.publishEvent(new TransactionCommitSendMQEvent("new member register",
                rocketmqCustomProperties.getMemberTopic(),
                MemberTagsEnum.MEMBER_REGISTER.name(),member));
    }
}
