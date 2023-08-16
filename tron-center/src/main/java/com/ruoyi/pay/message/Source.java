package com.ruoyi.pay.message;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;


/**
 * @author ：doctor
 * @description：消息生产者定义
 */
public interface Source {

    /*** 支付超时通知消息 ***/
    @Output("payTimeOutput")
    MessageChannel payTimeOutput();

    /*** 支付订单回调通知 ***/
    @Output("callBackOutput")
    MessageChannel callBackOutput();

    /*** 提现消息通知 ***/
    @Output("pdaiOutput")
    MessageChannel pdaiOutput();

    /*** 代付订单通知 ***/
    @Output("pdaiCallBackOutput")
    MessageChannel pdaiCallBackOutput();
}