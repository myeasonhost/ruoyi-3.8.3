package com.ruoyi.pay.model;

import lombok.Data;

import java.io.Serializable;


@Data
public class PdaiResultModel implements Serializable {

    private String amount;
    private String currency;
    private String coin_code;
    private String coin_amount;
    private String coin_address;
    private String out_address; //转出钱包
}
