package com.ruoyi.pay.service;

import com.ruoyi.pay.domain.OrgAccountOrderDaip;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 商户代付Service接口
 *
 * @author doctor
 * @date 2023-03-09
 */
public interface IOrgAccountOrderDaipService extends IService<OrgAccountOrderDaip> {

    /**
     * 查询列表
     */
    List<OrgAccountOrderDaip> queryList(OrgAccountOrderDaip orgAccountOrderDaip);
}
