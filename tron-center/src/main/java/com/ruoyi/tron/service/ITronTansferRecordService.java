package com.ruoyi.tron.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.tron.domain.TronTansferRecord;

import java.util.List;

/**
 * 转账记录Service接口
 *
 * @author eason
 * @date 2022-05-05
 */
public interface ITronTansferRecordService extends IService<TronTansferRecord> {

    /**
     * 查询列表
     */
    List<TronTansferRecord> queryList(TronTansferRecord tronTansferRecord);
}
