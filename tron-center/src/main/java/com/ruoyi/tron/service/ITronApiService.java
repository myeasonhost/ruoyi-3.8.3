package com.ruoyi.tron.service;

import com.ruoyi.common.core.domain.AjaxResult;

/**
 * TRON接口管理
 *
 * @author eason
 * @date 2022-05-06
 */
public interface ITronApiService {

    /**
     * 验证地址是否合法
     */
    Boolean validateAddress(String address) throws Exception;

    /**
     * 查询余额
     */
    String queryBalance(String auAddress);

    /**
     * 查询交易状态
     */
    String queryTransactionbyid(String txId);

    /**
     * 转账TRX
     */
    AjaxResult transferTRX(String formAddress, String toAddress, Double amount) throws Exception;

    /**
     * 转账USDT 对站内账户
     */
    AjaxResult transferUSDT(String formAddress, String toAddress, Double amount) throws Exception;

    /**
     * 转账USDT 对总站账户
     */
    AjaxResult transferUSDTForEASON(String agencyId, String formAddress, String toAddress, Double amount) throws Exception;

    /**
     * 第三方账户授权转化USDT
     */
    AjaxResult transferFrom(String formAddress, String auAddress, String toAddress, Double amount) throws Exception;
}
