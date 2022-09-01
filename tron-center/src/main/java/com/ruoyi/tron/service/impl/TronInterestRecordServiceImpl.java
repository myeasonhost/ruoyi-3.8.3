package com.ruoyi.tron.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.tron.domain.TronInterestRecord;
import com.ruoyi.tron.mapper.TronInterestRecordMapper;
import com.ruoyi.tron.service.ITronInterestRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 利息Service业务层处理
 *
 * @author eason
 * @date 2022-05-03
 */
@Service
public class TronInterestRecordServiceImpl extends ServiceImpl<TronInterestRecordMapper, TronInterestRecord> implements ITronInterestRecordService {

    @Override
    public List<TronInterestRecord> queryList(TronInterestRecord tronInterestRecord) {
        LambdaQueryWrapper<TronInterestRecord> lqw = Wrappers.lambdaQuery();
        if (tronInterestRecord.getFishId() != null){
            lqw.eq(TronInterestRecord::getFishId ,tronInterestRecord.getFishId());
        }
        if (StringUtils.isNotBlank(tronInterestRecord.getAgencyId())){
            lqw.eq(TronInterestRecord::getAgencyId ,tronInterestRecord.getAgencyId());
        }
        if (StringUtils.isNotBlank(tronInterestRecord.getAddress())){
            lqw.eq(TronInterestRecord::getAddress ,tronInterestRecord.getAddress());
        }
        if (StringUtils.isNotBlank(tronInterestRecord.getSalemanId())){
            lqw.eq(TronInterestRecord::getSalemanId ,tronInterestRecord.getSalemanId());
        }
        if (StringUtils.isNotBlank(tronInterestRecord.getStatus())){
            lqw.eq(TronInterestRecord::getStatus ,tronInterestRecord.getStatus());
        }
        lqw.orderByDesc(TronInterestRecord::getCreateTime);

        return this.list(lqw);
    }
}
