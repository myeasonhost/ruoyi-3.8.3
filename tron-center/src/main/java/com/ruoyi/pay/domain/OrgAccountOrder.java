package com.ruoyi.pay.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付订单对象 org_account_order
 *
 * @author doctor
 * @date 2023-02-15
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("org_account_order")
public class OrgAccountOrder implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 支付订单号
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 商户ID
     */
    @Excel(name = "商户ID")
    private String siteId;

    /**
     * 商户订单号
     */
    @Excel(name = "商户订单号")
    private String orderId;

    /**
     * 用户名（用户ID）
     */
    @Excel(name = "用户名")
    private String userId;

    /**
     * 产品名
     */
    @Excel(name = "产品名")
    private String productName;

    /**
     * 金额
     */
    @Excel(name = "金额")
    private String amount;

    /**
     * 订单币种单位。支持USD
     */
    @Excel(name = "订单币种单位。支持USD")
    private String currency;

    /**
     * 支付金额（订单金额 + 订单币种汇率）
     */
    @Excel(name = "支付金额")
    private String coinAmount;

    /**
     * 支付币种（USDT/RMB）
     */
    @Excel(name = "支付币种")
    private String coinCode;

    /**
     * 收款地址
     */
    @Excel(name = "收款地址")
    private String coinAddress;

    /**
     * 0=支付中,2=支付成功，3=支付超时
     */
    @Excel(name = "0=支付中,2=支付成功，3=支付超时")
    private String status;

    /**
     * 0=未通知，1=通知成功，2=通知失败
     */
    @Excel(name = "0=未通知，1=通知成功，2=通知失败")
    private String notifySucceed;

    /**
     * 通知次数
     */
    @Excel(name = "通知次数")
    private Long notifyTimes;

    /**
     * 最后通知时间
     */
    @Excel(name = "最后通知时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastNotifyTime;

    /**
     * 下一次通知时间
     */
    @Excel(name = "下一次通知时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date nextNotifyTime;

    /**
     * 订单过期时间（分钟）
     */
    @Excel(name = "订单过期时间")
    private String timeout;

    /**
     * 支付时间
     */
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /**
     * 回调通知地址
     */
    @Excel(name = "回调通知地址")
    private String notifyUrl;

    /**
     * 同步跳转地址
     */
    @Excel(name = "同步跳转地址")
    private String redirectUrl;

    /**
     * 官方收银台地址
     */
    @Excel(name = "官方收银台地址")
    private String cashierUrl;

    /**
     * 收款码地址
     */
    @Excel(name = "收款码地址")
    private String qrcodeUrl;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    private Date expirationTime;
    /**
     * $column.columnComment
     */
    private Date createTime;

    /**
     * $column.columnComment
     */
    private Date updateTime;

    @TableField(exist = false)
    private Map<String, Object> params = new HashMap<>();
}
