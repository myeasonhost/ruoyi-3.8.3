package com.ruoyi.tron.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.tron.domain.TronEasonAddress;

import java.util.List;

/**
 * 总站账户Service接口
 *
 * @author eason
 * @date 2022-05-06
 */
public interface ITronEasonAddressService extends IService<TronEasonAddress> {

    /**
     * 查询列表
     */
    List<TronEasonAddress> queryList(TronEasonAddress tronEasonAddress);
}
