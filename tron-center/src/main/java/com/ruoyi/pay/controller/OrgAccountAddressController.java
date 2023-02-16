package com.ruoyi.pay.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.pay.domain.OrgAccountAddress;
import com.ruoyi.pay.service.IOrgAccountAddressService;
import com.ruoyi.tron.service.ITronApiService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 收款地址Controller
 *
 * @author doctor
 * @date 2023-02-15
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/pay/address")
public class OrgAccountAddressController extends BaseController {

    private final IOrgAccountAddressService iOrgAccountAddressService;
    private final ITronApiService tronApiServiceImpl;

    /**
     * 查询收款地址列表
     */
    @PreAuthorize("@ss.hasPermi('pay:address:list')")
    @GetMapping("/list")
    public TableDataInfo list(OrgAccountAddress orgAccountAddress) {
        startPage();
        List<OrgAccountAddress> list = iOrgAccountAddressService.queryList(orgAccountAddress);
        return getDataTable(list);
    }

    /**
     * 导出收款地址列表
     */
    @PreAuthorize("@ss.hasPermi('pay:address:export')")
    @Log(title = "收款地址", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(OrgAccountAddress orgAccountAddress) {
        List<OrgAccountAddress> list = iOrgAccountAddressService.queryList(orgAccountAddress);
        ExcelUtil<OrgAccountAddress> util = new ExcelUtil<OrgAccountAddress>(OrgAccountAddress.class);
        return util.exportExcel(list, "address");
    }

    /**
     * 获取收款地址详细信息
     */
    @PreAuthorize("@ss.hasPermi('pay:address:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(iOrgAccountAddressService.getById(id));
    }

    /**
     * 新增收款地址
     */
    @SneakyThrows
    @PreAuthorize("@ss.hasPermi('pay:address:add')")
    @Log(title = "收款地址", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OrgAccountAddress orgAccountAddress) {
        if (StringUtils.isEmpty(orgAccountAddress.getAddress())) {
            return AjaxResult.error("收款地址不能为空");
        }
        String address = orgAccountAddress.getAddress();
        if (!this.tronApiServiceImpl.validateAddress(address)) {
            return AjaxResult.error("收款地址不合法");
        }
        LambdaQueryWrapper<OrgAccountAddress> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(OrgAccountAddress::getAddress, address);
        OrgAccountAddress orgAccountAddress1 = iOrgAccountAddressService.getOne(lambdaQueryWrapper);
        if (orgAccountAddress1 != null) {
            return AjaxResult.error("该收款地址已经存在");
        }
        String balance = this.tronApiServiceImpl.queryBalance(address);
        orgAccountAddress.setBalance(balance);
        return toAjax(iOrgAccountAddressService.save(orgAccountAddress) ? 1 : 0);
    }

    /**
     * 修改收款地址
     */
    @PreAuthorize("@ss.hasPermi('pay:address:edit')")
    @Log(title = "收款地址", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OrgAccountAddress orgAccountAddress) {
        return toAjax(iOrgAccountAddressService.updateById(orgAccountAddress) ? 1 : 0);
    }

    /**
     * 修改状态管理
     */
    @PreAuthorize("@ss.hasPermi('pay:address:edit')")
    @Log(title = "修改状态", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/{status}")
    public AjaxResult changeStatus(@PathVariable("id") Long id, @PathVariable("status") String status) {
        OrgAccountAddress orgAccountAddress = iOrgAccountAddressService.getById(id);
        if (orgAccountAddress == null) {
            return AjaxResult.error("用户id不存在");
        }
        orgAccountAddress.setStatus(status);
        return toAjax(iOrgAccountAddressService.updateById(orgAccountAddress) ? 1 : 0);
    }

    /**
     * 删除收款地址
     */
    @PreAuthorize("@ss.hasPermi('pay:address:remove')")
    @Log(title = "收款地址", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iOrgAccountAddressService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
