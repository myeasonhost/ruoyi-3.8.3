package com.ruoyi.pay.service;

import com.ruoyi.pay.domain.OrgAccountOrderDaip;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.pay.dto.StatUsdtDto;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

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

    /**
     * 查询统计
     */
    Integer queryCount(StatUsdtDto statUsdtDto) throws ParseException;

    /**
     * 查询USDT
     */
    Map<String,Object> queryTotalUsdt(StatUsdtDto statUsdtDto) throws ParseException;
}
