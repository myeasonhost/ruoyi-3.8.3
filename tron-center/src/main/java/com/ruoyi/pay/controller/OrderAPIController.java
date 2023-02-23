package com.ruoyi.pay.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.pay.domain.OrgAccountAddress;
import com.ruoyi.pay.domain.OrgAccountOrder;
import com.ruoyi.pay.message.MessageProducer;
import com.ruoyi.pay.model.OrderResultModel;
import com.ruoyi.pay.service.IOrgAccountAddressService;
import com.ruoyi.pay.service.IOrgAccountOrderService;
import com.ruoyi.pay.service.ISysConfigService;
import com.ruoyi.pay.utils.Des3;
import com.ruoyi.tron.domain.OrgAccountInfo;
import com.ruoyi.tron.service.IOrgAccountInfoService;
import io.swagger.annotations.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.DecimalMin;
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
@RequestMapping("/api/pay")
public class OrderAPIController extends BaseController {
    private final IOrgAccountInfoService orgAccountInfoService;
    private final IOrgAccountOrderService iOrgAccountOrderService;
    private final ISysConfigService configServiceImpl;
    private final IOrgAccountAddressService iOrgAccountAddressService;
    private final MessageProducer messageProducer;

    /**
     * 支付订单生成
     */
    @ApiOperation(value = "支付订单生成", notes = "用于生成 USDT.TRC20 的支付数据。商户可选择直接跳转至官方收银台供用户支付，也可以使用数据自定义收银台。在用户支付成功后，系统将即时进行 回调通知。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mch_id", value = "商户ID", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "amount", value = "金额", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "currency", value = "订单币种（CNY/USD）", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "coin_code", value = "支付币种（USDT/RMB）", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "notify_url", value = "回调通知地址", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "redirect_url", value = "同步跳转地址", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "order_id", value = "商户订单号", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "customer_id", value = "用户ID", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "product_name", value = "产品名", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "locale", value = "语言", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "signature", value = "签名", dataType = "String", dataTypeClass = String.class)
    })
    @RequestMapping("/create")
    public AjaxResult create(@Validated PayEntity payEntity) {
        log.info("【支付订单】支付订单生成参数:{}", payEntity);
        LambdaQueryWrapper<OrgAccountInfo> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OrgAccountInfo::getAgencyId, payEntity.getMch_id());
        OrgAccountInfo orgAccountInfo = orgAccountInfoService.getOne(lambdaQueryWrapper);
        if (orgAccountInfo == null) {
            return AjaxResult.error("商户mch_id不存在");
        }
        LambdaQueryWrapper<OrgAccountAddress> lambdaQueryWrapper2 = new LambdaQueryWrapper();
        lambdaQueryWrapper2.eq(OrgAccountAddress::getAgencyId, payEntity.getMch_id());
        lambdaQueryWrapper2.eq(OrgAccountAddress::getStatus, "0"); //帐号状态（0正常 1停用）
        List<OrgAccountAddress> listAddress = iOrgAccountAddressService.list(lambdaQueryWrapper2);
        if (listAddress.isEmpty()) {
            return AjaxResult.error("商户收款地址没有配置");
        }
        LambdaQueryWrapper<OrgAccountOrder> lambdaQueryWrapper3 = new LambdaQueryWrapper();
        lambdaQueryWrapper3.eq(OrgAccountOrder::getSiteId, payEntity.getMch_id());
        lambdaQueryWrapper3.eq(OrgAccountOrder::getOrderId, payEntity.getOrder_id());
        OrgAccountOrder orgAccountOrder1 = iOrgAccountOrderService.getOne(lambdaQueryWrapper3);
        if (orgAccountOrder1 != null) {
            return AjaxResult.error("商户order_id已经存在");
        }
        Collections.shuffle(listAddress); //打乱顺序
        OrgAccountAddress orgAccountAddress = listAddress.get(0);
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
            log.error("【支付订单】本地正确sign={},错误三方sign={}", sign, payEntity.getSign());
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
        String coinAmountStr;
        while (true) {
            Double coinAmount = NumberUtil.add(Double.parseDouble(payEntity.getAmount()), RandomUtil.randomDouble(0.01, 0.99));
            coinAmountStr = NumberUtil.roundStr(coinAmount, 2);
            LambdaQueryWrapper<OrgAccountOrder> lambdaQueryWrapper4 = new LambdaQueryWrapper();
            lambdaQueryWrapper4.eq(OrgAccountOrder::getSiteId, payEntity.getMch_id());
            lambdaQueryWrapper4.eq(OrgAccountOrder::getStatus, "1"); //1=支付中,2=支付成功，3=支付超时
            lambdaQueryWrapper4.eq(OrgAccountOrder::getCoinAmount, coinAmountStr);
            lambdaQueryWrapper4.gt(OrgAccountOrder::getExpirationTime, new Date()); //当前时间未过期
            List<OrgAccountOrder> list = this.iOrgAccountOrderService.list(lambdaQueryWrapper4);
            if (list.isEmpty()) {
                break;
            }
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
                orgAccountAddress.getAddress() + "," + orgAccountOrder.getCoinAmount() + "," + orgAccountOrder.getTimeout()+","+orgAccountOrder.getCreateTime().getTime(),
                payEntity.getLocale());
        orgAccountOrder.setCashierUrl(cashierUrl);
        this.iOrgAccountOrderService.save(orgAccountOrder);
        //（4）发送超时消息通知
        messageProducer.payTimeOutput(orgAccountOrder, 60 * timeout); //30分钟超时
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

    public static String responseEncryptResult(String key, String result, String locale) {
        Des3 des3 = new Des3();
        String rndKey = getRndString();
        des3.setKey(rndKey);
        byte[] encoded = des3.encrypt(result.getBytes(StandardCharsets.UTF_8));
        String sMessage = new String(Base64.encodeBase64(encoded));

        long date = (new Date()).getTime();
        String dateString = (new Long(date)).toString();
        des3.setKey(key);
        String pwd = new String(Base64.encodeBase64(des3.encrypt((rndKey + dateString).getBytes())));
        return "http://52.52.144.209:85/?data=" + sMessage + "&key=" + pwd + "&locale=" + locale;
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
            Double d = RandomUtil.randomDouble(0.01, 0.99);
            System.out.println(df.format(d));
        }
    }
}

