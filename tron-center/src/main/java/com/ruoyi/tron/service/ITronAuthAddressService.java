package com.ruoyi.tron.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.tron.domain.TronAuthAddress;

import java.util.List;

/**
 * 授权Service接口
 *
 * @author eason
 * @date 2022-04-20
 */
public interface ITronAuthAddressService extends IService<TronAuthAddress> {

    /**
     * 查询列表
     */
    List<TronAuthAddress> queryList(TronAuthAddress tronAuthAddress);

    /**
     * 查询代理名ID
     */
    String queryAgent(long deptId);

    /**
     * 获取链接地址
     */
    String getAuthAddressUri(String key);
}
