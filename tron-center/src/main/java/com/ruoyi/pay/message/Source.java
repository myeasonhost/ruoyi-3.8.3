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

}
