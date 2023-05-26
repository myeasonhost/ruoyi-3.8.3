package com.ruoyi.pay.service;

import com.ruoyi.pay.domain.OrgAccountOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.pay.dto.StatUsdtDto;
import com.ruoyi.tron.domain.TronFish;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

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

    /**
     * 查询统计
     */
    Integer queryCount(StatUsdtDto statUsdtDto) throws ParseException;

    /**
     * 查询USDT
     */
    Map<String,Object> queryTotalUsdt(StatUsdtDto statUsdtDto) throws ParseException;
}
