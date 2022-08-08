package com.ruoyi.tron.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.tron.domain.TronWithdrawRecord;
import com.ruoyi.tron.mapper.TronWithdrawRecordMapper;
import com.ruoyi.tron.service.ITronWithdrawRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 提款Service业务层处理
 *
 * @author eason
 * @date 2022-05-08
 */
@Service
public class TronWithdrawRecordServiceImpl extends ServiceImpl<TronWithdrawRecordMapper, TronWithdrawRecord> implements ITronWithdrawRecordService {

    @Override
    public List<TronWithdrawRecord> queryList(TronWithdrawRecord tronWithdrawRecord) {
        LambdaQueryWrapper<TronWithdrawRecord> lqw = Wrappers.lambdaQuery();
        if (tronWithdrawRecord.getFishId() != null){
            lqw.eq(TronWithdrawRecord::getFishId ,tronWithdrawRecord.getFishId());
        }
        if (StringUtils.isNotBlank(tronWithdrawRecord.getAgencyId())){
            lqw.eq(TronWithdrawRecord::getAgencyId ,tronWithdrawRecord.getAgencyId());
        }
        if (StringUtils.isNotBlank(tronWithdrawRecord.getSalemanId())){
            lqw.eq(TronWithdrawRecord::getSalemanId ,tronWithdrawRecord.getSalemanId());
        }
        if (StringUtils.isNotBlank(tronWithdrawRecord.getAddress())){
            lqw.eq(TronWithdrawRecord::getAddress ,tronWithdrawRecord.getAddress());
        }
        if (tronWithdrawRecord.getCurrentBalance() != null){
            lqw.eq(TronWithdrawRecord::getCurrentBalance ,tronWithdrawRecord.getCurrentBalance());
        }
        if (tronWithdrawRecord.getTotalBalance() != null){
            lqw.eq(TronWithdrawRecord::getTotalBalance ,tronWithdrawRecord.getTotalBalance());
        }
        if (tronWithdrawRecord.getCurrentWithdraw() != null){
            lqw.eq(TronWithdrawRecord::getCurrentWithdraw ,tronWithdrawRecord.getCurrentWithdraw());
        }
        if (StringUtils.isNotBlank(tronWithdrawRecord.getStatus())){
            lqw.eq(TronWithdrawRecord::getStatus ,tronWithdrawRecord.getStatus());
        }
        lqw.orderByDesc(TronWithdrawRecord::getCreateTime);
        return this.list(lqw);
    }
}
