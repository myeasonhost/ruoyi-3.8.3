package com.ruoyi.pay.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author ：doctor
 * @description：消息消费者接口定义
 */
public interface UserSink {

    /*** 支付超时通知消息 ***/
    @Input("payTimeInput")
    SubscribableChannel payTimeInput();

    /*** 支付订单回调通知 ***/
    @Input("callBackInput")
    SubscribableChannel callBackInput();

    /*** 提现消息通知 ***/
    @Input("pdaiInput")
    SubscribableChannel pdaiInput();
}
