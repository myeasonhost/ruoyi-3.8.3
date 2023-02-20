package com.ruoyi.pay.task;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ruoyi.pay.domain.OrgAccountOrder;
import com.ruoyi.pay.message.MessageProducer;
import com.ruoyi.pay.service.IOrgAccountOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 定时任务调度测试
 *
 * @author ruoyi
 */
@Slf4j
@Component("tronTask")
public class TronTask {

    @Autowired
    private IOrgAccountOrderService iOrgAccountOrderService;
    @Autowired
    private MessageProducer messageProducer;

    /**
     * 获取账户历史合约交易记录
     * GET
     * https://api.shasta.trongrid.io/v1/accounts/{address}/transactions/trc20
     * 获取一个账户的历史TRC20、TRC721转账记录及授权记录
     * <p>
     * "success": true,
     * "meta": {
     * "at": 1676721885959,
     * "fingerprint": "9mPzvhwtq8uTG3KkJa6hTeawdSUn7rFe4HYN6jrXrSHXkRk8Rputu7eP6qw6XFkVgqppwh1vbprMifbyoqUCxFvcUcFuJKdND",
     * "links": {
     * "next": "https://api.trongrid.io/v1/accounts/TW26XxLR9Tme8pcBoh9DVPTbvAR5XUMpoF/transactions/trc20?limit=100&only_confirmed11=true&only_to=true&fingerprint=9mPzvhwtq8uTG3KkJa6hTeawdSUn7rFe4HYN6jrXrSHXkRk8Rputu7eP6qw6XFkVgqppwh1vbprMifbyoqUCxFvcUcFuJKdND"
     * },
     * "page_size": 20
     * }
     *
     * @param params
     */
    public void queryTRC20(String params) {
        log.info("【订单扫块】扫块任务开始执行，参数=" + params);
        //（1）查找支付订单中，未过期的订单，最早的时间
        LambdaQueryWrapper<OrgAccountOrder> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OrgAccountOrder::getStatus, "1"); //1=支付中,2=支付成功，3=支付超时
        lambdaQueryWrapper.gt(OrgAccountOrder::getExpirationTime, new Date()); //当前时间未过期
        lambdaQueryWrapper.orderByAsc(OrgAccountOrder::getExpirationTime);
        List<OrgAccountOrder> list = this.iOrgAccountOrderService.list(lambdaQueryWrapper);
        if (list.isEmpty()) {
            return;
        }
        //（2）扫描订单支付记录
        list.forEach(orgAccountOrder -> {
            String url = String.format("https://api.trongrid.io/v1/accounts/%s/transactions/trc20?only_to=true&only_confirmed=true" +
                    "&min_timestamp=%s" +
                    "&max_timestamp=%s" +
                    "&limit=%s", orgAccountOrder.getCoinAddress(), orgAccountOrder.getCreateTime().getTime(), orgAccountOrder.getExpirationTime().getTime(), 100);
            log.info("【订单扫块】扫描订单接口={}", url);
            String result = HttpUtil.get(url);
            log.info("【订单扫块】扫描订单返回结果result={}", result);
            JSONObject jsonObject = JSONUtil.parseObj(result);
            JSONArray jsonData; //支付扫描记录数据

            String amountTarget = NumberUtil.mul(orgAccountOrder.getCoinAmount(), "1000000").toString();

            //（3）分页-扫描订单支付记录
            while (true) {
                jsonData = jsonObject.getJSONArray("data");
                if (jsonData == null) {
                    return;
                }
                Optional optional = jsonData.stream()
                        .filter(item -> ((JSONObject) item).getStr("value").equals(amountTarget)
                                && ((JSONObject) item).getStr("type").equals("Transfer"))  //type有可能是Transfer或者Approval
                        .findFirst();
                if (optional.isPresent()) {
                    JSONObject jsonTarget = (JSONObject) optional.get();
                    log.info("【订单扫块】匹配到支付金额coin_amount={},订单ID={},目标记录jsonTarget={}", orgAccountOrder.getCoinAmount(), orgAccountOrder.getOrderId(), jsonTarget);
                    //（4）扫描到波场交易记录，记录transactionId，发送收款回调通知
                    LambdaUpdateWrapper<OrgAccountOrder> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(OrgAccountOrder::getId, orgAccountOrder.getId())
                            .set(OrgAccountOrder::getStatus, "2") //1=支付中,2=支付成功，3=支付超时
                            .set(OrgAccountOrder::getPayTime, jsonTarget.getDate("block_timestamp"))
                            .set(OrgAccountOrder::getTransactionId, jsonTarget.getStr("transaction_id"));
                    this.iOrgAccountOrderService.update(lambdaUpdateWrapper);
                    //发送回调消息
                    messageProducer.callBackOutput(orgAccountOrder, 0);
                    return;
                }
                if (jsonObject.getJSONObject("meta").get("fingerprint") == null) {
                    break;
                }
                String url2 = jsonObject.getJSONObject("meta").getJSONObject("links").getStr("next");
                String result2 = HttpUtil.get(url2);
                log.info("【订单扫块】扫描订单返回结果result={}", result2);
                jsonObject = JSONUtil.parseObj(result2); //重新覆盖jsonObject
            }

        });
    }

}
