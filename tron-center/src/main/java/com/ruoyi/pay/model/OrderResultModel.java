package com.ruoyi.pay.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ：writer
 * @date ：Created in 2022/3/11 11:34
 * @description：支付业务参数基础类
 * @modified By：
 * @version: 0.0.1$
 */
@Data
public class OrderResultModel implements Serializable {

    private String amount;
    private String currency;
    private String coin_code;
    private String coin_amount;
    private String coin_address;
    private String qrcode_url;
    private String cashier_url;
    private String timeout;
}
