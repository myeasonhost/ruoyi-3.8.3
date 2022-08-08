package com.ruoyi.tron.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.ruoyi.tron.mapper.OrgAccountAddressMapper;
import com.ruoyi.tron.domain.OrgAccountAddress;
import com.ruoyi.tron.service.IOrgAccountAddressService;

import java.util.List;

/**
 * 商户账户Service业务层处理
 *
 * @author doctor
 * @date 2022-07-22
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
        if (StringUtils.isNotBlank(orgAccountAddress.getHexAddress())){
            lqw.eq(OrgAccountAddress::getHexAddress ,orgAccountAddress.getHexAddress());
        }
        if (StringUtils.isNotBlank(orgAccountAddress.getPrivatekey())){
            lqw.eq(OrgAccountAddress::getPrivatekey ,orgAccountAddress.getPrivatekey());
        }
        if (StringUtils.isNotBlank(orgAccountAddress.getBalance())){
            lqw.eq(OrgAccountAddress::getBalance ,orgAccountAddress.getBalance());
        }
        if (StringUtils.isNotBlank(orgAccountAddress.getStatus())){
            lqw.eq(OrgAccountAddress::getStatus ,orgAccountAddress.getStatus());
        }
        return this.list(lqw);
    }
}
