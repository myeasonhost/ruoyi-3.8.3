package com.ruoyi.tron.controller;

import java.util.List;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.tron.domain.OrgAccountAddress;
import com.ruoyi.tron.service.IOrgAccountAddressService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商户账户Controller
 *
 * @author doctor
 * @date 2022-07-22
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/org/account" )
public class OrgAccountAddressController extends BaseController {

    private final IOrgAccountAddressService iOrgAccountAddressService;

    /**
     * 查询商户账户列表
     */
    @PreAuthorize("@ss.hasPermi('org:account:list')")
    @GetMapping("/list")
    public TableDataInfo list(OrgAccountAddress orgAccountAddress) {
        startPage();
        List<OrgAccountAddress> list = iOrgAccountAddressService.queryList(orgAccountAddress);
        return getDataTable(list);
    }

    /**
     * 导出商户账户列表
     */
    @PreAuthorize("@ss.hasPermi('org:account:export')" )
    @Log(title = "商户账户" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(OrgAccountAddress orgAccountAddress) {
        List<OrgAccountAddress> list = iOrgAccountAddressService.queryList(orgAccountAddress);
        ExcelUtil<OrgAccountAddress> util = new ExcelUtil<OrgAccountAddress>(OrgAccountAddress.class);
        return util.exportExcel(list, "account" );
    }

    /**
     * 获取商户账户详细信息
     */
    @PreAuthorize("@ss.hasPermi('org:account:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iOrgAccountAddressService.getById(id));
    }

    /**
     * 新增商户账户
     */
    @PreAuthorize("@ss.hasPermi('org:account:add')" )
    @Log(title = "商户账户" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OrgAccountAddress orgAccountAddress) {
        return toAjax(iOrgAccountAddressService.save(orgAccountAddress) ? 1 : 0);
    }

    /**
     * 修改商户账户
     */
    @PreAuthorize("@ss.hasPermi('org:account:edit')" )
    @Log(title = "商户账户" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OrgAccountAddress orgAccountAddress) {
        return toAjax(iOrgAccountAddressService.updateById(orgAccountAddress) ? 1 : 0);
    }

    /**
     * 删除商户账户
     */
    @PreAuthorize("@ss.hasPermi('org:account:remove')" )
    @Log(title = "商户账户" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iOrgAccountAddressService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
