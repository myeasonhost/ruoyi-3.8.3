package com.ruoyi.pay.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "PdaiEntity", description = "代付订单实体")
public class PdaiEntity {
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

    @ApiModelProperty(value = "用户收款地址", required = true, position = 9)
    @NotNull(message = "用户地址不能为空")
    private String address;

    @ApiModelProperty(value = "签名", required = true, position = 10)
    @NotNull(message = "签名不能为空")
    private String sign;

}
