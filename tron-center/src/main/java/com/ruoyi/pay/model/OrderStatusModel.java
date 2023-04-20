package com.ruoyi.pay.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderStatusModel implements Serializable {

    private String mchId;
    private String orderId;
    private String status;
    private String notify_succeed;
    private String amount;
    private String coin_amount;
}
