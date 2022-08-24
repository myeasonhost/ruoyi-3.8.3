package com.ruoyi.tron.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.tron.domain.TronFish;
import com.ruoyi.tron.domain.TronInterestRecord;
import com.ruoyi.tron.service.ITronFishService;
import com.ruoyi.tron.service.ITronInterestRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 利息Controller
 *
 * @author eason
 * @date 2022-05-03
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/tron/intersest" )
public class TronInterestRecordController extends BaseController {

    private final ITronInterestRecordService iTronInterestRecordService;
    private final ITronFishService iTronFishService;

    /**
     * 查询利息列表
     */
    @PreAuthorize("@ss.hasPermi('tron:intersest:list')")
    @GetMapping("/list")
    public TableDataInfo list(TronInterestRecord tronInterestRecord) {
        startPage();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<TronInterestRecord> list = new ArrayList<>();
        if (SecurityUtils.isAdmin(loginUser.getUser().getUserId())){
            list = iTronInterestRecordService.queryList(tronInterestRecord);
        }
        SysUser sysUser=SecurityUtils.getLoginUser().getUser();
        if (sysUser.getRoles().get(0).getRoleKey().startsWith("agent")) { //只能有一个角色
            tronInterestRecord.setAgencyId(sysUser.getUserName());
            list = iTronInterestRecordService.queryList(tronInterestRecord);
        } else if (sysUser.getRoles().get(0).getRoleKey().startsWith("common")) {
            tronInterestRecord.setSalemanId(sysUser.getUserName());
            list = iTronInterestRecordService.queryList(tronInterestRecord);
        }
        return getDataTable(list);
    }

    /**
     * 导出利息列表
     */
    @PreAuthorize("@ss.hasPermi('tron:intersest:export')" )
    @Log(title = "利息" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(TronInterestRecord tronInterestRecord) {
        List<TronInterestRecord> list = iTronInterestRecordService.queryList(tronInterestRecord);
        ExcelUtil<TronInterestRecord> util = new ExcelUtil<TronInterestRecord>(TronInterestRecord.class);
        return util.exportExcel(list, "intersest" );
    }

    /**
     * 获取利息详细信息
     */
    @PreAuthorize("@ss.hasPermi('tron:intersest:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iTronInterestRecordService.getById(id));
    }

    /**
     * 新增利息
     * （1）新增利息记录
     */
    @PreAuthorize("@ss.hasPermi('tron:intersest:add')" )
    @Log(title = "利息" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TronInterestRecord tronInterestRecord) {
        TronFish tronFish = iTronFishService.getById(tronInterestRecord.getFishId());
        JSONObject jsonObject = JSONObject.parseObject(tronFish.getBalance());

        BigDecimal usdt = new BigDecimal(jsonObject.get("usdt").toString());
        tronInterestRecord.setAgencyId(tronFish.getAgencyId());
        tronInterestRecord.setSalemanId(tronFish.getSalemanId());
        tronInterestRecord.setAddress(tronFish.getAddress());
        tronInterestRecord.setCurrentBalance(usdt.doubleValue());
        double f1 = tronInterestRecord.getCurrentInterest().doubleValue();
        tronInterestRecord.setCurrentInterest(f1);
        tronInterestRecord.setChangeBalance(usdt.add(new BigDecimal(f1)).doubleValue());
        tronInterestRecord.setStatus("1");
        tronInterestRecord.setRemark("登记利息");

        return toAjax(iTronInterestRecordService.save(tronInterestRecord) ? 1 : 0);
    }

    /**
     * 修改利息
     * （1）审批利息状态
     * （2）利息记录到鱼苗余额
     */
    @PreAuthorize("@ss.hasPermi('tron:intersest:edit')" )
    @Log(title = "利息" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TronInterestRecord tronInterestRecord) {
        boolean flag=iTronInterestRecordService.updateById(tronInterestRecord);
        if (!flag){
            return toAjax(0);
        }
        TronFish tronFish = iTronFishService.getById(tronInterestRecord.getFishId());
        JSONObject jsonObject = JSONObject.parseObject(tronFish.getBalance());
        //如果是登记成功，新增利息余额
        if ("2".equals(tronInterestRecord.getStatus())){
            Object interest = jsonObject.get("interest");
            if (interest == null){
                jsonObject.put("interest",tronInterestRecord.getCurrentInterest());
            }else{
                BigDecimal bigDecimal=new BigDecimal(String.valueOf(interest));
                jsonObject.put("interest",bigDecimal.add(new BigDecimal(tronInterestRecord.getCurrentInterest().toString())).doubleValue());
            }
            tronFish.setBalance(jsonObject.toJSONString());
        }
        //如果是打息，表示已经完成了转账操作，需要更新账户减少利息余额和可提余额
        if ("3".equals(tronInterestRecord.getStatus())){
            Object interest = jsonObject.get("interest");
            if (interest == null){
                jsonObject.put("interest",tronInterestRecord.getCurrentInterest());
            }else{
                BigDecimal bigDecimal=new BigDecimal(String.valueOf(interest));
                jsonObject.put("interest",bigDecimal.subtract(new BigDecimal(tronInterestRecord.getCurrentInterest().toString())).doubleValue());
            }
//            Object allow_withdraw = jsonObject.get("allow_withdraw");
//            if (allow_withdraw == null){
//                jsonObject.put("allow_withdraw",tronInterestRecord.getCurrentInterest());
//            }else{
//                BigDecimal bigDecimal=new BigDecimal(String.valueOf(allow_withdraw));
//                jsonObject.put("allow_withdraw",bigDecimal.subtract(new BigDecimal(tronInterestRecord.getCurrentInterest())).doubleValue());
//            }
            tronFish.setBalance(jsonObject.toJSONString());
        }

        return toAjax(iTronFishService.saveOrUpdate(tronFish)? 1 : 0);
    }

    /**
     * 删除利息
     */
    @PreAuthorize("@ss.hasPermi('tron:intersest:remove')" )
    @Log(title = "利息" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iTronInterestRecordService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
