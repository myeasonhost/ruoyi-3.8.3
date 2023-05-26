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
import com.ruoyi.pay.domain.OrgAccountOrderDaip;
import com.ruoyi.pay.dto.StatUsdtDto;
import com.ruoyi.pay.message.MessageProducer;
import com.ruoyi.pay.service.IOrgAccountOrderDaipService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.*;

/**
 * 商户代付Controller
 *
 * @author doctor
 * @date 2023-03-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/pay/daip")
public class OrgAccountOrderDaipController extends BaseController {

    private final IOrgAccountOrderDaipService iOrgAccountOrderDaipService;
    private final MessageProducer messageProducer;

    /**
     * 查询商户代付列表
     */
    @PreAuthorize("@ss.hasPermi('pay:daip:list')")
    @GetMapping("/list")
    public TableDataInfo list(OrgAccountOrderDaip orgAccountOrderDaip) {
        startPage();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<OrgAccountOrderDaip> list = new ArrayList<>();
        if (SecurityUtils.isAdmin(loginUser.getUser().getUserId())) {
            list = iOrgAccountOrderDaipService.queryList(orgAccountOrderDaip);
        }
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        if (sysUser.getRoles().get(0).getRoleKey().startsWith("agent")) { //只能有一个角色
            orgAccountOrderDaip.setSiteId(sysUser.getUserName());
            list = iOrgAccountOrderDaipService.queryList(orgAccountOrderDaip);
        }
        return getDataTable(list);
    }

    /**
     * 导出商户代付列表
     */
    @PreAuthorize("@ss.hasPermi('pay:daip:export')")
    @Log(title = "商户代付", businessType = BusinessType.EXPORT)
    @RequestMapping("/export")
    public void export(HttpServletResponse response, OrgAccountOrderDaip orgAccountOrderDaip) {
        startPage();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<OrgAccountOrderDaip> list = new ArrayList<>();
        if (SecurityUtils.isAdmin(loginUser.getUser().getUserId())) {
            list = iOrgAccountOrderDaipService.queryList(orgAccountOrderDaip);
        }
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        if (sysUser.getRoles().get(0).getRoleKey().startsWith("agent")) { //只能有一个角色
            orgAccountOrderDaip.setSiteId(sysUser.getUserName());
            list = iOrgAccountOrderDaipService.queryList(orgAccountOrderDaip);
        }
        ExcelUtil<OrgAccountOrderDaip> util = new ExcelUtil<>(OrgAccountOrderDaip.class);
        util.exportExcel(response, list, "daip");
    }

    /**
     * 获取商户代付详细信息
     */
    @PreAuthorize("@ss.hasPermi('pay:daip:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(iOrgAccountOrderDaipService.getById(id));
    }

    /**
     * 新增商户代付
     */
    @PreAuthorize("@ss.hasPermi('pay:daip:add')")
    @Log(title = "商户代付", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OrgAccountOrderDaip orgAccountOrderDaip) {
        return toAjax(iOrgAccountOrderDaipService.save(orgAccountOrderDaip) ? 1 : 0);
    }

    /**
     * 修改商户代付
     */
    @PreAuthorize("@ss.hasPermi('pay:daip:edit')")
    @Log(title = "商户代付", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OrgAccountOrderDaip orgAccountOrderDaip) {
        orgAccountOrderDaip.setPayTime(new Date());//手动支付时间更新
        boolean flag = iOrgAccountOrderDaipService.updateById(orgAccountOrderDaip);
        if ("2".equals(orgAccountOrderDaip.getStatus()) && flag) {
            //发送回调消息
            messageProducer.pdaiCallBackOutput(orgAccountOrderDaip, 0);
        }
        return toAjax(flag ? 1 : 0);
    }

    /**
     * 删除商户代付
     */
    @PreAuthorize("@ss.hasPermi('pay:daip:remove')")
    @Log(title = "商户代付", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(iOrgAccountOrderDaipService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }

    /**
     * 提现U统计
     */
    @PreAuthorize("@ss.hasPermi('pay:order:query')")
    @PostMapping("/count/stat")
    public AjaxResult count(StatUsdtDto statUsdtDto) throws ParseException {
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        if (sysUser.getRoles().get(0).getRoleKey().startsWith("admin")) { //只能有一个角色
            statUsdtDto.setAgencyId(null); //查询所有的代理
            statUsdtDto.setSalemanId(null);
        }
        if (sysUser.getRoles().get(0).getRoleKey().startsWith("agent")) { //只能有一个角色
            statUsdtDto.setAgencyId(sysUser.getUserName()); //查询当前的代理
            statUsdtDto.setSalemanId(null);
        }
        StatUsdtDto statUsdtDto1 = new StatUsdtDto();
        //查询累计充值总数
        Integer totalPayCount = iOrgAccountOrderDaipService.queryCount(statUsdtDto);
        statUsdtDto1.setTotalPayCount(totalPayCount);
        //查询累计入U总额
        Map<String, Object> usdtMap = iOrgAccountOrderDaipService.queryTotalUsdt(statUsdtDto);
        statUsdtDto1.setTotalPayUsdt((Double) usdtMap.get("usdt"));
        //查询今日充值总数
        statUsdtDto.setCreateTime(new Date(System.currentTimeMillis()));
        Integer dayPayCount = iOrgAccountOrderDaipService.queryCount(statUsdtDto);
        statUsdtDto1.setDayPayCount(dayPayCount);
        //查询今日入U总数
        Map<String, Object> usdtMap2 = iOrgAccountOrderDaipService.queryTotalUsdt(statUsdtDto);
        statUsdtDto1.setDayPayUsdt((Double) usdtMap2.get("usdt"));
        return AjaxResult.success(statUsdtDto1);
    }
}
