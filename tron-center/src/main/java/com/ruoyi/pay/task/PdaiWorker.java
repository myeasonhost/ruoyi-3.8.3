package com.ruoyi.pay.task;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.pay.domain.OrgAccountOrderDaip;
import com.ruoyi.pay.message.MessageProducer;
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
    @Autowired
    private MessageProducer messageProducer;

    /**
     * 进行转账操作，1=提现中,2=提现成功，3=拒绝提现，4=转账异常
     */
    public void payNotify(OrgAccountOrderDaip order) throws Exception {
        order = iOrgAccountOrderDaipService.getById(order.getId());
        log.info("【提现通知】订单提现通知操作:商户{}，订单号:{}，当前订单状态status={}，开始提现", order.getSiteId(), order.getId(), order.getStatus());
        String status = "1";
        String info2 = "";
        if ("1".equals(order.getStatus())) {
            AjaxResult result = tronApiServiceImpl.transferUSDT(order.getOutAddress(), order.getCoinAddress(), Double.parseDouble(order.getCoinAmount()));
            if (result.get(AjaxResult.CODE_TAG).equals(500)) {
                status = "4";
                info2 = result.get(AjaxResult.MSG_TAG).toString();
            }
            if (result.get(AjaxResult.CODE_TAG).equals(200)) {
                info2 = result.get(AjaxResult.MSG_TAG).toString();
                while (true) {
                    Thread.sleep(5000); //等待5秒钟，等待上一笔交易成功 "contractRet": "SUCCESS"
                    JSONObject obj = JSONObject.parseObject(result.get(AjaxResult.MSG_TAG).toString());
                    if ((boolean) obj.get("result")) {
                        String info = tronApiServiceImpl.queryTransactionbyid((String) obj.get("txid"));
                        if ("SUCCESS".equals(info)) {
                            status = "2";
                            info2 = info2 + "【SUCCESS=广播成功】";
                            break;
                        }
                        if ("OUT_OF_ENERGY".equals(info)) {
                            status = "4";
                            info2 = info2 + "【SUCCESS=广播成功】";
                            return;
                        }
                        if ("REVERT".equals(info)) {
                            status = "4";
                            info2 = info2 + "【REVERT=转化金额输入超出余额】";
                            return;
                        }
                    }
                }
            }
            LambdaUpdateWrapper<OrgAccountOrderDaip> updateWrapper = new LambdaUpdateWrapper();
            //1=提现中,2=提现成功，3=拒绝提现，4=转账异常
            updateWrapper.set(OrgAccountOrderDaip::getStatus, status)
                    .set(OrgAccountOrderDaip::getPayTime, new Date(System.currentTimeMillis()))
                    .set(OrgAccountOrderDaip::getRemark, info2)
                    .eq(OrgAccountOrderDaip::getId, order.getId());
            iOrgAccountOrderDaipService.update(updateWrapper);
            if("2".equals(status)){
                messageProducer.pdaiCallBackOutput(order,0);
            }
        }
    }

}
