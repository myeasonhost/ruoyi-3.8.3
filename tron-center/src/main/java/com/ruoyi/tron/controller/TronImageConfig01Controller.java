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
import com.ruoyi.tron.service.ITronAuthAddressService;
import com.ruoyi.tron.service.ITronImageConfig01Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 图片配置01Controller
 *
 * @author eason
 * @date 2022-05-17
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/tron/config01" )
public class TronImageConfig01Controller extends BaseController {

    private final ITronImageConfig01Service iTronImageConfig01Service;
    private final ITronAuthAddressService iTronAuthAddressService;


    /**
     * 查询图片配置01列表
     */
    @PreAuthorize("@ss.hasPermi('tron:config01:list')")
    @GetMapping("/list")
    public TableDataInfo list(TronImageConfig01 tronImageConfig01) {
        startPage();
        SysUser sysUser=SecurityUtils.getLoginUser().getUser();
        tronImageConfig01.setSalemanId(sysUser.getUserName());
        List<TronImageConfig01> list=iTronImageConfig01Service.queryList(tronImageConfig01);
        return getDataTable(list);
    }

    /**
     * 导出图片配置01列表
     */
    @PreAuthorize("@ss.hasPermi('tron:config01:export')" )
    @Log(title = "图片配置01" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(TronImageConfig01 tronImageConfig01) {
        List<TronImageConfig01> list = iTronImageConfig01Service.queryList(tronImageConfig01);
        ExcelUtil<TronImageConfig01> util = new ExcelUtil<TronImageConfig01>(TronImageConfig01.class);
        return util.exportExcel(list, "config01" );
    }

    /**
     * 获取图片配置01详细信息
     */
    @PreAuthorize("@ss.hasPermi('tron:config01:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iTronImageConfig01Service.getById(id));
    }

    /**
     * 新增图片配置01
     */
    @PreAuthorize("@ss.hasPermi('tron:config01:add')" )
    @Log(title = "图片配置01" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TronImageConfig01 tronImageConfig01) {
        SysUser sysUser= SecurityUtils.getLoginUser().getUser();
        if (sysUser.getRoles().get(0).getRoleKey().startsWith("admin")) { //只能有一个角色
            tronImageConfig01.setAgencyId(sysUser.getUserName()); //查询所有的代理
            tronImageConfig01.setSalemanId(sysUser.getUserName());
        }
        if (sysUser.getRoles().get(0).getRoleKey().startsWith("agent")) { //只能有一个角色
            tronImageConfig01.setAgencyId(sysUser.getUserName()); //查询当前的代理
            tronImageConfig01.setSalemanId(sysUser.getUserName());
        }
        if (sysUser.getRoles().get(0).getRoleKey().startsWith("common")) {
            tronImageConfig01.setSalemanId(sysUser.getUserName());
            String agencyId=iTronAuthAddressService.queryAgent(sysUser.getDeptId());
            tronImageConfig01.setAgencyId(agencyId);
        }
        LambdaQueryWrapper<TronImageConfig01> lqw = Wrappers.lambdaQuery();
        lqw.eq(TronImageConfig01::getSalemanId ,tronImageConfig01.getSalemanId());
        TronImageConfig01 config02=iTronImageConfig01Service.getOne(lqw);
        if (config02==null){
            return toAjax(iTronImageConfig01Service.save(tronImageConfig01) ? 1 : 0);
        } else{
            tronImageConfig01.setId(config02.getId());
            return toAjax(iTronImageConfig01Service.updateById(tronImageConfig01) ? 1 : 0);
        }
    }

    /**
     * 修改图片配置01
     */
    @PreAuthorize("@ss.hasPermi('tron:config01:edit')" )
    @Log(title = "图片配置01" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TronImageConfig01 tronImageConfig01) {
        return toAjax(iTronImageConfig01Service.updateById(tronImageConfig01) ? 1 : 0);
    }

    /**
     * 删除图片配置01
     */
    @PreAuthorize("@ss.hasPermi('tron:config01:remove')" )
    @Log(title = "图片配置01" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iTronImageConfig01Service.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
