package com.ruoyi.pay.model;

import lombok.Data;

import java.io.Serializable;

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
