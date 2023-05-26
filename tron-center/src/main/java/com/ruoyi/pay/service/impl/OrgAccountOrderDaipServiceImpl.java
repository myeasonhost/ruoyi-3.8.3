package com.ruoyi.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.pay.domain.OrgAccountOrder;
import com.ruoyi.pay.domain.OrgAccountOrderDaip;
import com.ruoyi.pay.dto.StatUsdtDto;
import com.ruoyi.pay.mapper.OrgAccountOrderDaipMapper;
import com.ruoyi.pay.service.IOrgAccountOrderDaipService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 商户代付Service业务层处理
 *
 * @author doctor
 * @date 2023-03-09
 */
@Service
public class OrgAccountOrderDaipServiceImpl extends ServiceImpl<OrgAccountOrderDaipMapper, OrgAccountOrderDaip> implements IOrgAccountOrderDaipService {

    @Override
    public List<OrgAccountOrderDaip> queryList(OrgAccountOrderDaip orgAccountOrderDaip) {
        LambdaQueryWrapper<OrgAccountOrderDaip> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(orgAccountOrderDaip.getSiteId())) {
            lqw.eq(OrgAccountOrderDaip::getSiteId, orgAccountOrderDaip.getSiteId());
        }
        if (StringUtils.isNotBlank(orgAccountOrderDaip.getOrderId())) {
            lqw.eq(OrgAccountOrderDaip::getOrderId, orgAccountOrderDaip.getOrderId());
        }
        if (StringUtils.isNotBlank(orgAccountOrderDaip.getUserId())) {
            lqw.eq(OrgAccountOrderDaip::getUserId, orgAccountOrderDaip.getUserId());
        }
        if (StringUtils.isNotBlank(orgAccountOrderDaip.getCoinAddress())) {
            lqw.eq(OrgAccountOrderDaip::getCoinAddress, orgAccountOrderDaip.getCoinAddress());
        }
        if (StringUtils.isNotBlank(orgAccountOrderDaip.getStatus())) {
            lqw.eq(OrgAccountOrderDaip::getStatus, orgAccountOrderDaip.getStatus());
        }
        lqw.orderByDesc(OrgAccountOrderDaip::getCreateTime);
        return this.list(lqw);
    }

    @Override
    public Integer queryCount(StatUsdtDto statUsdtDto) throws ParseException {
        LambdaQueryWrapper<OrgAccountOrderDaip> lqw = Wrappers.lambdaQuery();
        lqw.eq(OrgAccountOrderDaip::getStatus, "2");
        if (StringUtils.isNotBlank(statUsdtDto.getAgencyId())){
            lqw.eq(OrgAccountOrderDaip::getSiteId ,statUsdtDto.getAgencyId());
        }
        if (statUsdtDto.getCreateTime()!=null){
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            statUsdtDto.setCreateTime(sdf.parse(sdf.format(statUsdtDto.getCreateTime())));
            lqw.ge(OrgAccountOrderDaip::getCreateTime ,statUsdtDto.getCreateTime()); //ne	不等于<>   gt大于> ge大于等于>= lt小于< le小于等于<=
        }
        lqw.orderByDesc(OrgAccountOrderDaip::getCreateTime);

        return this.count(lqw);
    }

    @Override
    public Map<String,Object> queryTotalUsdt(StatUsdtDto statUsdtDto) throws ParseException{
        QueryWrapper<OrgAccountOrderDaip> queryWrapper = Wrappers.query();
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
