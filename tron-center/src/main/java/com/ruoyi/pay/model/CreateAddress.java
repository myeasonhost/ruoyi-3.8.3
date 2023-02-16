package com.ruoyi.pay.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "CreateAddress", description = "订单接口参数")
public class CreateAddress extends BaseEntity {

    @ApiModelProperty("钱包地址充值后,该地址会收到充值通知()")
    @NotNull(message = "支付回调地址不能为空")
    @Length(min = 8, max = 127, message = "支付回调地址长度范围是[8,127]")
    private String notifyUrl;

    @ApiModelProperty("用户id:必填")
    @Range(min = 0, message = "钱包id必须大于0")
    private Long userId = 0L;

    @ApiModelProperty("数字货币类型:可不填写")
    private Long coinType = 0L;

    @ApiModelProperty("钱包的别称:可不填写")
    @Length(max = 64, message = "钱包的别称地址长度范围是[0,64]")
    String alias = "";

    @ApiModelProperty("sign签名")
    @Length(max = 64, message = "字段ASC排序:md5(alias=4&coinType=5&notifyUrl=2&organizationId=1&userId=3)")
    String sign = "";

    @Override
    public String toString() {
        return "alias=" + alias + "&coinType=" + coinType + "&notifyUrl=" + notifyUrl + "&organizationId=" + organizationId + "&userId=" + userId;
    }

}
