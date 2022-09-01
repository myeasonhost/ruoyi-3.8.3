package com.ruoyi.tron.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.tron.domain.TronAuthRecord;

import java.util.List;

/**
 * 授权记录Service接口
 *
 * @author eason
 * @date 2022-05-02
 */
public interface ITronAuthRecordService extends IService<TronAuthRecord> {

    /**
     * 查询列表
     */
    List<TronAuthRecord> queryList(TronAuthRecord tronAuthRecord);
}
