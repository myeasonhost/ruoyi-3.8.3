package com.ruoyi.tron.service;

import com.ruoyi.tron.domain.OrgAccountAddress;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 商户账户Service接口
 *
 * @author doctor
 * @date 2022-07-22
 */
public interface IOrgAccountAddressService extends IService<OrgAccountAddress> {

    /**
     * 查询列表
     */
    List<OrgAccountAddress> queryList(OrgAccountAddress orgAccountAddress);
}
