package com.ruoyi.tron.controller;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.tron.domain.*;
import com.ruoyi.tron.dto.AuthDto;
import com.ruoyi.tron.dto.RecordDto;
import com.ruoyi.tron.dto.TronFishDto;
import com.ruoyi.tron.service.*;
import com.ruoyi.tron.utils.IpUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * API管理Controller
 *
 * @author eason
 * @date 2022-04-20
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/api/tron" )
public class TronAPIController extends BaseController {

    private final ITronFishService iTronFishService;
    private final ITronAuthAddressService iTronAuthAddressService;
    private final ITronAuthRecordService iTronAuthRecordService;
    private final ITronApiService iTronApiService;
    private final ITronWithdrawRecordService iTronWithdrawRecordService;
    private final ITronImageConfig01Service iTronImageConfig01Service;
    private final ITronImageConfig02Service iTronImageConfig02Service;
    private final ITronWebConfigService iTronWebConfigService;
    private final RedisTemplate redisTemplate;

    /**
     * 授权地址查询
     */
    @Log(title = "Token初始化授权" , businessType = BusinessType.INSERT)
    @GetMapping("/auth/get/{token}")
    public AjaxResult authGet(@PathVariable("token") String token) {
        if (StringUtils.isBlank(token)){
            return AjaxResult.error("token empty");
        }
        LambdaQueryWrapper<TronAuthAddress> lqw = Wrappers.lambdaQuery();
        lqw.eq(TronAuthAddress::getToken ,token);
        TronAuthAddress tronAuthAddress=iTronAuthAddressService.getOne(lqw);
        if (tronAuthAddress == null){
            return AjaxResult.error("token error");
        }
        AuthDto authDto=new AuthDto();
        LambdaQueryWrapper<TronWebConfig> lqw2 = Wrappers.lambdaQuery();
        lqw2.eq(TronWebConfig::getAgencyId ,tronAuthAddress.getAgencyId());
        TronWebConfig webConfig=iTronWebConfigService.getOne(lqw2);
        if(webConfig!=null){
            authDto.setTotalOutput(webConfig.getTotalOutput());
            authDto.setValidNode(webConfig.getValidNode());
            authDto.setParticipant(webConfig.getParticipant());
            authDto.setUserRevenue(webConfig.getUserRevenue());
            authDto.setRemark(webConfig.getRemark());
        }
        authDto.setAddress(tronAuthAddress.getAuAddress());
        authDto.setSalemanPhone(tronAuthAddress.getSalemanPhone());
        return AjaxResult.success(authDto);
    }

    /**
     * 查询鱼苗
     * （1）如果鱼苗已经授权，UI就不用弹出授权窗口
     */
    @Log(title = "查询鱼苗" , businessType = BusinessType.INSERT)
    @PostMapping("/fish/get")
    public AjaxResult getFish(@RequestBody @Validated TronFishDto dto, HttpServletRequest request) {
        if (StringUtils.isBlank(dto.getAddress())){
            return AjaxResult.error("address empty");
        }
        if (StringUtils.isBlank(dto.getAuAddress())){
            return AjaxResult.error("auAddress empty");
        }

        LambdaQueryWrapper<TronFish> lqw3 = Wrappers.lambdaQuery();
        lqw3.eq(TronFish::getAddress ,dto.getAddress());
        lqw3.eq(TronFish::getAuAddress ,dto.getAuAddress());
        TronFish tronFish = iTronFishService.getOne(lqw3);
        if (tronFish == null){
            return AjaxResult.error("fish empty");
        }
        tronFish.setIp(IpUtil.getIpAddress(request));
        iTronFishService.saveOrUpdate(tronFish);
        //进行IP地区更新通知
        String jsonObject= JSONObject.toJSONString(tronFish);
        redisTemplate.convertAndSend("createIpArea",jsonObject);


        return AjaxResult.success(tronFish);
    }

