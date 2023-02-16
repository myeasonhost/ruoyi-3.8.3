package com.ruoyi.pay.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.ruoyi.pay.mapper.OrgAccountAddressMapper;
import com.ruoyi.pay.domain.OrgAccountAddress;
import com.ruoyi.pay.service.IOrgAccountAddressService;

import java.util.List;
import java.util.Map;

/**
 * 收款地址Service业务层处理
 *
 * @author doctor
 * @date 2023-02-15
 */
@Service
public class OrgAccountAddressServiceImpl extends ServiceImpl<OrgAccountAddressMapper, OrgAccountAddress> implements IOrgAccountAddressService {

    @Override
    public List<OrgAccountAddress> queryList(OrgAccountAddress orgAccountAddress) {
        LambdaQueryWrapper<OrgAccountAddress> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(orgAccountAddress.getAgencyId())){
            lqw.eq(OrgAccountAddress::getAgencyId ,orgAccountAddress.getAgencyId());
        }
        if (StringUtils.isNotBlank(orgAccountAddress.getAddressType())){
            lqw.eq(OrgAccountAddress::getAddressType ,orgAccountAddress.getAddressType());
        }
        if (StringUtils.isNotBlank(orgAccountAddress.getAddress())){
            lqw.eq(OrgAccountAddress::getAddress ,orgAccountAddress.getAddress());
        }
        if (StringUtils.isNotBlank(orgAccountAddress.getStatus())){
            lqw.eq(OrgAccountAddress::getStatus ,orgAccountAddress.getStatus());
        }
        return this.list(lqw);
    }
}
