package com.ruoyi.pay.message;

import com.ruoyi.pay.domain.OrgAccountOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;


/**
 * @author ：doctor
 * @description：消息生产者
 */
@Slf4j
@EnableBinding(Source.class)
public class MessageProducer {

    @Autowired
    private Source source;

    public boolean payTimeOutput(OrgAccountOrder mbPayOrder, Integer seconds) {
        log.info("【超时通知】OUTPUT订单发送消息：延迟seconds={}，message={}", seconds, mbPayOrder);
        boolean result = source.payTimeOutput().send(MessageBuilder.withPayload(mbPayOrder)
                .setHeader("x-delay", seconds * 1000).build());
        if (result) {
            return true;
        } else {
            log.info("【超时通知】OUTPUT订单发送消息失败：延迟seconds={}，message={}", seconds, mbPayOrder);
            return false;
        }
    }

    public boolean callBackOutput(OrgAccountOrder messages, Integer seconds) {
        log.info("【回调通知】callBackOutput发消息成功");
        if (source.callBackOutput().send(MessageBuilder.withPayload(messages)
                .setHeader("x-delay", seconds * 1000).build())) {
            return true;
        } else {
            log.warn("【回调通知】发送支付记录消息失败。消息信息：{}", messages);
            return false;
        }
    }

}
