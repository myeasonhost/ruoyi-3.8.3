package com.ruoyi.tron.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.ruoyi.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 商户账户对象 org_account_address
 *
 * @author doctor
 * @date 2022-07-22
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("org_account_address")
public class OrgAccountAddress implements Serializable {

private static final long serialVersionUID=1L;


    /** $column.columnComment */
    @TableId(value = "id")
    private Long id;

    /** 代理ID */
    @Excel(name = "代理ID")
    private String agencyId;

    /** 地址类型 */
    @Excel(name = "地址类型")
    private String addressType;

    /** 地址-Base58格式 */
    @Excel(name = "地址-Base58格式")
    private String address;

    /** 地址-Hex格式 */
    @Excel(name = "地址-Hex格式")
    private String hexAddress;

    /** 私钥 */
    @Excel(name = "私钥")
    private String privatekey;

    /** 余额 */
    @Excel(name = "余额")
    private String balance;

    /** 0=启用，1=禁用 */
    @Excel(name = "0=启用，1=禁用")
    private String status;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    /** $column.columnComment */
    private Date createTime;

    /** $column.columnComment */
    private Date updateTime;

    @TableField(exist = false)
    private Map<String, Object> params = new HashMap<>();
}
