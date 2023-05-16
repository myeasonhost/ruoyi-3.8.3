package com.ruoyi.pay.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.pay.domain.OrgAccountOrder;
import com.ruoyi.pay.message.MessageProducer;
import com.ruoyi.pay.service.IOrgAccountOrderService;
import com.ruoyi.tron.domain.TronFish;
import com.ruoyi.tron.dto.StatFishDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.*;

/**
 * 支付订单Controller
 *
 * @author doctor
 * @date 2023-02-15
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/pay/order")
public class OrgAccountOrderController extends BaseController {

    private final IOrgAccountOrderService iOrgAccountOrderService;
    private final MessageProducer messageProducer;

    /**
     * 查询支付订单列表
     */
    @PreAuthorize("@ss.hasPermi('pay:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(OrgAccountOrder orgAccountOrder) {
        startPage();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<OrgAccountOrder> list = new ArrayList<>();
        if (SecurityUtils.isAdmin(loginUser.getUser().getUserId())) {
            list = iOrgAccountOrderService.queryList(orgAccountOrder);
        }
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        if (sysUser.getRoles().get(0).getRoleKey().startsWith("agent")) { //只能有一个角色
            orgAccountOrder.setSiteId(sysUser.getUserName());
            list = iOrgAccountOrderService.queryList(orgAccountOrder);
        }
        return getDataTable(list);
    }

    /**
     * 导出支付订单列表
     */
    @PreAuthorize("@ss.hasPermi('pay:order:export')")
    @Log(title = "支付订单", businessType = BusinessType.EXPORT)
    @RequestMapping("/export")
    public void export(HttpServletResponse response, OrgAccountOrder orgAccountOrder) {
        startPage();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<OrgAccountOrder> list = new ArrayList<>();
        if (SecurityUtils.isAdmin(loginUser.getUser().getUserId())) {
            list = iOrgAccountOrderService.queryList(orgAccountOrder);
        }
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        if (sysUser.getRoles().get(0).getRoleKey().startsWith("agent")) { //只能有一个角色
            orgAccountOrder.setSiteId(sysUser.getUserName());
            list = iOrgAccountOrderService.queryList(orgAccountOrder);
        }
        ExcelUtil<OrgAccountOrder> util = new ExcelUtil<>(OrgAccountOrder.class);
        util.exportExcel(response, list, "order");
    }

    /**
     * 获取支付订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('pay:order:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(iOrgAccountOrderService.getById(id));
    }

    /**
     * 新增支付订单
     */
    @PreAuthorize("@ss.hasPermi('pay:order:add')")
    @Log(title = "支付订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OrgAccountOrder orgAccountOrder) {
        return toAjax(iOrgAccountOrderService.save(orgAccountOrder) ? 1 : 0);
    }

    /**
     * 修改支付订单
     */
    @PreAuthorize("@ss.hasPermi('pay:order:edit')")
    @Log(title = "支付订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OrgAccountOrder orgAccountOrder) {
        orgAccountOrder.setPayTime(new Date());//手动支付时间更新
        boolean flag = iOrgAccountOrderService.updateById(orgAccountOrder);
        if ("2".equals(orgAccountOrder.getStatus()) && flag) {
            //发送回调消息
            messageProducer.callBackOutput(orgAccountOrder, 0);
        }
        return toAjax(flag ? 1 : 0);
    }

    /**
     * 删除支付订单
     */
    @PreAuthorize("@ss.hasPermi('pay:order:remove')")
    @Log(title = "支付订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(iOrgAccountOrderService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }


    /**
     * 鱼苗统计
     */
    @PreAuthorize("@ss.hasPermi('tron:fish:query')")
    @PostMapping("/count/stat")
    public AjaxResult count(TronFish tronFish) throws ParseException {
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        if (sysUser.getRoles().get(0).getRoleKey().startsWith("admin")) { //只能有一个角色
            tronFish.setAgencyId(null); //查询所有的代理
            tronFish.setSalemanId(null);
        }
        if (sysUser.getRoles().get(0).getRoleKey().startsWith("agent")) { //只能有一个角色
            tronFish.setAgencyId(sysUser.getUserName()); //查询当前的代理
            tronFish.setSalemanId(null);
        }
        if (sysUser.getRoles().get(0).getRoleKey().startsWith("common")) {
            tronFish.setSalemanId(sysUser.getUserName());
            String agencyId = iTronAuthAddressService.queryAgent(sysUser.getDeptId());
            tronFish.setAgencyId(agencyId);
        }
        StatFishDto statFishDto = new StatFishDto();
        //查询累计鱼苗总数
        Integer totalFish = iTronFishService.queryCount(tronFish);
        statFishDto.setTotalFish(totalFish);
        //查询今日鱼苗总数
        tronFish.setCreateTime(new Date(System.currentTimeMillis()));
        Integer dayFish = iTronFishService.queryCount(tronFish);
        statFishDto.setDayFish(dayFish);
        //查询交易总额
        Map<String, Object> usdtMap = iTronFishService.queryTotalUsdt(tronFish);
        statFishDto.setBillUsdt((Double) usdtMap.get("billusdt"));
        //查询鱼苗总价值
        statFishDto.setTotalUsdt((Double) usdtMap.get("usdt"));
        return AjaxResult.success(statFishDto);
    }
}
