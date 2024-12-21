package com.ruoyi.pay.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.pay.domain.OrgAccountAddress;
import com.ruoyi.pay.domain.OrgAccountOrder;
import com.ruoyi.pay.domain.OrgAccountOrderDaip;
import com.ruoyi.pay.dto.PayEntity;
import com.ruoyi.pay.dto.PdaiEntity;
import com.ruoyi.pay.message.MessageProducer;
import com.ruoyi.pay.model.OrderResultModel;
import com.ruoyi.pay.model.OrderStatusModel;
import com.ruoyi.pay.model.PdaiResultModel;
import com.ruoyi.pay.service.IOrgAccountAddressService;
import com.ruoyi.pay.service.IOrgAccountOrderDaipService;
import com.ruoyi.pay.service.IOrgAccountOrderService;
import com.ruoyi.pay.service.ISysConfigService;
import com.ruoyi.pay.utils.Des3;
import com.ruoyi.tron.domain.OrgAccountInfo;
import com.ruoyi.tron.domain.TronAccountAddress;
import com.ruoyi.tron.service.IOrgAccountInfoService;
import com.ruoyi.tron.service.ITronAccountAddressService;
import com.ruoyi.tron.service.ITronApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;

/**
 * API管理Controller
 *
 * @author eason
 * @date 2022-04-20
 */
