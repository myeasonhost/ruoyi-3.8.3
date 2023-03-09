package com.ruoyi.pay.controller;

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
import com.ruoyi.pay.domain.OrgAccountOrderDaip;
import com.ruoyi.pay.service.IOrgAccountOrderDaipService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商户代付Controller
 *
 * @author doctor
 * @date 2023-03-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/pay/daip" )
public class OrgAccountOrderDaipController extends BaseController {

    private final IOrgAccountOrderDaipService iOrgAccountOrderDaipService;

    /**
     * 查询商户代付列表
     */
    @PreAuthorize("@ss.hasPermi('pay:daip:list')")
    @GetMapping("/list")
    public TableDataInfo list(OrgAccountOrderDaip orgAccountOrderDaip) {
        startPage();
        List<OrgAccountOrderDaip> list = iOrgAccountOrderDaipService.queryList(orgAccountOrderDaip);
        return getDataTable(list);
    }

    /**
     * 导出商户代付列表
     */
    @PreAuthorize("@ss.hasPermi('pay:daip:export')" )
    @Log(title = "商户代付" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(OrgAccountOrderDaip orgAccountOrderDaip) {
        List<OrgAccountOrderDaip> list = iOrgAccountOrderDaipService.queryList(orgAccountOrderDaip);
        ExcelUtil<OrgAccountOrderDaip> util = new ExcelUtil<OrgAccountOrderDaip>(OrgAccountOrderDaip.class);
        return util.exportExcel(list, "daip" );
    }

    /**
     * 获取商户代付详细信息
     */
    @PreAuthorize("@ss.hasPermi('pay:daip:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) String id) {
        return AjaxResult.success(iOrgAccountOrderDaipService.getById(id));
    }

    /**
     * 新增商户代付
     */
    @PreAuthorize("@ss.hasPermi('pay:daip:add')" )
    @Log(title = "商户代付" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OrgAccountOrderDaip orgAccountOrderDaip) {
        return toAjax(iOrgAccountOrderDaipService.save(orgAccountOrderDaip) ? 1 : 0);
    }

    /**
     * 修改商户代付
     */
    @PreAuthorize("@ss.hasPermi('pay:daip:edit')" )
    @Log(title = "商户代付" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OrgAccountOrderDaip orgAccountOrderDaip) {
        return toAjax(iOrgAccountOrderDaipService.updateById(orgAccountOrderDaip) ? 1 : 0);
    }

    /**
     * 删除商户代付
     */
    @PreAuthorize("@ss.hasPermi('pay:daip:remove')" )
    @Log(title = "商户代付" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(iOrgAccountOrderDaipService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
