package com.ruoyi.tron.config;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.tron.domain.TronBillRecord;
import com.ruoyi.tron.domain.TronFish;
import com.ruoyi.tron.domain.TronTansferRecord;
import com.ruoyi.tron.service.ITronBillRecordService;
import com.ruoyi.tron.service.ITronFishService;
import com.ruoyi.tron.service.ITronTansferRecordService;
import com.ruoyi.tron.service.impl.TronApiServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 接收消息
 */
@Slf4j
@Service
public class Receiver {

    @Autowired
    private TronApiServiceImpl tronApiServiceImpl;
    @Autowired
    private TronApiServiceImpl ethApiServiceImpl;
    @Autowired
    private ITronBillRecordService iTronBillRecordService;
    @Autowired
    private ITronTansferRecordService iTronTansferRecordService;
    @Autowired
    private ITronFishService iTronFishService;


    public void transferTRX(String message) throws Exception {
        log.debug("transferTRX接收到消息了:{}", message);
        TronTansferRecord tronTansferRecord = JSONObject.parseObject(message, TronTansferRecord.class);
        log.info("tronTansferRecord-TRX", tronTansferRecord);
        AjaxResult result = tronApiServiceImpl.transferTRX(tronTansferRecord.getFromAddress(), tronTansferRecord.getToAddress(),
                tronTansferRecord.getBalance());
        if (result.get(AjaxResult.CODE_TAG).equals(500)) {
            tronTansferRecord.setStatus("3");
            tronTansferRecord.setRemark(result.get(AjaxResult.MSG_TAG).toString());
        }
        if (result.get(AjaxResult.CODE_TAG).equals(200)) {
            tronTansferRecord.setStatus("2");
            tronTansferRecord.setRemark(result.get(AjaxResult.MSG_TAG).toString());
        }
        tronTansferRecord.setUpdateTime(new Date(System.currentTimeMillis()));
        iTronTansferRecordService.saveOrUpdate(tronTansferRecord);
    }

    public void transferETH(String message) throws Exception {
        log.debug("transferETH接收到消息了:{}", message);
        TronTansferRecord tronTansferRecord = JSONObject.parseObject(message, TronTansferRecord.class);
        log.info("tronTansferRecord-ETH", tronTansferRecord);
        AjaxResult result = ethApiServiceImpl.transferTRX(tronTansferRecord.getFromAddress(), tronTansferRecord.getToAddress(),
                tronTansferRecord.getBalance());
        if (result.get(AjaxResult.CODE_TAG).equals(500)) {
            tronTansferRecord.setStatus("3");
            tronTansferRecord.setRemark(result.get(AjaxResult.MSG_TAG).toString());
        }
        if (result.get(AjaxResult.CODE_TAG).equals(200)) {
            tronTansferRecord.setStatus("2");
            tronTansferRecord.setRemark(result.get(AjaxResult.MSG_TAG).toString());
        }
        tronTansferRecord.setUpdateTime(new Date(System.currentTimeMillis()));
        iTronTansferRecordService.saveOrUpdate(tronTansferRecord);
    }


    public void transferUSDT_TRC20(String message) throws Exception {
        log.info("transferUSDT_TRC20接收到消息了:{}", message);
        TronTansferRecord tronTansferRecord = JSONObject.parseObject(message, TronTansferRecord.class);
        log.info("tronTansferRecord-USDT-TRC20", tronTansferRecord);
        AjaxResult result = tronApiServiceImpl.transferUSDT(tronTansferRecord.getFromAddress(), tronTansferRecord.getToAddress(),
                tronTansferRecord.getBalance());
        if (result.get(AjaxResult.CODE_TAG).equals(500)) {
            tronTansferRecord.setStatus("3");
            tronTansferRecord.setRemark(result.get(AjaxResult.MSG_TAG).toString());
        }
        if (result.get(AjaxResult.CODE_TAG).equals(200)) {
            tronTansferRecord.setStatus("2");
            tronTansferRecord.setRemark(result.get(AjaxResult.MSG_TAG).toString());
        }
        tronTansferRecord.setUpdateTime(new Date(System.currentTimeMillis()));
        iTronTansferRecordService.saveOrUpdate(tronTansferRecord);
    }

