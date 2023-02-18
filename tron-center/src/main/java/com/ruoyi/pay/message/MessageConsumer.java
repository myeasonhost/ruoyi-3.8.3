package com.ruoyi.pay.message;

import com.ruoyi.pay.domain.OrgAccountOrder;
import com.ruoyi.pay.task.CallbackNotifyWorker;
import com.ruoyi.pay.task.PayTimeOutWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.MessageBuilder;

/**
 * @author ：doctor
 * @description：订单消息消费者
 */
@Slf4j
@EnableBinding(UserSink.class)
public class MessageConsumer {

    @Autowired
    private PayTimeOutWorker payTimeOutWorker;
    @Autowired
    private CallbackNotifyWorker callbackNotifyWorker;

    @StreamListener("payTimeInput")
    public void payTimeInput(Message<OrgAccountOrder> message, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws Exception {
        log.info("【超时通知】收到订单超时通知消息:{},deliveryTag={}", message.getPayload(), deliveryTag);
        payTimeOutWorker.payTimeOut(message.getPayload());
    }

    @StreamListener("callBackInput")
    public void callBackInput(Message<OrgAccountOrder> message, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) {
        log.info("【回调通知】收到支付通知消息:{},deliveryTag={}", message.getPayload(),deliveryTag);
        callbackNotifyWorker.payNotify(message.getPayload());
    }

}
