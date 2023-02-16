package com.ruoyi.pay.service;

import com.ruoyi.pay.domain.OrgAccountOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 支付订单Service接口
 *
 * @author doctor
 * @date 2023-02-15
 */
public interface IOrgAccountOrderService extends IService<OrgAccountOrder> {

    /**
     * 查询列表
     */
    List<OrgAccountOrder> queryList(OrgAccountOrder orgAccountOrder);
}
