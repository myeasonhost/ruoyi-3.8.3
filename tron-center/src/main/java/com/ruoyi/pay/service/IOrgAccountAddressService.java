package com.ruoyi.pay.service;

import com.ruoyi.pay.domain.OrgAccountAddress;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 收款地址Service接口
 *
 * @author doctor
 * @date 2023-02-15
 */
public interface IOrgAccountAddressService extends IService<OrgAccountAddress> {

    /**
     * 查询列表
     */
    List<OrgAccountAddress> queryList(OrgAccountAddress orgAccountAddress);
}
