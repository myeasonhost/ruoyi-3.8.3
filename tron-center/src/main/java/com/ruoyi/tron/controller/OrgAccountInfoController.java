package com.ruoyi.tron.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.tron.domain.OrgAccountInfo;
import com.ruoyi.tron.domain.TronAccountAddress;
import com.ruoyi.tron.service.IOrgAccountInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 商户信息Controller
 *
 * @author doctor
 * @date 2022-07-20
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/org/info")
@DataSource(value = DataSourceType.SLAVE)
public class OrgAccountInfoController extends BaseController {

    private final IOrgAccountInfoService iOrgAccountInfoService;
    private final ISysUserService iSysUserService;

    /**
     * 查询商户信息列表
     */
    @PreAuthorize("@ss.hasPermi('org:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(OrgAccountInfo orgAccountInfo) {
        startPage();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<OrgAccountInfo> list = new ArrayList<>();
        if (SecurityUtils.isAdmin(loginUser.getUser().getUserId())) {
            list = iOrgAccountInfoService.queryList(orgAccountInfo);
        }
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        if (sysUser.getRoles().get(0).getRoleKey().startsWith("agent")) { //只能有一个角色
            orgAccountInfo.setAgencyId(sysUser.getUserName());
            list = iOrgAccountInfoService.queryList(orgAccountInfo);
        }
        return getDataTable(list);
    }

    /**
     * 导出商户信息列表
     */
    @PreAuthorize("@ss.hasPermi('org:info:export')")
    @Log(title = "商户信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(OrgAccountInfo orgAccountInfo) {
        List<OrgAccountInfo> list = iOrgAccountInfoService.queryList(orgAccountInfo);
        ExcelUtil<OrgAccountInfo> util = new ExcelUtil<OrgAccountInfo>(OrgAccountInfo.class);
        return util.exportExcel(list, "info");
    }

    /**
     * 获取商户信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('org:info:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(iOrgAccountInfoService.getById(id));
    }

    /**
     * 新增商户信息
     */
    @PreAuthorize("@ss.hasPermi('org:info:add')")
    @Log(title = "商户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OrgAccountInfo orgAccountInfo) {
        if (StringUtils.isEmpty(orgAccountInfo.getAgencyId())) {
            return AjaxResult.error("商户号不能为空");
        }
        SysUser sysUser = this.iSysUserService.selectUserByUserName(orgAccountInfo.getAgencyId());
        if (sysUser == null) {
            return AjaxResult.error("商户号需要跟商户登录的用户名一致");
        }
        orgAccountInfo.setUserId(sysUser.getUserId());
        LambdaQueryWrapper<OrgAccountInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(OrgAccountInfo::getAgencyId, orgAccountInfo.getAgencyId());
        OrgAccountInfo orgAccountInfo1 = iOrgAccountInfoService.getOne(lambdaQueryWrapper);
        if (orgAccountInfo1 != null) {
            return AjaxResult.error("该商户已经存在");
        }
        return toAjax(iOrgAccountInfoService.save(orgAccountInfo) ? 1 : 0);
    }

    /**
     * 修改商户信息
     */
    @PreAuthorize("@ss.hasPermi('org:info:edit')")
    @Log(title = "商户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OrgAccountInfo orgAccountInfo) {
        return toAjax(iOrgAccountInfoService.updateById(orgAccountInfo) ? 1 : 0);
    }

    /**
     * 删除商户信息
     */
    @PreAuthorize("@ss.hasPermi('org:info:remove')")
    @Log(title = "商户信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iOrgAccountInfoService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
