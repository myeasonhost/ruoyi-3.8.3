package com.ruoyi.tron.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.tron.domain.TronAccountAddress;
import com.ruoyi.tron.service.ITronAccountAddressService;
import com.ruoyi.tron.service.ITronApiService;
import com.sunlight.tronsdk.address.AddressHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 站内账号Controller
 *
 * @author eason
 * @date 2022-05-05
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/tron/account")
public class TronAccountAddressController extends BaseController {

    private final ITronAccountAddressService iTronAccountAddressService;
    private final ITronApiService tronApiServiceImpl;

    /**
     * 查询站内账号列表
     */
    @PreAuthorize("@ss.hasPermi('tron:account:list')")
    @GetMapping("/list")
    public TableDataInfo list(TronAccountAddress tronAccountAddress) {
        startPage();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<TronAccountAddress> list = new ArrayList<>();
        if (SecurityUtils.isAdmin(loginUser.getUser().getUserId())) {
            list = iTronAccountAddressService.queryList(tronAccountAddress);
        }
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        if (sysUser.getRoles().get(0).getRoleKey().startsWith("agent")) { //只能有一个角色
            tronAccountAddress.setAgencyId(sysUser.getUserName());
            list = iTronAccountAddressService.queryList(tronAccountAddress);
        }
        return getDataTable(list);
    }

    /**
     * 导出站内账号列表
     */
    @PreAuthorize("@ss.hasPermi('tron:account:export')")
    @Log(title = "站内账号", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TronAccountAddress tronAccountAddress) {
        List<TronAccountAddress> list = iTronAccountAddressService.queryList(tronAccountAddress);
        ExcelUtil<TronAccountAddress> util = new ExcelUtil<TronAccountAddress>(TronAccountAddress.class);
        return util.exportExcel(list, "account");
    }

    /**
     * 获取站内账号详细信息
     */
    @PreAuthorize("@ss.hasPermi('tron:account:query')")
    @GetMapping(value = "/{id}/{method}")
    public AjaxResult getInfo(@PathVariable("id") Long id, @PathVariable("method") String method) {
        TronAccountAddress tronAccountAddress = iTronAccountAddressService.getById(id);
        if ("detail".equals(method)) {
            tronAccountAddress.setPrivateKey(null); //私钥不对外开放
            return AjaxResult.success(tronAccountAddress);
        }

        if ("queryBalance".equals(method)) {
            String balance = tronApiServiceImpl.queryBalance(tronAccountAddress.getAddress());
            if (balance == null) {
                return toAjax(0);
            }
            tronAccountAddress.setBalance(balance);
            iTronAccountAddressService.updateById(tronAccountAddress);
            return AjaxResult.success(balance);
        }
        return AjaxResult.error("查询失败");
    }

    /**
     * 新增站内账号
     */
    @PreAuthorize("@ss.hasPermi('tron:account:add')")
    @Log(title = "站内账号", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TronAccountAddress tronAccountAddress) throws Exception {
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        tronAccountAddress.setAgencyId(sysUser.getUserName());
        tronAccountAddress.setHexAddress(AddressHelper.toHexString(tronAccountAddress.getAddress()));
        tronAccountAddress.setBalance("{trx:0.0,usdt:0.0}");
        return toAjax(iTronAccountAddressService.save(tronAccountAddress) ? 1 : 0);
    }

    /**
     * 修改站内账号
     */
    @PreAuthorize("@ss.hasPermi('tron:account:edit')")
    @Log(title = "站内账号", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TronAccountAddress tronAccountAddress) {
        return toAjax(iTronAccountAddressService.updateById(tronAccountAddress) ? 1 : 0);
    }

    /**
     * 删除站内账号
     */
    @PreAuthorize("@ss.hasPermi('tron:account:remove')")
    @Log(title = "站内账号", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iTronAccountAddressService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
