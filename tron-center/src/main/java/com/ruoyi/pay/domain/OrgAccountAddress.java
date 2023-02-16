package com.ruoyi.pay.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 收款地址对象 org_account_address
 *
 * @author doctor
 * @date 2023-02-15
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("org_account_address")
public class OrgAccountAddress implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * $column.columnComment
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 代理ID
     */
    @Excel(name = "代理ID")
    private String agencyId;

    /**
     * 地址类型
     */
    @Excel(name = "地址类型")
    private String addressType;

    /**
     * 用户地址
     */
    @Excel(name = "用户地址")
    private String address;

    /**
     * 私钥
     */
    private String privatekey;

    /**
     * 余额集合
     */
    @Excel(name = "余额集合")
    private String balance;

    /**
     * 累计收款金额
     */
    @Excel(name = "累计收款金额")
    private BigDecimal totalAmount;

    /**
     * 帐号状态（0正常 1停用）
     */
    @Excel(name = "帐号状态")
    private String status;

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
