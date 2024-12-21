package com.ruoyi.pay.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "PayEntity", description = "支付订单实体")
public class PayEntity {

    @ApiModelProperty(value = "商户id", required = true, example = "test001", position = 1)
    @NotNull(message = "mch_id不能为空")
    private String mch_id;

    @ApiModelProperty(value = "商户订单号", required = true, example = "C15S6S3S1221", position = 2)
    @NotNull(message = "order_id不能为空")
    private String order_id;

    @ApiModelProperty(value = "用户ID", required = true, example = "23", position = 3)
    @NotNull(message = "customer_id不能为空")
    private String customer_id;

    @ApiModelProperty(value = "产品名", required = true, example = "花费充值", position = 4)
    @NotNull(message = "product_name不能为空")
    private String product_name;

    @ApiModelProperty(value = "金额", required = true, example = "100", position = 5)
    @NotNull(message = "amount不能为空")
    @DecimalMin(value = "0", message = "amount必须大于0")
    private String amount;

    @ApiModelProperty(value = "订单币种单位（CNY/USD）", position = 6)
    private String currency = "USD";

    @ApiModelProperty(value = "支付币种（USDT/RMB）", position = 7)
    private String coin_code = "USDT";

    @ApiModelProperty(value = "回调通知地址", required = true, example = "https://notify_url", position = 8)
    @NotNull(message = "notify_url回调地址不能为空")
    @URL(message = "notify_url回调地址不合法")
    private String notify_url;

    @ApiModelProperty(value = "同步跳转地址", position = 9)
    @URL
    private String redirect_url;

    @ApiModelProperty(value = "语言", required = false, example = "zh-CN", position = 10)
    private String locale = "zh-CN";

    @ApiModelProperty(value = "签名", required = true, position = 11)
    @NotNull(message = "签名不能为空")
    private String sign;

    public PayEntity() {
    }

    public PayEntity(String mch_id,
                     String amount,
                     String currency,
                     String coin_code,
                     String notify_url,
                     String redirect_url,
                     String order_id,
                     String customer_id,
                     String product_name,
                     String locale,
                     String sign) {
        this.mch_id = mch_id;
        this.amount = amount;
        this.currency = currency;
        this.coin_code = coin_code;
        this.notify_url = notify_url;
        this.redirect_url = redirect_url;
        this.order_id = order_id;
        this.customer_id = customer_id;
        this.product_name = product_name;
        this.locale = locale;
        this.sign = sign;
    }

    @Override
    public String toString() {

        return "PayEntity{" +
                "mch_id='" + mch_id + '\'' +
                ", amount='" + amount + '\'' +
                ", currency='" + currency + '\'' +
                ", coin_code='" + coin_code + '\'' +
                ", notify_url='" + notify_url + '\'' +
                ", redirect_url='" + redirect_url + '\'' +
                ", order_id='" + order_id + '\'' +
                ", customer_id='" + customer_id + '\'' +
                ", product_name='" + product_name + '\'' +
                ", locale='" + locale + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