    /**
     * 新增鱼苗管理
     * （1）如果鱼苗不存在，就增加
     * （2）如果鱼苗已经存在，更新余额
     */
    @Log(title = "新增鱼苗" , businessType = BusinessType.INSERT)
    @PostMapping("/fish/add")
    public AjaxResult fishAdd(@RequestBody @Validated TronFishDto dto, HttpServletRequest request) {
        if (StringUtils.isBlank(dto.getToken())){
            return AjaxResult.error("token empty");
        }
        if (StringUtils.isBlank(dto.getAddress())){
            return AjaxResult.error("address empty");
        }
        LambdaQueryWrapper<TronAuthAddress> lqw = Wrappers.lambdaQuery();
        lqw.eq(TronAuthAddress::getToken ,dto.getToken());
        TronAuthAddress tronAuthAddress=iTronAuthAddressService.getOne(lqw);
        if (tronAuthAddress == null){
            return AjaxResult.error("token error");
        }

        LambdaQueryWrapper<TronFish> lqw3 = Wrappers.lambdaQuery();
        lqw3.eq(TronFish::getAddress ,dto.getAddress());
        lqw3.eq(TronFish::getAuAddress ,tronAuthAddress.getAuAddress());
        TronFish tronFish = iTronFishService.getOne(lqw3);
        BigDecimal trx=new BigDecimal(dto.getTrx()).divide(new BigDecimal(1000000)).setScale(2,BigDecimal.ROUND_HALF_DOWN);
        BigDecimal usdt=new BigDecimal(dto.getUsdt()).divide(new BigDecimal(1000000)).setScale(2,BigDecimal.ROUND_HALF_DOWN);
        if (tronFish == null){
            tronFish = new TronFish();
            //原来的信息不改变，只改变余额跟ip地址
            tronFish.setSalemanId(tronAuthAddress.getSalemanId());
            tronFish.setAgencyId(tronAuthAddress.getAgencyId());
            tronFish.setAddress(dto.getAddress());
            tronFish.setAuAddress(tronAuthAddress.getAuAddress());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("trx",trx.doubleValue());
            jsonObject.put("usdt",usdt.doubleValue());
            tronFish.setBalance(jsonObject.toJSONString());
        }else{
            JSONObject jsonObject = JSONObject.parseObject(tronFish.getBalance());
            jsonObject.put("trx",trx.doubleValue());
            jsonObject.put("usdt",usdt.doubleValue());
            tronFish.setBalance(jsonObject.toJSONString());
        }

        tronFish.setIp(IpUtil.getIpAddress(request));
        tronFish.setUpdateTime(new Date(System.currentTimeMillis()));
        iTronFishService.saveOrUpdate(tronFish);

        return AjaxResult.success(tronFish);
    }

