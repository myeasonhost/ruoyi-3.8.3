package com.ruoyi.pay.task;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ruoyi.pay.domain.OrgAccountOrder;
import com.ruoyi.pay.service.IOrgAccountOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Slf4j
@Service
public class PayTimeOutWorker {

    @Autowired
    private IOrgAccountOrderService iOrgAccountOrderService;

    /**
     * 订单超时，关闭订单--status=3
     */
    public void payTimeOut(OrgAccountOrder order) throws Exception {
        order = iOrgAccountOrderService.getById(order.getId());
        log.info("【超时通知】订单超时通知操作:商户{}，订单号:{}，当前订单状态status={}，开始关闭订单", order.getSiteId(), order.getId(), order.getStatus());
        if ("1".equals(order.getStatus())) {
            LambdaUpdateWrapper<OrgAccountOrder> updateWrapper = new LambdaUpdateWrapper();
//            1=支付中,2=支付成功，3=支付超时
            updateWrapper.set(OrgAccountOrder::getStatus, "3")
                    .set(OrgAccountOrder::getPayTime, new Date(System.currentTimeMillis()))
                    .eq(OrgAccountOrder::getId, order.getId());
            iOrgAccountOrderService.update(updateWrapper);
        }
    }

}
