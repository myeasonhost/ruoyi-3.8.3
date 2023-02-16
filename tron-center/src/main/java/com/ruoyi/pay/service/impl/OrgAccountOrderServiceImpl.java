package com.ruoyi.pay.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.ruoyi.pay.mapper.OrgAccountOrderMapper;
import com.ruoyi.pay.domain.OrgAccountOrder;
import com.ruoyi.pay.service.IOrgAccountOrderService;

import java.util.List;
import java.util.Map;

/**
 * 支付订单Service业务层处理
 *
 * @author doctor
 * @date 2023-02-15
 */
@Service
public class OrgAccountOrderServiceImpl extends ServiceImpl<OrgAccountOrderMapper, OrgAccountOrder> implements IOrgAccountOrderService {

    @Override
    public List<OrgAccountOrder> queryList(OrgAccountOrder orgAccountOrder) {
        LambdaQueryWrapper<OrgAccountOrder> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(orgAccountOrder.getSiteId())){
            lqw.eq(OrgAccountOrder::getSiteId ,orgAccountOrder.getSiteId());
        }
        if (StringUtils.isNotBlank(orgAccountOrder.getOrderId())){
            lqw.eq(OrgAccountOrder::getOrderId ,orgAccountOrder.getOrderId());
        }
        if (StringUtils.isNotBlank(orgAccountOrder.getUserId())){
            lqw.eq(OrgAccountOrder::getUserId ,orgAccountOrder.getUserId());
        }
        if (StringUtils.isNotBlank(orgAccountOrder.getStatus())){
            lqw.eq(OrgAccountOrder::getStatus ,orgAccountOrder.getStatus());
        }
        return this.list(lqw);
    }
}
