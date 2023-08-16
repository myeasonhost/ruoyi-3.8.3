package com.ruoyi.pay.task;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ruoyi.pay.domain.OrgAccountOrder;
import com.ruoyi.pay.domain.OrgAccountOrderDaip;
import com.ruoyi.pay.message.MessageProducer;
import com.ruoyi.pay.service.IOrgAccountOrderDaipService;
import com.ruoyi.pay.service.IOrgAccountOrderService;
import com.ruoyi.tron.domain.OrgAccountInfo;
import com.ruoyi.tron.service.IOrgAccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
@Service
public class CallbackNotifyWorker {

    @Autowired
    private IOrgAccountOrderService orgAccountOrderService;
    @Autowired
    private IOrgAccountOrderDaipService orgAccountOrderDaipService;
    @Autowired
    private IOrgAccountInfoService orgAccountInfoService;
    @Autowired
    private MessageProducer messageProducer;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 支付回调
     * @param order
     * @return
     */
    public boolean payNotify(OrgAccountOrder order) {
        order = this.orgAccountOrderService.getById(order.getId());
        log.info("【支付回调】回调通知操作:商户{}，订单号:{}，当前订单状态status={}", order.getSiteId(), order.getId(), order.getStatus());
        if ("1".equals(order.getNotifySucceed())) {
            return true;
        }
        if (!StringUtils.hasText(order.getNotifyUrl())) {
            LambdaUpdateWrapper<OrgAccountOrder> updateWrapper = new LambdaUpdateWrapper();
            //0=未通知，1=通知成功，2=通知失败
            updateWrapper.set(OrgAccountOrder::getNotifySucceed, "2")
                    .set(OrgAccountOrder::getLastNotifyTime, new Date(System.currentTimeMillis()))
                    .eq(OrgAccountOrder::getId, order.getId());
            orgAccountOrderService.update(updateWrapper);
            log.info("【支付回调】回调通知操作:商户{}，订单号:{}，通知地址错误,充值通知失败", order.getSiteId(), order.getId());
            return true;
        }
        LambdaQueryWrapper<OrgAccountInfo> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OrgAccountInfo::getAgencyId, order.getSiteId());
        OrgAccountInfo orgAccountInfo = orgAccountInfoService.getOne(lambdaQueryWrapper);

        Map<String, Object> appParamMap = new TreeMap<>();
        appParamMap.put("pay_id", order.getId());
        appParamMap.put("order_id", order.getOrderId());
        appParamMap.put("amount", order.getAmount());
        appParamMap.put("currency", order.getCurrency());
        appParamMap.put("coin_code", order.getCoinCode());
        appParamMap.put("coin_amount", order.getCoinAmount());
        appParamMap.put("pay_time", order.getPayTime() == null ? System.currentTimeMillis() : order.getPayTime().getTime());
        appParamMap.put("status", order.getStatus()); //1=支付中,2=支付成功，3=支付超时
        appParamMap.put("hash", order.getTransactionId());

