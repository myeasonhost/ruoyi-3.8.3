package com.ruoyi.pay.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class StatUsdtDto implements Serializable {

    private String agencyId;

    private String salemanId;

    @ApiModelProperty("今日充值笔数")
    private Integer dayPayCount;

    @ApiModelProperty("今日入U")
    private Double dayPayUsdt;

    @ApiModelProperty("累计充值笔数")
    private Integer totalPayCount;

    @ApiModelProperty("累计入U")
    private Double totalPayUsdt;

    @ApiModelProperty("今日提现笔数")
    private Integer dayWithdrawCount;

    @ApiModelProperty("今日出U")
    private Double dayWithdrawUsdt;

    @ApiModelProperty("累计提现笔数")
    private Integer totalWithdrawCount;

    @ApiModelProperty("累计出U")
    private Double totalWithdrawUsdt;

    private Date createTime;

}
