package com.ruoyi.tron.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.tron.domain.TronWithdrawRecord;

import java.util.List;

/**
 * 提款Service接口
 *
 * @author ruoyi
 * @date 2022-05-08
 */
public interface ITronWithdrawRecordService extends IService<TronWithdrawRecord> {

    /**
     * 查询列表
     */
    List<TronWithdrawRecord> queryList(TronWithdrawRecord tronWithdrawRecord);
}
