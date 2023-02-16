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
import com.ruoyi.pay.domain.OrgAccountOrder;
import com.ruoyi.pay.service.IOrgAccountOrderService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 支付订单Controller
 *
 * @author doctor
 * @date 2023-02-15
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/pay/order" )
public class OrgAccountOrderController extends BaseController {

    private final IOrgAccountOrderService iOrgAccountOrderService;

    /**
     * 查询支付订单列表
     */
    @PreAuthorize("@ss.hasPermi('pay:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(OrgAccountOrder orgAccountOrder) {
        startPage();
        List<OrgAccountOrder> list = iOrgAccountOrderService.queryList(orgAccountOrder);
        return getDataTable(list);
    }

    /**
     * 导出支付订单列表
     */
    @PreAuthorize("@ss.hasPermi('pay:order:export')" )
    @Log(title = "支付订单" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(OrgAccountOrder orgAccountOrder) {
        List<OrgAccountOrder> list = iOrgAccountOrderService.queryList(orgAccountOrder);
        ExcelUtil<OrgAccountOrder> util = new ExcelUtil<OrgAccountOrder>(OrgAccountOrder.class);
        return util.exportExcel(list, "order" );
    }

    /**
     * 获取支付订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('pay:order:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) String id) {
        return AjaxResult.success(iOrgAccountOrderService.getById(id));
    }

    /**
     * 新增支付订单
     */
    @PreAuthorize("@ss.hasPermi('pay:order:add')" )
    @Log(title = "支付订单" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OrgAccountOrder orgAccountOrder) {
        return toAjax(iOrgAccountOrderService.save(orgAccountOrder) ? 1 : 0);
    }

    /**
     * 修改支付订单
     */
    @PreAuthorize("@ss.hasPermi('pay:order:edit')" )
    @Log(title = "支付订单" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OrgAccountOrder orgAccountOrder) {
        return toAjax(iOrgAccountOrderService.updateById(orgAccountOrder) ? 1 : 0);
    }

    /**
     * 删除支付订单
     */
    @PreAuthorize("@ss.hasPermi('pay:order:remove')" )
    @Log(title = "支付订单" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(iOrgAccountOrderService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
