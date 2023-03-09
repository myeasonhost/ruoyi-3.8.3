package com.ruoyi.pay.task;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.pay.domain.OrgAccountOrderDaip;
import com.ruoyi.pay.service.IOrgAccountOrderDaipService;
import com.ruoyi.tron.service.ITronApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Slf4j
@Service
public class PdaiWorker {

    @Autowired
    private IOrgAccountOrderDaipService iOrgAccountOrderDaipService;
    @Autowired
    private ITronApiService tronApiServiceImpl;


    /**
     * 进行转账操作，1=提现中,2=提现成功，3=拒绝提现，4=转账异常
     */
    public void payNotify(OrgAccountOrderDaip order) throws Exception {
        order = iOrgAccountOrderDaipService.getById(order.getId());
        log.info("【提现通知】订单提现通知操作:商户{}，订单号:{}，当前订单状态status={}，开始提现", order.getSiteId(), order.getId(), order.getStatus());
        String status = "1";
        String info = "";
        if ("1".equals(order.getStatus())) {
            AjaxResult result = tronApiServiceImpl.transferUSDT(order.getOutAddress(), order.getCoinAddress(), Double.parseDouble(order.getCoinAmount()));
            if (result.get(AjaxResult.CODE_TAG).equals(500)) {
                status = "4";
                info = result.get(AjaxResult.MSG_TAG).toString();
            }
            if (result.get(AjaxResult.CODE_TAG).equals(200)) {
                status = "2";
                info = result.get(AjaxResult.MSG_TAG).toString();
            }
            LambdaUpdateWrapper<OrgAccountOrderDaip> updateWrapper = new LambdaUpdateWrapper();
            //1=提现中,2=提现成功，3=拒绝提现，4=转账异常
            updateWrapper.set(OrgAccountOrderDaip::getStatus, status)
                    .set(OrgAccountOrderDaip::getPayTime, new Date(System.currentTimeMillis()))
                    .set(OrgAccountOrderDaip::getRemark, info)
                    .eq(OrgAccountOrderDaip::getId, order.getId());
            iOrgAccountOrderDaipService.update(updateWrapper);
        }
    }

}
