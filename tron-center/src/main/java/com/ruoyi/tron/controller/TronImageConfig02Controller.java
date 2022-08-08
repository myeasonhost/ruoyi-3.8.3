package com.ruoyi.tron.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.tron.domain.TronImageConfig01;
import com.ruoyi.tron.domain.TronImageConfig02;
import com.ruoyi.tron.service.ITronAuthAddressService;
import com.ruoyi.tron.service.ITronImageConfig01Service;
import com.ruoyi.tron.service.ITronImageConfig02Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 图片配置02Controller
 *
 * @author eason
 * @date 2022-05-17
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/tron/config02" )
public class TronImageConfig02Controller extends BaseController {

    private final ITronImageConfig02Service iTronImageConfig02Service;
    private final ITronAuthAddressService iTronAuthAddressService;
    private final ITronImageConfig01Service iTronImageConfig01Service;

    /**
     * 查询图片配置02列表
     */
    @PreAuthorize("@ss.hasPermi('tron:config02:list')")
    @GetMapping("/list")
    public TableDataInfo list(TronImageConfig02 tronImageConfig02) {
        startPage();
        SysUser sysUser=SecurityUtils.getLoginUser().getUser();
        tronImageConfig02.setSalemanId(sysUser.getUserName());
        tronImageConfig02.setConfigId(tronImageConfig02.getConfigId());
        List<TronImageConfig02> list = iTronImageConfig02Service.queryList(tronImageConfig02);
        return getDataTable(list);
    }

    /**
     * 导出图片配置02列表
     */
    @PreAuthorize("@ss.hasPermi('tron:config02:export')" )
    @Log(title = "图片配置02" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(TronImageConfig02 tronImageConfig02) {
        List<TronImageConfig02> list = iTronImageConfig02Service.queryList(tronImageConfig02);
        ExcelUtil<TronImageConfig02> util = new ExcelUtil<TronImageConfig02>(TronImageConfig02.class);
        return util.exportExcel(list, "config02" );
    }

    /**
     * 获取图片配置02详细信息
     */
    @PreAuthorize("@ss.hasPermi('tron:config02:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iTronImageConfig02Service.getById(id));
    }

    /**
     * 新增图片配置02
     */
    @PreAuthorize("@ss.hasPermi('tron:config02:add')" )
    @Log(title = "图片配置02" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TronImageConfig02 tronImageConfig02) {
        SysUser sysUser= SecurityUtils.getLoginUser().getUser();
        if (sysUser.getRoles().get(0).getRoleKey().startsWith("admin")) { //只能有一个角色
            tronImageConfig02.setAgencyId(sysUser.getUserName()); //查询所有的代理
            tronImageConfig02.setSalemanId(sysUser.getUserName());
        }
        if (sysUser.getRoles().get(0).getRoleKey().startsWith("agent")) { //只能有一个角色
            tronImageConfig02.setAgencyId(sysUser.getUserName()); //查询当前的代理
            tronImageConfig02.setSalemanId(sysUser.getUserName());
        }
        if (sysUser.getRoles().get(0).getRoleKey().startsWith("common")) {
            tronImageConfig02.setSalemanId(sysUser.getUserName());
            String agencyId=iTronAuthAddressService.queryAgent(sysUser.getDeptId());
            tronImageConfig02.setAgencyId(agencyId);
        }
        LambdaQueryWrapper<TronImageConfig01> lqw = Wrappers.lambdaQuery();
        lqw.eq(TronImageConfig01::getSalemanId ,tronImageConfig02.getSalemanId());
        TronImageConfig01 config01=iTronImageConfig01Service.getOne(lqw);
        if (config01!=null){
            tronImageConfig02.setConfigId(config01.getId());
        }
        return toAjax(iTronImageConfig02Service.save(tronImageConfig02) ? 1 : 0);
    }

    /**
     * 修改图片配置02
     */
    @PreAuthorize("@ss.hasPermi('tron:config02:edit')" )
    @Log(title = "图片配置02" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TronImageConfig02 tronImageConfig02) {
        return toAjax(iTronImageConfig02Service.updateById(tronImageConfig02) ? 1 : 0);
    }

    /**
     * 删除图片配置02
     */
    @PreAuthorize("@ss.hasPermi('tron:config02:remove')" )
    @Log(title = "图片配置02" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iTronImageConfig02Service.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
