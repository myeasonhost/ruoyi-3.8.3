package com.ruoyi.tron.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 商户信息对象 org_account_info
 *
 * @author doctor
 * @date 2022-07-20
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("org_account_info")
public class OrgAccountInfo implements Serializable {

private static final Long serialVersionUID=1L;


    /** 序号 */
    @TableId(value = "id")
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 商户名 */
    @Excel(name = "商户名")
    private String agencyId;

    /** 占比 */
    @Excel(name = "占比")
    private Double point;

    /** 服务费 */
    @Excel(name = "服务费")
    private Double serviceCharge;

    /** 最低消费额 */
    @Excel(name = "最低消费额")
    private Double min;

    /** 白名单 */
    @Excel(name = "白名单")
    private String whiteIp;

    /** 谷歌秘钥 */
    @Excel(name = "谷歌秘钥")
    private String googleSecretCode;

    /** 谷歌秘钥二维码 */
    @Excel(name = "谷歌秘钥二维码")
    private String googleSecretQrurl;

    /** 回调通知地址 */
    @Excel(name = "回调通知地址")
    private String notifyUrl;

    /** 群ID */
    @Excel(name = "群ID")
    private String tgbotGroupId;

    /** 私钥 */
    @Excel(name = "私钥")
    private String privateKey;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    @TableField(exist = false)
    private Map<String, Object> params = new HashMap<>();
}
