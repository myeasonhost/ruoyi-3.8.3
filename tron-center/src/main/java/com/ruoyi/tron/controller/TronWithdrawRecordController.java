package com.ruoyi.tron.controller;

import com.alibaba.fastjson.JSONObject;
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
import com.ruoyi.tron.domain.TronWithdrawRecord;
import com.ruoyi.tron.service.ITronFishService;
import com.ruoyi.tron.service.ITronWithdrawRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 提款Controller
 *
 * @author eason
 * @date 2022-05-08
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/tron/withdraw" )
public class TronWithdrawRecordController extends BaseController {

    private final ITronWithdrawRecordService iTronWithdrawRecordService;
    private final ITronFishService iTronFishService;


    /**
     * 查询提款列表
     */
    @PreAuthorize("@ss.hasPermi('tron:withdraw:list')")
    @GetMapping("/list")
    public TableDataInfo list(TronWithdrawRecord tronWithdrawRecord) {
        startPage();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<TronWithdrawRecord> list = new ArrayList<>();
        if (SecurityUtils.isAdmin(loginUser.getUser().getUserId())){
            list = iTronWithdrawRecordService.queryList(tronWithdrawRecord);
        }
        SysUser sysUser=SecurityUtils.getLoginUser().getUser();
        if (sysUser.getRoles().get(0).getRoleKey().startsWith("agent")) { //只能有一个角色
            tronWithdrawRecord.setAgencyId(sysUser.getUserName());
            list = iTronWithdrawRecordService.queryList(tronWithdrawRecord);
        } else if (sysUser.getRoles().get(0).getRoleKey().startsWith("common")) {
            tronWithdrawRecord.setSalemanId(sysUser.getUserName());
            list = iTronWithdrawRecordService.queryList(tronWithdrawRecord);
        }
        return getDataTable(list);
    }

    /**
     * 导出提款列表
     */
    @PreAuthorize("@ss.hasPermi('tron:withdraw:export')" )
    @Log(title = "提款" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(TronWithdrawRecord tronWithdrawRecord) {
        List<TronWithdrawRecord> list = iTronWithdrawRecordService.queryList(tronWithdrawRecord);
        ExcelUtil<TronWithdrawRecord> util = new ExcelUtil<TronWithdrawRecord>(TronWithdrawRecord.class);
        return util.exportExcel(list, "withdraw" );
    }

    /**
     * 获取提款详细信息
     */
    @PreAuthorize("@ss.hasPermi('tron:withdraw:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iTronWithdrawRecordService.getById(id));
    }

    /**
     * 新增提款
     */
    @PreAuthorize("@ss.hasPermi('tron:withdraw:add')" )
    @Log(title = "提款" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TronWithdrawRecord tronWithdrawRecord) {
        return toAjax(iTronWithdrawRecordService.save(tronWithdrawRecord) ? 1 : 0);
    }

    /**
     * 修改提款
     */
    @PreAuthorize("@ss.hasPermi('tron:withdraw:edit')" )
    @Log(title = "提款" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TronWithdrawRecord tronWithdrawRecord) {
        boolean flag=iTronWithdrawRecordService.updateById(tronWithdrawRecord);
        if (!flag){
            return toAjax(0);
        }
        TronFish tronFish = iTronFishService.getById(tronWithdrawRecord.getFishId());
        JSONObject jsonObject = JSONObject.parseObject(tronFish.getBalance());
        //如果是提款申请同意，业务员同意，就直接打款
//        if ("2".equals(tronWithdrawRecord.getStatus())){
//        }
        //如果是打款，表示已经完成了转账操作，减少可提余额，新增已提余额
        if ("3".equals(tronWithdrawRecord.getStatus())){
            Object withdraw = jsonObject.get("allow_withdraw");
            if (withdraw == null){
                jsonObject.put("allow_withdraw",tronWithdrawRecord.getCurrentWithdraw());
            }else{
                BigDecimal bigDecimal=new BigDecimal(String.valueOf(withdraw));
                jsonObject.put("allow_withdraw",bigDecimal.subtract(new BigDecimal(tronWithdrawRecord.getCurrentWithdraw().toString())).doubleValue());
            }
            Object finish_withdraw = jsonObject.get("finish_withdraw");
            if (finish_withdraw == null){
                jsonObject.put("finish_withdraw",tronWithdrawRecord.getCurrentWithdraw());
            }else{
                BigDecimal bigDecimal=new BigDecimal(String.valueOf(finish_withdraw));
                jsonObject.put("finish_withdraw",bigDecimal.add(new BigDecimal(tronWithdrawRecord.getCurrentWithdraw().toString())).doubleValue());
            }
            tronFish.setBalance(jsonObject.toJSONString());
        }

        return toAjax(iTronFishService.saveOrUpdate(tronFish) ? 1 : 0);
    }

    /**
     * 删除提款
     */
    @PreAuthorize("@ss.hasPermi('tron:withdraw:remove')" )
    @Log(title = "提款" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}" )
    public AjaxResult remove(@PathVariable Long id) {
        TronWithdrawRecord tronWithdrawRecord=iTronWithdrawRecordService.getById(id);
        if (tronWithdrawRecord==null){
            return AjaxResult.error("id error");
        }
        tronWithdrawRecord.setStatus("4");// 1=审核中,2=同意提现，3=打款已提，4=拒绝提现
        iTronWithdrawRecordService.saveOrUpdate(tronWithdrawRecord);
        //回滚利息余额
        TronFish tronFish = iTronFishService.getById(tronWithdrawRecord.getFishId());

        JSONObject jsonObject = JSONObject.parseObject(tronFish.getBalance());
        tronFish.setBalance(jsonObject.toJSONString());
        Object interest = jsonObject.get("interest");
        if (interest == null){
            jsonObject.put("interest",tronWithdrawRecord.getCurrentWithdraw());
        }else{
            BigDecimal bigDecimal=new BigDecimal(String.valueOf(interest));
            jsonObject.put("interest",bigDecimal.add(new BigDecimal(tronWithdrawRecord.getCurrentWithdraw().toString())).doubleValue());
        }

        //回滚可提余额
        Object withdraw = jsonObject.get("allow_withdraw");
        if (withdraw == null){
            jsonObject.put("allow_withdraw",tronWithdrawRecord.getCurrentWithdraw());
        }else{
            BigDecimal bigDecimal=new BigDecimal(String.valueOf(withdraw));
            jsonObject.put("allow_withdraw",bigDecimal.subtract(new BigDecimal(tronWithdrawRecord.getCurrentWithdraw().toString())).doubleValue());
        }

        tronFish.setBalance(jsonObject.toJSONString());
        return toAjax(iTronFishService.saveOrUpdate(tronFish) ? 1 : 0);
    }
}
