package com.ruoyi.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.pay.domain.OrgAccountOrderDaip;
import com.ruoyi.pay.mapper.OrgAccountOrderDaipMapper;
import com.ruoyi.pay.service.IOrgAccountOrderDaipService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商户代付Service业务层处理
 *
 * @author doctor
 * @date 2023-03-09
 */
@Service
public class OrgAccountOrderDaipServiceImpl extends ServiceImpl<OrgAccountOrderDaipMapper, OrgAccountOrderDaip> implements IOrgAccountOrderDaipService {

    @Override
    public List<OrgAccountOrderDaip> queryList(OrgAccountOrderDaip orgAccountOrderDaip) {
        LambdaQueryWrapper<OrgAccountOrderDaip> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(orgAccountOrderDaip.getSiteId())) {
            lqw.eq(OrgAccountOrderDaip::getSiteId, orgAccountOrderDaip.getSiteId());
        }
        if (StringUtils.isNotBlank(orgAccountOrderDaip.getOrderId())) {
            lqw.eq(OrgAccountOrderDaip::getOrderId, orgAccountOrderDaip.getOrderId());
        }
        if (StringUtils.isNotBlank(orgAccountOrderDaip.getUserId())) {
            lqw.eq(OrgAccountOrderDaip::getUserId, orgAccountOrderDaip.getUserId());
        }
        if (StringUtils.isNotBlank(orgAccountOrderDaip.getCoinAddress())) {
            lqw.eq(OrgAccountOrderDaip::getCoinAddress, orgAccountOrderDaip.getCoinAddress());
        }
        if (StringUtils.isNotBlank(orgAccountOrderDaip.getStatus())) {
            lqw.eq(OrgAccountOrderDaip::getStatus, orgAccountOrderDaip.getStatus());
        }
        lqw.orderByDesc(OrgAccountOrderDaip::getCreateTime);
        return this.list(lqw);
    }
}
