package com.ruoyi.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.pay.domain.OrgAccountOrder;
import com.ruoyi.pay.dto.StatUsdtDto;
import com.ruoyi.pay.mapper.OrgAccountOrderMapper;
import com.ruoyi.pay.service.IOrgAccountOrderService;
import com.ruoyi.tron.domain.TronFish;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 支付订单Service业务层处理
 *
 * @author doctor
 * @date 2023-02-15
 */
@Service
public class OrgAccountOrderServiceImpl extends ServiceImpl<OrgAccountOrderMapper, OrgAccountOrder> implements IOrgAccountOrderService {

    @Override
    public List<OrgAccountOrder> queryList(OrgAccountOrder orgAccountOrder) {
        LambdaQueryWrapper<OrgAccountOrder> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(orgAccountOrder.getSiteId())) {
            lqw.eq(OrgAccountOrder::getSiteId, orgAccountOrder.getSiteId());
        }
        if (StringUtils.isNotBlank(orgAccountOrder.getOrderId())) {
            lqw.eq(OrgAccountOrder::getOrderId, orgAccountOrder.getOrderId());
        }
        if (StringUtils.isNotBlank(orgAccountOrder.getUserId())) {
            lqw.eq(OrgAccountOrder::getUserId, orgAccountOrder.getUserId());
        }
        if (StringUtils.isNotBlank(orgAccountOrder.getStatus())) {
            lqw.eq(OrgAccountOrder::getStatus, orgAccountOrder.getStatus());
        }
        lqw.orderByDesc(OrgAccountOrder::getCreateTime);
        return this.list(lqw);
    }

    @Override
    public Integer queryCount(StatUsdtDto statUsdtDto) throws ParseException {
        LambdaQueryWrapper<OrgAccountOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(OrgAccountOrder::getStatus, "2");
        if (StringUtils.isNotBlank(statUsdtDto.getAgencyId())){
            lqw.eq(OrgAccountOrder::getSiteId ,statUsdtDto.getAgencyId());
        }
        if (statUsdtDto.getCreateTime()!=null){
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            statUsdtDto.setCreateTime(sdf.parse(sdf.format(statUsdtDto.getCreateTime())));
            lqw.ge(OrgAccountOrder::getCreateTime ,statUsdtDto.getCreateTime()); //ne	不等于<>   gt大于> ge大于等于>= lt小于< le小于等于<=
        }
        lqw.orderByDesc(OrgAccountOrder::getCreateTime);

        return this.count(lqw);
    }

    @Override
    public Map<String,Object> queryTotalUsdt(StatUsdtDto statUsdtDto) throws ParseException{
        QueryWrapper<OrgAccountOrder> queryWrapper = Wrappers.query();
        queryWrapper.select("IFNULL(SUM(coin_amount),0) as usdt");
        queryWrapper.eq("status","2");
        if (StringUtils.isNotBlank(statUsdtDto.getAgencyId())){
            queryWrapper.eq("site_id" ,statUsdtDto.getAgencyId());
        }
        if (statUsdtDto.getCreateTime()!=null){
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            statUsdtDto.setCreateTime(sdf.parse(sdf.format(statUsdtDto.getCreateTime())));
            queryWrapper.ge("create_time" ,statUsdtDto.getCreateTime()); //ne	不等于<>   gt大于> ge大于等于>= lt小于< le小于等于<=
        }
        return this.getMap(queryWrapper);
    }
}