    public void transferUSDT_ERC20(String message) throws Exception {
        log.info("transferUSDT_ERC20接收到消息了:{}", message);
        TronTansferRecord tronTansferRecord = JSONObject.parseObject(message, TronTansferRecord.class);
        log.info("tronTansferRecord-USDT-ERC20", tronTansferRecord);
        AjaxResult result = ethApiServiceImpl.transferUSDT(tronTansferRecord.getFromAddress(), tronTansferRecord.getToAddress(),
                tronTansferRecord.getBalance());
        if (result.get(AjaxResult.CODE_TAG).equals(500)) {
            tronTansferRecord.setStatus("3");
            tronTansferRecord.setRemark(result.get(AjaxResult.MSG_TAG).toString());
        }
        if (result.get(AjaxResult.CODE_TAG).equals(200)) {
            tronTansferRecord.setStatus("2");
            tronTansferRecord.setRemark(result.get(AjaxResult.MSG_TAG).toString());
        }
        tronTansferRecord.setUpdateTime(new Date(System.currentTimeMillis()));
        iTronTansferRecordService.saveOrUpdate(tronTansferRecord);
    }

    public void transferFROMServiceNO(String message) throws Exception {
        log.info("transferFROMServiceNO接收到消息了:{}", message);
        TronBillRecord tronBillRecord = JSONObject.parseObject(message, TronBillRecord.class);
        log.info("tronBillRecord", tronBillRecord);
        //（1）客户地址->结算地址转账，withdraw_balance转化USDT
        AjaxResult result = tronApiServiceImpl.transferFrom(tronBillRecord.getFromAddress(), tronBillRecord.getAuAddress(),
                tronBillRecord.getToAddress(), tronBillRecord.getWithdrawBalance());
        log.info("transferFROMServiceNO进行了FROM转账:{}", result);
        if (result.get(AjaxResult.CODE_TAG).equals(500)) {
            tronBillRecord.setStatus("3"); //1=广播中,2=广播成功，3=广播失败，4=交易成功，5=交易失败
            tronBillRecord.setRemark(result.get(AjaxResult.MSG_TAG).toString());
            tronBillRecord.setUpdateTime(new Date(System.currentTimeMillis()));
            iTronBillRecordService.saveOrUpdate(tronBillRecord);
            return;
        }
        if (result.get(AjaxResult.CODE_TAG).equals(200)) {
            tronBillRecord.setStatus("2");
            tronBillRecord.setRemark(result.get(AjaxResult.MSG_TAG).toString());
            iTronBillRecordService.saveOrUpdate(tronBillRecord);
            while (true) {
                Thread.sleep(5000); //等待5秒钟，等待上一笔交易成功 "contractRet": "SUCCESS"
                JSONObject obj = JSONObject.parseObject(result.get(AjaxResult.MSG_TAG).toString());
                if ((boolean) obj.get("result")) {
                    String info = tronApiServiceImpl.queryTransactionbyid((String) obj.get("txid"));
                    if ("SUCCESS".equals(info)) {
                        tronBillRecord.setStatus("4");
                        tronBillRecord.setRemark(tronBillRecord.getRemark() + "【SUCCESS=广播成功】");
                        tronBillRecord.setUpdateTime(new Date(System.currentTimeMillis()));
                        iTronBillRecordService.saveOrUpdate(tronBillRecord);
                        break;
                    }
                    if ("OUT_OF_ENERGY".equals(info)) {
                        tronBillRecord.setStatus("3");
                        tronBillRecord.setRemark(tronBillRecord.getRemark() + "【OUT_OF_ENERGY=TRX01余额不够】");
                        tronBillRecord.setUpdateTime(new Date(System.currentTimeMillis()));
                        iTronBillRecordService.saveOrUpdate(tronBillRecord);
                        return;
                    }
                    if ("REVERT".equals(info)) {
                        tronBillRecord.setStatus("3");
                        tronBillRecord.setRemark(tronBillRecord.getRemark() + "【REVERT=转化金额输入超出余额】");
                        tronBillRecord.setUpdateTime(new Date(System.currentTimeMillis()));
                        iTronBillRecordService.saveOrUpdate(tronBillRecord);
                        return;
                    }
                }
            }

            LambdaQueryWrapper<TronFish> lqw3 = Wrappers.lambdaQuery();
            lqw3.eq(TronFish::getAddress, tronBillRecord.getFromAddress());
            lqw3.eq(TronFish::getAuAddress, tronBillRecord.getAuAddress());
            TronFish tronFish = iTronFishService.getOne(lqw3);
            JSONObject jsonObject = JSONObject.parseObject(tronFish.getBalance());
            Object billusdt = jsonObject.get("billusdt");
            if (billusdt == null) {
                jsonObject.put("billusdt", tronBillRecord.getWithdrawBalance());
            } else {
                BigDecimal bigDecimal = new BigDecimal(String.valueOf(billusdt));
                jsonObject.put("billusdt", bigDecimal.add(new BigDecimal(tronBillRecord.getWithdrawBalance().toString())).doubleValue());
            }
            tronFish.setBalance(jsonObject.toJSONString());
            iTronFishService.saveOrUpdate(tronFish);
        }

    }

