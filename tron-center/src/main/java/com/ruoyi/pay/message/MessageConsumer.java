package com.ruoyi.pay.message;

import com.ruoyi.pay.domain.OrgAccountOrder;
import com.ruoyi.pay.domain.OrgAccountOrderDaip;
import com.ruoyi.pay.task.CallbackNotifyWorker;
import com.ruoyi.pay.task.PayTimeOutWorker;
import com.ruoyi.pay.task.PdaiWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;

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
    @Autowired
    private PdaiWorker pdaiWorker;

    @StreamListener("payTimeInput")
    public void payTimeInput(Message<OrgAccountOrder> message, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws Exception {
        log.info("【超时通知】收到订单超时通知消息:{},deliveryTag={}", message.getPayload(), deliveryTag);
        payTimeOutWorker.payTimeOut(message.getPayload());
    }

    @StreamListener("callBackInput")
    public void callBackInput(Message<OrgAccountOrder> message, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws Exception {
        log.info("【回调通知】收到支付通知消息:{},deliveryTag={}", message.getPayload(), deliveryTag);
        callbackNotifyWorker.payNotify(message.getPayload());
    }

    @StreamListener("pdaiInput")
    public void pdaiInput(Message<OrgAccountOrderDaip> message) throws Exception {
        log.info("【提现通知】收到提现通知消息:{},deliveryTag={}", message.getPayload());
        pdaiWorker.payNotify(message.getPayload());
    }

    @StreamListener("pdaiCallBackInput")
    public void pdaiCallBackInput(Message<OrgAccountOrderDaip> message, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws Exception {
        log.info("【回调通知】收到提现通知消息:{},deliveryTag={}", message.getPayload(), deliveryTag);
        callbackNotifyWorker.pdaiNotify(message.getPayload());
    }

}
