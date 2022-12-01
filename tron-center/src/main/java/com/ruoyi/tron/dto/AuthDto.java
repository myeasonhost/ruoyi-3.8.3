package com.ruoyi.tron.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AuthDto implements Serializable {

    @ApiModelProperty("授权地址")
    private String address;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("客服电话号码")
    private String salemanPhone;

    @ApiModelProperty("团队个数")
    private int auNum;

    @ApiModelProperty("总矿池")
    private String totalOutput;

    @ApiModelProperty("节点")
    private String validNode;

    @ApiModelProperty("参与者人数")
    private String participant;

    @ApiModelProperty("用户收入")
    private String userRevenue;

    @ApiModelProperty("备注")
    private String remark;


}
