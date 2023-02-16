package com.ruoyi.pay.model;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author ：writer
 * @date ：Created in 2022/3/11 11:34
 * @description：支付业务参数基础类
 * @modified By：
 * @version: 0.0.1$
 */

public class BaseEntity {

    @ApiModelProperty("商户id")
    @NotNull(message = "商户id不能为空")
    @Range(min = 1, message = "商户id不能小于1")
    public Long  organizationId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