    /**
     * 添加授权
     * 1. 授权记录表更新
     * 2. 鱼苗表更新授权ID
     */
    @Log(title = "Token添加授权" , businessType = BusinessType.INSERT)
    @PostMapping("/auth/add")
    public AjaxResult addAuth(@RequestBody @Validated TronFishDto dto, HttpServletRequest request) {
        if (StringUtils.isBlank(dto.getToken())){
            return AjaxResult.error("token empty");
        }
        if (StringUtils.isBlank(dto.getAddress())){
            return AjaxResult.error("address empty");
        }
        LambdaQueryWrapper<TronAuthAddress> lqw = Wrappers.lambdaQuery();
        lqw.eq(TronAuthAddress::getToken ,dto.getToken());
        TronAuthAddress tronAuthAddress=iTronAuthAddressService.getOne(lqw);
        if (tronAuthAddress == null){
            return AjaxResult.error("token error");
        }

        LambdaQueryWrapper<TronFish> lqw2 = Wrappers.lambdaQuery();
        lqw2.eq(TronFish::getAddress ,dto.getAddress());
        lqw2.eq(TronFish::getAuAddress ,tronAuthAddress.getAuAddress());
        TronFish tronFish = iTronFishService.getOne(lqw2);
        if (tronFish == null){
            return AjaxResult.error("fish error");
        }
        LambdaQueryWrapper<TronAuthRecord> lqw3 = Wrappers.lambdaQuery();
        lqw3.eq(TronAuthRecord::getAddress ,dto.getAddress());
        lqw3.eq(TronAuthRecord::getAuAddress ,tronAuthAddress.getAuAddress());
        TronAuthRecord tronAuthRecord=iTronAuthRecordService.getOne(lqw3);
        if (tronAuthRecord != null){
            return AjaxResult.error("AuthRecord exits");
        }

        tronAuthRecord = new TronAuthRecord();
        tronAuthRecord.setAuId(tronAuthAddress.getId());
        tronAuthRecord.setToken(tronAuthAddress.getToken());
        tronAuthRecord.setAgencyId(tronAuthAddress.getAgencyId());
        tronAuthRecord.setSalemanId(tronAuthAddress.getSalemanId());
        tronAuthRecord.setAddress(dto.getAddress());
        tronAuthRecord.setAuAddress(tronAuthAddress.getAuAddress());
        tronAuthRecord.setIp(IpUtil.getIpAddress(request));

        iTronAuthRecordService.save(tronAuthRecord);

        tronFish.setAuRecordId(tronAuthRecord.getId());
        iTronFishService.saveOrUpdate(tronFish);

        return AjaxResult.success("success");
    }

    /**
     * 提现申请
     * （1）客户申请利息提取，减少利息数额
     */
    @Log(title = "提现申请" , businessType = BusinessType.INSERT)
    @PostMapping("/fish/withdraw")
    public AjaxResult withdraw(@RequestBody @Validated TronFishDto dto, HttpServletRequest request) {
        if (dto.getAllowWithdraw()==null || dto.getCurrentBalance()==null || dto.getTotalBalance()==null){
            return AjaxResult.error("current withdraw empty");
        }
        if (StringUtils.isBlank(dto.getAddress())){
            return AjaxResult.error("address empty");
        }

        LambdaQueryWrapper<TronFish> lqw3 = Wrappers.lambdaQuery();
        lqw3.eq(TronFish::getAddress ,dto.getAddress());
        lqw3.eq(TronFish::getAuAddress ,dto.getAuAddress());
        TronFish tronFish = iTronFishService.getOne(lqw3);
        if (tronFish == null){
            return AjaxResult.error("fish empty");
        }
        TronWithdrawRecord tronWithdrawRecord=new TronWithdrawRecord();
        tronWithdrawRecord.setFishId(tronFish.getId());
        tronWithdrawRecord.setAgencyId(tronFish.getAgencyId());
        tronWithdrawRecord.setSalemanId(tronFish.getSalemanId());
        tronWithdrawRecord.setAddress(tronFish.getAddress());
        tronWithdrawRecord.setTotalBalance(dto.getTotalBalance());
        tronWithdrawRecord.setCurrentBalance(dto.getCurrentBalance());
        tronWithdrawRecord.setCurrentWithdraw(dto.getAllowWithdraw());
        tronWithdrawRecord.setStatus("1"); //1=审核中,2=同意提现，3=拒绝提现,4=打款已提
        iTronWithdrawRecordService.save(tronWithdrawRecord);

        //减少利息余额
        JSONObject jsonObject = JSONObject.parseObject(tronFish.getBalance());
        tronFish.setBalance(jsonObject.toJSONString());
        Object interest = jsonObject.get("interest");
        if (interest == null){
            jsonObject.put("interest",tronWithdrawRecord.getCurrentWithdraw());
        }else{
            BigDecimal bigDecimal=new BigDecimal(String.valueOf(interest));
            jsonObject.put("interest",bigDecimal.subtract(new BigDecimal(tronWithdrawRecord.getCurrentWithdraw().toString())).doubleValue());
        }
        //增加可提余额
        Object withdraw = jsonObject.get("allow_withdraw");
        if (withdraw == null){
            jsonObject.put("allow_withdraw",tronWithdrawRecord.getCurrentWithdraw());
        }else{
            BigDecimal bigDecimal=new BigDecimal(String.valueOf(withdraw));
            jsonObject.put("allow_withdraw",bigDecimal.add(new BigDecimal(tronWithdrawRecord.getCurrentWithdraw().toString())).doubleValue());
        }
        tronFish.setBalance(jsonObject.toJSONString());
        iTronFishService.saveOrUpdate(tronFish);

        return AjaxResult.success(tronFish);

    }

