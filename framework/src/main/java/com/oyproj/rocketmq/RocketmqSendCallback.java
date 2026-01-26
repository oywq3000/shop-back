package com.oyproj.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;

/**
 * @author oywq3000
 * @since 2026-01-25
 */
@Slf4j
public class RocketmqSendCallback implements SendCallback {
    @Override
    public void onSuccess(SendResult sendResult) {
        log.info("async onSuccess SendResult={}", sendResult);
    }

    @Override
    public void onException(Throwable throwable) {
        log.error("async onException Throwable", throwable);
    }
}