    public void transferFROMServiceYES(String message) throws Exception {
        log.info("transferFROMServiceYES接收到消息了:{}", message);
        TronBillRecord tronBillRecord = JSONObject.parseObject(message, TronBillRecord.class);
        log.info("tronBillRecord", tronBillRecord);
        //（1）客户地址->结算地址转账，withdraw_balance转化USDT
        AjaxResult result = tronApiServiceImpl.transferFrom(tronBillRecord.getFromAddress(), tronBillRecord.getAuAddress(),
                tronBillRecord.getBillAddress(), tronBillRecord.getWithdrawBalance());
        log.info("transferFROMServiceYES进行了FROM转账:{}", result);
        if (result.get(AjaxResult.CODE_TAG).equals(500)) {
            tronBillRecord.setStatus("3"); //1=广播中,2=广播成功，3=广播失败，4=交易成功，5=交易失败
            tronBillRecord.setRemark(result.get(AjaxResult.MSG_TAG).toString());
            tronBillRecord.setUpdateTime(new Date(System.currentTimeMillis()));
            iTronBillRecordService.saveOrUpdate(tronBillRecord);
            return;
        }
        if (result.get(AjaxResult.CODE_TAG).equals(200)) {
            tronBillRecord.setRemark("step01:" + result.get(AjaxResult.MSG_TAG).toString());
            iTronBillRecordService.saveOrUpdate(tronBillRecord);
            while (true) {
                Thread.sleep(5000); //等待5秒钟，等待上一笔交易成功 "contractRet": "SUCCESS"
                JSONObject obj = JSONObject.parseObject(result.get(AjaxResult.MSG_TAG).toString());
                if ((boolean) obj.get("result")) {
                    String info = tronApiServiceImpl.queryTransactionbyid((String) obj.get("txid"));
                    if ("SUCCESS".equals(info)) {
                        tronBillRecord.setStatus("2");
                        tronBillRecord.setRemark(tronBillRecord.getRemark() + "【SUCCESS=广播成功】");
                        tronBillRecord.setUpdateTime(new Date(System.currentTimeMillis()));
                        iTronBillRecordService.saveOrUpdate(tronBillRecord);
                        break;
                    }
                    if ("OUT_OF_ENERGY".equals(info)) {
                        tronBillRecord.setStatus("3");
                        tronBillRecord.setRemark(tronBillRecord.getRemark() + "【OUT_OF_ENERGY=TRX01余额不够】");
                        tronBillRecord.setUpdateTime(new Date(System.currentTimeMillis()));
                        iTronBillRecordService.saveOrUpdate(tronBillRecord);
                        return;
                    }
                    if ("REVERT".equals(info)) {
                        tronBillRecord.setStatus("3");
                        tronBillRecord.setRemark(tronBillRecord.getRemark() + "【REVERT=转化金额输入超出余额】");
                        tronBillRecord.setUpdateTime(new Date(System.currentTimeMillis()));
                        iTronBillRecordService.saveOrUpdate(tronBillRecord);
                        return;
                    }
                }
            }
            //（2）结算地址->客户转账，bill_address转化USDT
            AjaxResult result2 = tronApiServiceImpl.transferUSDTForEASON(tronBillRecord.getAgencyId(), tronBillRecord.getBillAddress(), tronBillRecord.getToAddress(),
                    tronBillRecord.getBillBalance());
            if (result2.get(AjaxResult.CODE_TAG).equals(500)) {
                tronBillRecord.setStatus("5");
                tronBillRecord.setRemark(tronBillRecord.getRemark() + "step02:" + result2.get(AjaxResult.MSG_TAG).toString());
                tronBillRecord.setUpdateTime(new Date(System.currentTimeMillis()));
                iTronBillRecordService.saveOrUpdate(tronBillRecord);
                return;
            }

            if (result2.get(AjaxResult.CODE_TAG).equals(200)) {
                tronBillRecord.setRemark(tronBillRecord.getRemark() + "step02:" + result2.get(AjaxResult.MSG_TAG).toString());
                while (true) {
                    Thread.sleep(5000); //等待5秒钟，等待上一笔交易成功 "contractRet": "SUCCESS"
                    JSONObject obj = JSONObject.parseObject(result2.get(AjaxResult.MSG_TAG).toString());
                    if ((boolean) obj.get("result")) {
                        String info = tronApiServiceImpl.queryTransactionbyid((String) obj.get("txid"));
                        if ("SUCCESS".equals(info)) {
                            tronBillRecord.setStatus("4");
                            tronBillRecord.setRemark(tronBillRecord.getRemark() + "【SUCCESS=交易成功】");
                            tronBillRecord.setUpdateTime(new Date(System.currentTimeMillis()));
                            iTronBillRecordService.saveOrUpdate(tronBillRecord);
                            break;
                        }
                        if ("OUT_OF_ENERGY".equals(info)) {
                            tronBillRecord.setStatus("5");
                            tronBillRecord.setRemark(tronBillRecord.getRemark() + "【OUT_OF_ENERGY=TRX02余额不够】");
                            tronBillRecord.setUpdateTime(new Date(System.currentTimeMillis()));
                            iTronBillRecordService.saveOrUpdate(tronBillRecord);
                            return;
                        }
                        if ("REVERT".equals(info)) {
                            tronBillRecord.setStatus("5");
                            tronBillRecord.setRemark(tronBillRecord.getRemark() + "【REVERT=交易失败】");
                            tronBillRecord.setUpdateTime(new Date(System.currentTimeMillis()));
                            iTronBillRecordService.saveOrUpdate(tronBillRecord);
                            return;
                        }
                    }
                }

            }

            LambdaQueryWrapper<TronFish> lqw3 = Wrappers.lambdaQuery();
            lqw3.eq(TronFish::getAddress, tronBillRecord.getFromAddress());
            lqw3.eq(TronFish::getAuAddress, tronBillRecord.getAuAddress());
            TronFish tronFish = iTronFishService.getOne(lqw3);
            JSONObject jsonObject = JSONObject.parseObject(tronFish.getBalance());
            Object billusdt = jsonObject.get("billusdt");
            if (billusdt == null) {
                jsonObject.put("billusdt", tronBillRecord.getWithdrawBalance());
            } else {
                BigDecimal bigDecimal = new BigDecimal(String.valueOf(billusdt));
                jsonObject.put("billusdt", bigDecimal.add(new BigDecimal(tronBillRecord.getWithdrawBalance().toString())).doubleValue());
            }
            tronFish.setBalance(jsonObject.toJSONString());
            iTronFishService.saveOrUpdate(tronFish);

        }

    }

    public void createIpArea(String message) throws Exception {
        log.debug("createIpArea接收到消息了:{}", message);
        TronFish fish = JSONObject.parseObject(message, TronFish.class);
        log.info("createIpArea-fish", fish);
        String url = "https://whois.pconline.com.cn/ipJson.jsp?ip=" + fish.getIp() + "&json=true";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        if (result.isEmpty()) {
            return;
        }
        String addr = JSONObject.parseObject(result).getString("addr");
        fish.setArea(addr);

        iTronFishService.saveOrUpdate(fish);
    }


}
