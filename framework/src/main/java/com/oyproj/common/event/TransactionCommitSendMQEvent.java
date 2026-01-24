package com.oyproj.common.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 事务提交后发生mq事件
 *
 * @author oywq3000
 * @since 2026-01-24
 */
public class TransactionCommitSendMQEvent extends ApplicationEvent {
    private static final long serialVersionUID = 5885956821347953071L;
    @Getter
    private final String topic;
    @Getter
    private final String tag;
    @Getter
    private final Object message;
    public TransactionCommitSendMQEvent(Object source, String topic, String tag, Object message) {
        super(source);
        this.topic = topic;
        this.tag = tag;
        this.message = message;
    }

}
