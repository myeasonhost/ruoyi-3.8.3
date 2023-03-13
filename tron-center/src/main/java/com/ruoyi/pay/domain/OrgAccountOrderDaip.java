package com.ruoyi.pay.domain;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 商户代付对象 org_account_order_daip
 *
 * @author doctor
 * @date 2023-03-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("org_account_order_daip")
public class OrgAccountOrderDaip implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 支付订单号
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

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
    @Excel(name = "用户名", readConverterExp = "用户ID")
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
     * 订单币种（CNY/USD）
     */
    @Excel(name = "订单币种", readConverterExp = "CNY/USD")
    private String currency;

    /**
     * 支付金额（订单金额 + 订单币种汇率）
     */
    @Excel(name = "支付金额", readConverterExp = "订单金额币种汇率")
    private String coinAmount;

    /**
     * 支付币种（USDT/RMB）
     */
    @Excel(name = "支付币种", readConverterExp = "USDT/RMB")
    private String coinCode;

    /**
     * 转出地址
     */
    @Excel(name = "转出地址")
    private String outAddress;

    /**
     * 收款地址
     */
    @Excel(name = "收款地址")
    private String coinAddress;

    /**
     * 1=提现中,2=提现成功，3=拒绝提现
     */
    @Excel(name = "1=提现中,2=提现成功，3=拒绝提现")
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
    private int notifyTimes;

    /**
     * 最后通知时间
     */
    private Date lastNotifyTime;

    /**
     * 下一次通知时间
     */
    private Date nextNotifyTime;

    /**
     * 转账时间
     */
    @Excel(name = "转账时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /**
     * 回调通知地址
     */
    private String notifyUrl;

    /**
     * 交易事务ID
     */
    @Excel(name = "交易事务ID")
    private String transactionId;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

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