@Slf4j
@Api(value = "MBPay", tags = {"支付接口"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/api")
public class OrderAPIController extends BaseController {
    private final IOrgAccountInfoService orgAccountInfoService;
    private final IOrgAccountOrderService iOrgAccountOrderService;
    private final IOrgAccountOrderDaipService iOrgAccountOrderDaipService;
    private final ISysConfigService configServiceImpl;
    private final IOrgAccountAddressService iOrgAccountAddressService;
    private final ITronAccountAddressService iTronAccountAddressService;
    private final ITronApiService tronApiServiceImpl;
    private final MessageProducer messageProducer;
    private final RedisTemplate redisTemplate;

    /**
     * 支付订单生成
     */
    @ApiOperation(value = "支付订单生成", notes = "用于生成 USDT.TRC20 的支付数据。商户可选择直接跳转至官方收银台供用户支付，也可以使用数据自定义收银台。在用户支付成功后，系统将即时进行 回调通知。")
    @PostMapping("/pay/create")
    public AjaxResult payCreate(@Validated PayEntity payEntity, BindingResult bindingResult) {
        //（1）基础校验
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldErrors().get(0).getDefaultMessage();
            return AjaxResult.error(errorMsg);
        }
        log.info("【支付订单】支付订单生成参数:{}", payEntity);
        LambdaQueryWrapper<OrgAccountInfo> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OrgAccountInfo::getAgencyId, payEntity.getMch_id());
        OrgAccountInfo orgAccountInfo = orgAccountInfoService.getOne(lambdaQueryWrapper);
        if (orgAccountInfo == null) {
            return AjaxResult.error("商户mch_id不存在");
        }
        LambdaQueryWrapper<OrgAccountOrder> lambdaQueryWrapper1 = new LambdaQueryWrapper();
        lambdaQueryWrapper1.eq(OrgAccountOrder::getSiteId, payEntity.getMch_id());
        lambdaQueryWrapper1.eq(OrgAccountOrder::getOrderId, payEntity.getOrder_id());
        OrgAccountOrder orgAccountOrder1 = iOrgAccountOrderService.getOne(lambdaQueryWrapper1);
        if (orgAccountOrder1 != null) {
            return AjaxResult.error("商户order_id已经存在");
        }
        LambdaQueryWrapper<OrgAccountAddress> lambdaQueryWrapper2 = new LambdaQueryWrapper();
        lambdaQueryWrapper2.eq(OrgAccountAddress::getAgencyId, payEntity.getMch_id());
        lambdaQueryWrapper2.eq(OrgAccountAddress::getStatus, "0"); //帐号状态（0正常 1停用）
        List<OrgAccountAddress> listAddress = iOrgAccountAddressService.list(lambdaQueryWrapper2);
        if (listAddress.isEmpty()) {
            return AjaxResult.error("商户收款地址没有配置");
        }
        Collections.shuffle(listAddress); //打乱顺序
        OrgAccountAddress orgAccountAddress = listAddress.get(0);

        LambdaQueryWrapper<OrgAccountOrder> lambdaQueryWrapper3 = new LambdaQueryWrapper();
        lambdaQueryWrapper3.eq(OrgAccountOrder::getSiteId, payEntity.getMch_id());
        lambdaQueryWrapper3.eq(OrgAccountOrder::getUserId, payEntity.getCustomer_id());
        lambdaQueryWrapper3.eq(OrgAccountOrder::getStatus, "1"); //1=支付中,2=支付成功，3=支付超时
        lambdaQueryWrapper3.eq(OrgAccountOrder::getCoinAddress, orgAccountAddress.getAddress());
        lambdaQueryWrapper3.eq(OrgAccountOrder::getCoinAmount, NumberUtil.roundStr(payEntity.getAmount(), 2));
        lambdaQueryWrapper3.gt(OrgAccountOrder::getExpirationTime, new Date()); //当前时间未过期
        OrgAccountOrder orgAccountOrder2 = iOrgAccountOrderService.getOne(lambdaQueryWrapper3);

        if (orgAccountOrder2 != null) {
            orgAccountOrder2.setStatus("3");
            orgAccountOrder2.setRemark("当前会员金额重复充值，订单失效");
            iOrgAccountOrderService.update(orgAccountOrder2, lambdaQueryWrapper3);
        }

        //（1）sign签名验证与校验
        Map<String, Object> treeMap = new TreeMap<>(BeanUtil.beanToMap(payEntity));
        treeMap.remove("sign");
        StringBuffer orgin = new StringBuffer();
        Iterator iter = treeMap.keySet().iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            orgin.append("&").append(name).append("=").append(treeMap.get(name));
        }
        orgin.append("&").append("key").append("=").append(orgAccountInfo.getPrivateKey());
        orgin.deleteCharAt(0);
        log.info("【支付订单】sign加密串{}", orgin);
        String sign = DigestUtil.md5Hex(orgin.toString());
        log.info("【支付订单】sign生成={}", sign);
        if (!sign.equals(payEntity.getSign())) {
            log.info("【支付订单】本地正确sign={},错误三方sign={}", sign, payEntity.getSign());
            return AjaxResult.error("sign签名错误");
        }
        //（2）生成订单
        OrgAccountOrder orgAccountOrder = new OrgAccountOrder();
        String payId = "MB" + IdUtil.getSnowflakeNextIdStr();
        orgAccountOrder.setId(payId);
        orgAccountOrder.setSiteId(payEntity.getMch_id());
        orgAccountOrder.setOrderId(payEntity.getOrder_id());
        orgAccountOrder.setUserId(payEntity.getCustomer_id());
        orgAccountOrder.setProductName(payEntity.getProduct_name());
        orgAccountOrder.setAmount(payEntity.getAmount()); //订单金额
        orgAccountOrder.setCurrency(payEntity.getCurrency());
        //（3）生成支付金额
        String coinAmountStr = null;
        Double rate = configServiceImpl.getPayRate(); //生成金额的汇率小数

        while (true) {
            if (coinAmountStr == null) {
                coinAmountStr = NumberUtil.roundStr(payEntity.getAmount(), 2);
            }
            LambdaQueryWrapper<OrgAccountOrder> lambdaQueryWrapper4 = new LambdaQueryWrapper();
            lambdaQueryWrapper4.eq(OrgAccountOrder::getSiteId, payEntity.getMch_id());
            lambdaQueryWrapper4.eq(OrgAccountOrder::getStatus, "1"); //1=支付中,2=支付成功，3=支付超时
            lambdaQueryWrapper4.eq(OrgAccountOrder::getCoinAddress, orgAccountAddress.getAddress());
            lambdaQueryWrapper4.eq(OrgAccountOrder::getCoinAmount, coinAmountStr);
            lambdaQueryWrapper4.gt(OrgAccountOrder::getExpirationTime, new Date()); //当前时间未过期
            List<OrgAccountOrder> list = this.iOrgAccountOrderService.list(lambdaQueryWrapper4);
            if (list.isEmpty()) {
                break;
            }
            Double coinAmount = NumberUtil.sub(Double.parseDouble(payEntity.getAmount()), RandomUtil.randomDouble(0.01, rate));
            coinAmountStr = NumberUtil.roundStr(coinAmount, 2);
            orgAccountOrder.setRemark("与其他会员金额重复，费率减少");

        }

        orgAccountOrder.setCoinAmount(coinAmountStr);//支付金额,舍弃方式采用四舍五入
        orgAccountOrder.setCoinCode(payEntity.getCoin_code());
        orgAccountOrder.setCoinAddress(orgAccountAddress.getAddress());
        orgAccountOrder.setStatus("1"); //1=支付中,2=支付成功，3=支付超时
        orgAccountOrder.setNotifyUrl(payEntity.getNotify_url());
        orgAccountOrder.setRedirectUrl(payEntity.getRedirect_url());
        Integer timeout = configServiceImpl.getPayTimeOut();
        orgAccountOrder.setTimeout(timeout.toString());
        orgAccountOrder.setExpirationTime(DateUtil.offsetMinute(orgAccountOrder.getCreateTime(), timeout));
        orgAccountOrder.setCreateTime(new Date(System.currentTimeMillis()));
        String cashierUrl = responseEncryptResult("fT6phq0wkOPRlAoyToidAnkogUV7ttGo",
                orgAccountAddress.getAddress() + "," + orgAccountOrder.getCoinAmount() + "," + orgAccountOrder.getTimeout() +
                        "," + orgAccountOrder.getCreateTime().getTime() + "," + orgAccountOrder.getOrderId() + "," + orgAccountOrder.getSiteId(),
                payEntity.getLocale());
        orgAccountOrder.setCashierUrl(cashierUrl);
        this.iOrgAccountOrderService.save(orgAccountOrder);
        //（4）发送超时消息通知
        messageProducer.payTimeOutput(orgAccountOrder, 60 * timeout); //30分钟超时
        String jsonObject = JSONObject.toJSONString(orgAccountOrder);
        redisTemplate.convertAndSend("sendMsgPay", jsonObject);

        //（5）生成返回信息
        OrderResultModel resultModel = new OrderResultModel();
        resultModel.setAmount(orgAccountOrder.getAmount());
        resultModel.setCurrency(orgAccountOrder.getCurrency());
        resultModel.setCoin_code(orgAccountOrder.getCoinCode());
        resultModel.setCoin_amount(orgAccountOrder.getCoinAmount());
        resultModel.setCoin_address(orgAccountAddress.getAddress());
        resultModel.setCashier_url(cashierUrl);
        resultModel.setTimeout(orgAccountOrder.getTimeout());
        return AjaxResult.success(resultModel);
    }

    /**
     * 支付订单状态查询
     */
    @ApiOperation(value = "支付订单状态查询", notes = "用于生成 USDT.TRC20 的支付数据。商户可选择直接跳转至官方收银台供用户支付，也可以使用数据自定义收银台。在用户支付成功后，前端收银页面订单刷新订单状态。")
    @GetMapping("/pay/order/queryStatus")
    public AjaxResult payCreate(@RequestParam("mch_id") @NotNull(message = "mch_id不能为空") String mch_id,
                                @RequestParam("order_id") @NotNull(message = "order_id不能为空") String order_id) {
        LambdaQueryWrapper<OrgAccountInfo> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OrgAccountInfo::getAgencyId, mch_id);
        OrgAccountInfo orgAccountInfo = orgAccountInfoService.getOne(lambdaQueryWrapper);
        if (orgAccountInfo == null) {
            return AjaxResult.error("商户mch_id不存在");
        }
        LambdaQueryWrapper<OrgAccountOrder> lambdaQueryWrapper3 = new LambdaQueryWrapper();
        lambdaQueryWrapper3.eq(OrgAccountOrder::getSiteId, mch_id);
        lambdaQueryWrapper3.eq(OrgAccountOrder::getOrderId, order_id);
        OrgAccountOrder orgAccountOrder = iOrgAccountOrderService.getOne(lambdaQueryWrapper3);
        if (orgAccountOrder == null) {
            return AjaxResult.error("商户order_id不存在");
        }
        OrderStatusModel resultModel = new OrderStatusModel();
        resultModel.setMchId(orgAccountOrder.getSiteId());
        resultModel.setOrderId(orgAccountOrder.getOrderId());
        resultModel.setStatus(orgAccountOrder.getStatus());
        resultModel.setNotify_succeed(orgAccountOrder.getNotifySucceed());
        resultModel.setAmount(orgAccountOrder.getAmount());
        resultModel.setCoin_amount(orgAccountOrder.getCoinAmount());
        return AjaxResult.success(resultModel);
    }

    /**
     * 代付订单生成
     */
    @ApiOperation(value = "代付订单生成", notes = "用于生成 USDT.TRC20 的代付数据。商户发起代付的申请之后，系统将即时进行为指定账户付款。")
    @PostMapping("/pdai/create")
    public AjaxResult pdaiCreate(@Validated PdaiEntity payEntity, BindingResult bindingResult) {
        //（1）基础校验
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldErrors().get(0).getDefaultMessage();
            return AjaxResult.error(errorMsg);
        }
        log.info("【代付订单】代付订单生成参数:{}", payEntity);
        LambdaQueryWrapper<OrgAccountInfo> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OrgAccountInfo::getAgencyId, payEntity.getMch_id());
        OrgAccountInfo orgAccountInfo = orgAccountInfoService.getOne(lambdaQueryWrapper);
        if (orgAccountInfo == null) {
            return AjaxResult.error("商户mch_id不存在");
        }
        String address = payEntity.getAddress();
        if (!this.tronApiServiceImpl.validateAddress(address)) {
            return AjaxResult.error("收款地址不合法");
        }

        LambdaQueryWrapper<TronAccountAddress> lambdaQueryWrapper2 = new LambdaQueryWrapper();
        lambdaQueryWrapper2.eq(TronAccountAddress::getAgencyId, payEntity.getMch_id());
        lambdaQueryWrapper2.eq(TronAccountAddress::getStatus, "0"); //帐号状态（0正常 1停用）
        List<TronAccountAddress> listAddress = iTronAccountAddressService.list(lambdaQueryWrapper2);
        if (listAddress.isEmpty()) {
            return AjaxResult.error("商户出款地址没有配置");
        }
        LambdaQueryWrapper<OrgAccountOrderDaip> lambdaQueryWrapper3 = new LambdaQueryWrapper();
        lambdaQueryWrapper3.eq(OrgAccountOrderDaip::getSiteId, payEntity.getMch_id());
        lambdaQueryWrapper3.eq(OrgAccountOrderDaip::getOrderId, payEntity.getOrder_id());
        OrgAccountOrderDaip orgAccountOrderDaip1 = iOrgAccountOrderDaipService.getOne(lambdaQueryWrapper3);
        if (orgAccountOrderDaip1 != null) {
            return AjaxResult.error("商户order_id已经存在");
        }
        Collections.shuffle(listAddress); //打乱顺序
        TronAccountAddress orgAccountAddress = listAddress.get(0);
        //（1）sign签名验证与校验
        Map<String, Object> treeMap = new TreeMap<>(BeanUtil.beanToMap(payEntity));
        treeMap.remove("sign");
        StringBuffer orgin = new StringBuffer();
        Iterator iter = treeMap.keySet().iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            orgin.append("&").append(name).append("=").append(treeMap.get(name));
        }
        orgin.append("&").append("key").append("=").append(orgAccountInfo.getPrivateKey());
        orgin.deleteCharAt(0);
        log.info("【支付订单】sign加密串{}", orgin);
        String sign = DigestUtil.md5Hex(orgin.toString());
        log.info("【支付订单】sign生成={}", sign);
        if (!sign.equals(payEntity.getSign())) {
            log.info("【支付订单】本地正确sign={},错误三方sign={}", sign, payEntity.getSign());
            return AjaxResult.error("sign签名错误");
        }
        //（2）生成订单
        OrgAccountOrderDaip orgAccountOrder = new OrgAccountOrderDaip();
        String payId = "DF" + IdUtil.getSnowflakeNextIdStr();
        orgAccountOrder.setId(payId);
        orgAccountOrder.setSiteId(payEntity.getMch_id());
        orgAccountOrder.setOrderId(payEntity.getOrder_id());
        orgAccountOrder.setUserId(payEntity.getCustomer_id());
        orgAccountOrder.setProductName(payEntity.getProduct_name());
        orgAccountOrder.setAmount(payEntity.getAmount()); //订单金额
        orgAccountOrder.setCurrency(payEntity.getCurrency());
        orgAccountOrder.setCoinAmount(orgAccountOrder.getAmount());//支付金额(默认订单金额，暂时不加汇率换算)
        orgAccountOrder.setCoinCode(payEntity.getCoin_code());
        orgAccountOrder.setOutAddress(orgAccountAddress.getAddress());
        orgAccountOrder.setCoinAddress(payEntity.getAddress());
        Integer limit = configServiceImpl.getDaipLimit();
        if (NumberUtil.compare(new Double(orgAccountOrder.getCoinAmount()), limit) >= 0) {
            orgAccountOrder.setStatus("0"); //0=需要审批,2=提现成功，3=提现失败
        } else {
            orgAccountOrder.setStatus("1"); //1=提现中,2=提现成功，3=提现失败
        }
        orgAccountOrder.setNotifyUrl(payEntity.getNotify_url());
        orgAccountOrder.setCreateTime(new Date(System.currentTimeMillis()));

        this.iOrgAccountOrderDaipService.save(orgAccountOrder);
        //（4）发送提现消息通知
        messageProducer.pdaiOutput(orgAccountOrder); //进行转账操作
        String jsonObject = JSONObject.toJSONString(orgAccountOrder);
        redisTemplate.convertAndSend("sendMsgPdai", jsonObject);

        //（5）生成返回信息
        PdaiResultModel resultModel = new PdaiResultModel();
        resultModel.setAmount(orgAccountOrder.getAmount());
        resultModel.setCurrency(orgAccountOrder.getCurrency());
        resultModel.setCoin_code(orgAccountOrder.getCoinCode());
        resultModel.setCoin_amount(orgAccountOrder.getCoinAmount());
        resultModel.setCoin_address(payEntity.getAddress());
        resultModel.setOut_address(orgAccountAddress.getAddress());
        return AjaxResult.success(resultModel);
    }

    public String responseEncryptResult(String key, String result, String locale) {
        Des3 des3 = new Des3();
        String rndKey = getRndString();
        des3.setKey(rndKey);
        byte[] encoded = des3.encrypt(result.getBytes(StandardCharsets.UTF_8));
        String sMessage = new String(Base64.encodeBase64(encoded));

        long date = (new Date()).getTime();
        String dateString = (new Long(date)).toString();
        des3.setKey(key);
        String pwd = new String(Base64.encodeBase64(des3.encrypt((rndKey + dateString).getBytes())));

        return configServiceImpl.getCashierUrl() + "?data=" + sMessage + "&key=" + pwd + "&locale=" + locale;
    }

    private static String getRndString() {
        String sStr = "0123456789abcdefghijklmnopqrstuvwxyz";
        String rStr = "";
        Random rn = new Random();
        for (int j = 0; j < 8; ++j) {
            int rNum = rn.nextInt(36);
            rStr = rStr + sStr.charAt(rNum);
        }
        return rStr;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 90; i++) {
            DecimalFormat df = new DecimalFormat("0.00");
            Double d = RandomUtil.randomDouble(0.01, 0.5);
            System.out.println(df.format(d));
        }
    }
}