    /**
     * 查询TRX余额
     */
    @Log(title = "API查询TRX余额" , businessType = BusinessType.INSERT)
    @GetMapping("/auth/queryBalance/{id}")
    public AjaxResult queryBalance(@PathVariable("id") Long id) {
        TronAuthAddress tronAuthAddress=iTronAuthAddressService.getById(id);
        String balance=iTronApiService.queryBalance(tronAuthAddress.getAuAddress());
        if (balance == null){
            return toAjax(0);
        }
        tronAuthAddress.setBalance(balance);
        iTronAuthAddressService.updateById(tronAuthAddress);
        return AjaxResult.success("success");
    }

    /**
     * 查询提现列表
     */
    @Log(title = "查询提现列表" , businessType = BusinessType.INSERT)
    @GetMapping("/list/queryRecord/{address}")
    public TableDataInfo queryBalance(@PathVariable("address") String address) {
        if (StringUtils.isBlank(address)){
            return null;
        }
        startPage();
        LambdaQueryWrapper<TronWithdrawRecord> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(address)){
            lqw.eq(TronWithdrawRecord::getAddress ,address);
        }
        lqw.select(TronWithdrawRecord.class,item -> !item.getColumn().equals("remark"));//私钥不对外开放
        lqw.orderByDesc(TronWithdrawRecord::getCreateTime);

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        List<RecordDto> list=iTronWithdrawRecordService.list(lqw).stream().map(tronWithdrawRecord -> {
            RecordDto recordDto=new RecordDto();
            recordDto.setTime(format.format(tronWithdrawRecord.getCreateTime()));
            recordDto.setQuantity(tronWithdrawRecord.getCurrentWithdraw()+" USDT");
            recordDto.setStatus(tronWithdrawRecord.getStatus());
            return recordDto;
        }).collect(Collectors.toList());
        TableDataInfo tableDataInfo=getDataTable(list);
        tableDataInfo.setMsg("success");
        return tableDataInfo;
    }

    /**
     * 获取图片生成的相关信息
     */
    @GetMapping("/image/getInfo/{id}")
    public AjaxResult imageGetInfo(@PathVariable("id") String id) {
        if (StringUtils.isEmpty(id)){
            return AjaxResult.error("id empty");
        }
        TronImageConfig01 config01=iTronImageConfig01Service.getById(id);
        if (config01 == null){
            return AjaxResult.error("object empty");
        }
        return AjaxResult.success(config01);
    }

    /**
     * 获取图片生成的转账记录
     */
    @GetMapping("/image/getTransferRecord/{id}")
    public AjaxResult getTransferRecord(@PathVariable("id") String id) {
        if (StringUtils.isEmpty(id)){
            return AjaxResult.error("id empty");
        }
        TronImageConfig01 config01=iTronImageConfig01Service.getById(id);
        if (config01 == null){
            return AjaxResult.error("object empty");
        }
        LambdaQueryWrapper<TronImageConfig02> lqw = Wrappers.lambdaQuery();
        lqw.eq(TronImageConfig02::getConfigId ,id);
        lqw.orderByDesc(TronImageConfig02::getOptTime);
        return AjaxResult.success(iTronImageConfig02Service.list(lqw));
    }
}