        StringBuffer orgin = new StringBuffer();
        Iterator iter = appParamMap.keySet().iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            orgin.append("&").append(name).append("=").append(appParamMap.get(name));
        }
        orgin.append("&").append("key").append("=").append(orgAccountInfo.getPrivateKey());
        orgin.deleteCharAt(0);
        String sign = DigestUtil.md5Hex(orgin.toString());
        appParamMap.put("sign", sign);

        log.info("【支付回调】商户订单号:{}，回调通知操作:商户{}，订单号:{}", order.getOrderId(), order.getSiteId(), order.getId());

        Long timeStamp = System.currentTimeMillis();
        String responseString = null;
        try {
            log.info("【支付回调】商户订单号:{}，回调地址={}", order.getOrderId(), order.getNotifyUrl());
            log.info("【支付回调】商户订单号:{}，回调地址参数={}", order.getOrderId(), appParamMap);

            responseString = HttpUtil.post(order.getNotifyUrl(), appParamMap);
            log.info("【支付回调】商户订单号:{}，回调通知操作：通知成功，第三方回调返回={}", order.getOrderId(), responseString);
            if (!"SUCCESS".equalsIgnoreCase(responseString)) {
                throw new Exception(responseString);
            }
            String jsonObject1 = JSONObject.toJSONString(order);
            redisTemplate.convertAndSend("sendMsgPay", jsonObject1);
        } catch (Exception e) {
            log.error("【支付回调】商户订单号:{}，回调通知操作：URL={}，回调出错={}", order.getOrderId(), order.getNotifyUrl(), e.getMessage());
            if (order.getNotifyTimes() < 3) {
                order.setNotifyTimes(order.getNotifyTimes() + 1);
                Integer dalayTime = getTimeForNetNotify(order.getNotifyTimes());
                LambdaUpdateWrapper<OrgAccountOrder> updateWrapper = new LambdaUpdateWrapper();
                //0=未通知，1=通知成功，2=通知失败
                updateWrapper.set(OrgAccountOrder::getNotifyTimes, order.getNotifyTimes())
                        .set(OrgAccountOrder::getLastNotifyTime, new Date(System.currentTimeMillis()))
                        .set(OrgAccountOrder::getNextNotifyTime, new Date(timeStamp + dalayTime * 1000))
                        .eq(OrgAccountOrder::getId, order.getId());
                orgAccountOrderService.update(updateWrapper);

                log.info("【支付回调】商户订单号:{}，回调通知操作：重试次数={}，回调错误={}", order.getOrderId(), order.getNotifyTimes(), e.getMessage());
                this.messageProducer.callBackOutput(order, dalayTime);
            } else {
                log.info("【支付回调】商户订单号:{}，回调通知操作：通知结束，累计通知回调次数{},通知失败{}，", order.getOrderId(), order.getNotifyTimes(), e.getMessage());
                LambdaUpdateWrapper<OrgAccountOrder> updateWrapper = new LambdaUpdateWrapper();
                updateWrapper.set(OrgAccountOrder::getNotifySucceed, "2")
                        .set(OrgAccountOrder::getLastNotifyTime, new Date(System.currentTimeMillis()))
                        .eq(OrgAccountOrder::getId, order.getId());
                orgAccountOrderService.update(updateWrapper);
            }
            return true;
        }
        LambdaUpdateWrapper<OrgAccountOrder> updateWrapper = new LambdaUpdateWrapper();

        if ("SUCCESS".equalsIgnoreCase(responseString)) {  //返回结果SUCCESS
            updateWrapper.set(OrgAccountOrder::getNotifySucceed, "1");
        } else {
            updateWrapper.set(OrgAccountOrder::getNotifySucceed, "2");
        }
        updateWrapper.set(OrgAccountOrder::getLastNotifyTime, new Date(System.currentTimeMillis()));
        updateWrapper.eq(OrgAccountOrder::getId, order.getId());
        orgAccountOrderService.update(updateWrapper);
        return true;
    }

    /**
     * 待付回调
     * @param order
     * @return
     */
    public boolean pdaiNotify(OrgAccountOrderDaip order) {
        order = this.orgAccountOrderDaipService.getById(order.getId());
        log.info("【待付回调】回调通知操作:商户{}，订单号:{}，当前订单状态status={}", order.getSiteId(), order.getId(), order.getStatus());
        if ("1".equals(order.getNotifySucceed())) {
            return true;
        }
        if (!StringUtils.hasText(order.getNotifyUrl())) {
            LambdaUpdateWrapper<OrgAccountOrderDaip> updateWrapper = new LambdaUpdateWrapper();
            //0=未通知，1=通知成功，2=通知失败
            updateWrapper.set(OrgAccountOrderDaip::getNotifySucceed, "2")
                    .set(OrgAccountOrderDaip::getLastNotifyTime, new Date(System.currentTimeMillis()))
                    .eq(OrgAccountOrderDaip::getId, order.getId());
            orgAccountOrderDaipService.update(updateWrapper);
            log.info("【待付回调】回调通知操作:商户{}，订单号:{}，通知地址错误,充值通知失败", order.getSiteId(), order.getId());
            return true;
        }
        LambdaQueryWrapper<OrgAccountInfo> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OrgAccountInfo::getAgencyId, order.getSiteId());
        OrgAccountInfo orgAccountInfo = orgAccountInfoService.getOne(lambdaQueryWrapper);

        Map<String, Object> appParamMap = new TreeMap<>();
        appParamMap.put("pay_id", order.getId());
        appParamMap.put("order_id", order.getOrderId());
        appParamMap.put("amount", order.getAmount());
        appParamMap.put("currency", order.getCurrency());
        appParamMap.put("coin_code", order.getCoinCode());
        appParamMap.put("coin_amount", order.getCoinAmount());
        appParamMap.put("pay_time", order.getPayTime() == null ? System.currentTimeMillis() : order.getPayTime().getTime());
        appParamMap.put("status", order.getStatus()); //1=支付中,2=支付成功，3=支付超时
        appParamMap.put("hash", order.getTransactionId());

        StringBuffer orgin = new StringBuffer();
        Iterator iter = appParamMap.keySet().iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            orgin.append("&").append(name).append("=").append(appParamMap.get(name));
        }
        orgin.append("&").append("key").append("=").append(orgAccountInfo.getPrivateKey());
        orgin.deleteCharAt(0);
        String sign = DigestUtil.md5Hex(orgin.toString());
        appParamMap.put("sign", sign);

        log.info("【待付回调】商户订单号:{}，回调通知操作:商户{}，订单号:{}", order.getOrderId(), order.getSiteId(), order.getId());

        Long timeStamp = System.currentTimeMillis();
        String responseString = null;
        try {
            log.info("【待付回调】商户订单号:{}，回调地址={}", order.getOrderId(), order.getNotifyUrl());
            log.info("【待付回调】商户订单号:{}，回调地址参数={}", order.getOrderId(), appParamMap);

            responseString = HttpUtil.post(order.getNotifyUrl(), appParamMap);
            log.info("【待付回调】商户订单号:{}，回调通知操作：通知成功，第三方回调返回={}", order.getOrderId(), responseString);
            if (!"SUCCESS".equalsIgnoreCase(responseString)) {
                throw new Exception(responseString);
            }
            String jsonObject1 = JSONObject.toJSONString(order);
            redisTemplate.convertAndSend("sendMsgPdai", jsonObject1);
        } catch (Exception e) {
            log.error("【待付回调】商户订单号:{}，回调通知操作：URL={}，回调出错={}", order.getOrderId(), order.getNotifyUrl(), e.getMessage());
            if (order.getNotifyTimes() < 3) {
                order.setNotifyTimes(order.getNotifyTimes() + 1);
                Integer dalayTime = getTimeForNetNotify(order.getNotifyTimes());
                LambdaUpdateWrapper<OrgAccountOrderDaip> updateWrapper = new LambdaUpdateWrapper();
                //0=未通知，1=通知成功，2=通知失败
                updateWrapper.set(OrgAccountOrderDaip::getNotifyTimes, order.getNotifyTimes())
                        .set(OrgAccountOrderDaip::getLastNotifyTime, new Date(System.currentTimeMillis()))
                        .set(OrgAccountOrderDaip::getNextNotifyTime, new Date(timeStamp + dalayTime * 1000))
                        .eq(OrgAccountOrderDaip::getId, order.getId());
                orgAccountOrderDaipService.update(updateWrapper);

                log.info("【待付回调】商户订单号:{}，回调通知操作：重试次数={}，回调错误={}", order.getOrderId(), order.getNotifyTimes(), e.getMessage());
                this.messageProducer.pdaiCallBackOutput(order, dalayTime);
            } else {
                log.info("【待付回调】商户订单号:{}，回调通知操作：通知结束，累计通知回调次数{},通知失败{}，", order.getOrderId(), order.getNotifyTimes(), e.getMessage());
                LambdaUpdateWrapper<OrgAccountOrderDaip> updateWrapper = new LambdaUpdateWrapper();
                updateWrapper.set(OrgAccountOrderDaip::getNotifySucceed, "2")
                        .set(OrgAccountOrderDaip::getLastNotifyTime, new Date(System.currentTimeMillis()))
                        .eq(OrgAccountOrderDaip::getId, order.getId());
                orgAccountOrderDaipService.update(updateWrapper);
            }
            return true;
        }
        LambdaUpdateWrapper<OrgAccountOrderDaip> updateWrapper = new LambdaUpdateWrapper();

        if ("SUCCESS".equalsIgnoreCase(responseString)) {  //返回结果SUCCESS
            updateWrapper.set(OrgAccountOrderDaip::getNotifySucceed, "1");
        } else {
            updateWrapper.set(OrgAccountOrderDaip::getNotifySucceed, "2");
        }
        updateWrapper.set(OrgAccountOrderDaip::getLastNotifyTime, new Date(System.currentTimeMillis()));
        updateWrapper.eq(OrgAccountOrderDaip::getId, order.getId());
        orgAccountOrderDaipService.update(updateWrapper);
        return true;
    }

    /****  通知原则:
     * 第一次通知：距离第一次10秒钟;
     * 第二次通知：距离第二次1分钟;
     * 第三次通知: 距离第三次5分钟;
     ****  构造通知次数与每次通知时间间隔的毫秒数 ****/
    private static Map<Integer, Integer> timeSecondsMap = new HashMap<Integer, Integer>() {{
        put(1, 10);
        put(2, 60);
        put(3, 300);
    }};

    /***
     * @Description 根据当前通知次数查询
     ****/
    public static Integer getTimeForNetNotify(int times) {
        return timeSecondsMap.getOrDefault(times, 0);
    }

}