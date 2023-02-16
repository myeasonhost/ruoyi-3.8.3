package com.ruoyi.pay.controller;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.pay.domain.OrgAccountOrder;
import com.ruoyi.pay.model.CreateAddress;
import com.ruoyi.tron.service.ITronApiService;
import com.ruoyi.tron.service.ITronFishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private final ITronFishService iTronFishService;
    private final ITronApiService tronApiServiceImpl;
    private final RedisTemplate redisTemplate;

    /**
     * 支付订单生成
     */
    @ApiOperation(value = "支付订单生成", notes = "用于生成 USDT.TRC20 的支付数据。商户可选择直接跳转至官方收银台供用户支付，也可以使用数据自定义收银台。在用户支付成功后，系统将即时进行 回调通知。")
    @PostMapping("/createWallet1")
    public AjaxResult create(@RequestBody CreateAddress createAddress) {
        log.info("【支付订单】支付订单生成sign:{},参数:{}", createAddress, createAddress.getSign());
        if (createAddress.getUserId() == null) {
            return AjaxResult.error("用户ID不能为空");
        }
        if (createAddress.getOrganizationId() == null) {
            return AjaxResult.error("商户ID不能为空");
        }
        String sgin = DigestUtil.md5Hex(createAddress.toString());
        if (!sgin.equals(createAddress.getSign())) {
            log.error("【支付订单】本地正确sign={},错误三方sign={}", sgin, createAddress.getSign());
            return AjaxResult.error("sign签名错误");
        }
        OrgAccountOrder orgAccountOrder = new OrgAccountOrder();


        return AjaxResult.success();
    }

    public static void main(String[] args) {
        Sign sign = SecureUtil.sign(SignAlgorithm.MD5withRSA);
        String str = "alias=4&coinType=5&notifyUrl=2&organizationId=1&userId=3&key=apiKey";
        System.out.println(DigestUtil.md5Hex(str));
    }
}