@Data
@ApiModel(value = "PayEntity", description = "支付订单实体")
class PayEntity {

    @ApiModelProperty("商户id")
    @NotNull(message = "mch_id不能为空")
    private String mch_id;

    @ApiModelProperty("金额")
    @NotNull(message = "amount不能为空")
    @DecimalMin(value = "0", message = "amount必须大于0")
    private String amount;

    @ApiModelProperty("订单币种单位（CNY/USD）")
    private String currency = "USD";

    @ApiModelProperty("支付币种（USDT/RMB）")
    private String coin_code = "USDT";

    @ApiModelProperty("回调通知地址")
    @NotNull(message = "notify_url回调地址不能为空")
    @URL(message = "notify_url回调地址不合法")
    private String notify_url;

    @ApiModelProperty("同步跳转地址")
    @URL
    private String redirect_url;

    @ApiModelProperty("商户订单号")
    @NotNull(message = "order_id不能为空")
    private String order_id;

    @ApiModelProperty("用户ID")
    @NotNull(message = "customer_id不能为空")
    private String customer_id;

    @ApiModelProperty("产品名")
    @NotNull(message = "product_name不能为空")
    private String product_name;

    @ApiModelProperty("语言")
    private String locale = "en-US";

    @ApiModelProperty("签名")
    @NotNull(message = "签名不能为空")
    private String sign;

    public PayEntity() {
    }

    public PayEntity(String mch_id,
                     String amount,
                     String currency,
                     String coin_code,
                     String notify_url,
                     String redirect_url,
                     String order_id,
                     String customer_id,
                     String product_name,
                     String locale,
                     String sign) {
        this.mch_id = mch_id;
        this.amount = amount;
        this.currency = currency;
        this.coin_code = coin_code;
        this.notify_url = notify_url;
        this.redirect_url = redirect_url;
        this.order_id = order_id;
        this.customer_id = customer_id;
        this.product_name = product_name;
        this.locale = locale;
        this.sign = sign;
    }

    @Override
    public String toString() {

        return "PayEntity{" +
                "mch_id='" + mch_id + '\'' +
                ", amount='" + amount + '\'' +
                ", currency='" + currency + '\'' +
                ", coin_code='" + coin_code + '\'' +
                ", notify_url='" + notify_url + '\'' +
                ", redirect_url='" + redirect_url + '\'' +
                ", order_id='" + order_id + '\'' +
                ", customer_id='" + customer_id + '\'' +
                ", product_name='" + product_name + '\'' +
                ", locale='" + locale + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
