package com.ruoyi.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.pay.domain.SystemConfig;
import com.ruoyi.pay.mapper.ConfigMapper;
import com.ruoyi.pay.service.ISysConfigService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 参数配置Service业务层处理
 *
 * @author doctor
 * @date 2022-09-12
 */
@Service("configServiceImpl")
public class SysConfigServiceImpl extends ServiceImpl<ConfigMapper, SystemConfig> implements ISysConfigService {

    @Override
    @Cacheable("api:mbpay-pay-timeout")
    public Integer getPayTimeOut() {
        LambdaQueryWrapper<SystemConfig> lqw = Wrappers.lambdaQuery();
        lqw.eq(SystemConfig::getConfigKey, "mbpay-pay-timeout");
        SystemConfig systemConfig = this.getOne(lqw);
        if (systemConfig == null) {
            return 30;//默认30分钟
        }
        return Integer.parseInt(systemConfig.getConfigValue());
    }

    @Override
    @Cacheable("api:mbpay-rate-usdt")
    public Double getPayRate() {
        LambdaQueryWrapper<SystemConfig> lqw = Wrappers.lambdaQuery();
        lqw.eq(SystemConfig::getConfigKey, "mbpay-rate-usdt");
        SystemConfig systemConfig = this.getOne(lqw);
        if (systemConfig == null) {
            return 0.9;//默认0.9
        }
        return Double.parseDouble(systemConfig.getConfigValue());
    }

    @Override
    @Cacheable("api:mbpay-cash-url")
    public String getCashierUrl() {
        LambdaQueryWrapper<SystemConfig> lqw = Wrappers.lambdaQuery();
        lqw.eq(SystemConfig::getConfigKey, "mbpay-cash-url");
        SystemConfig systemConfig = this.getOne(lqw);
        if (systemConfig == null) {
            return "http://52.52.144.209:85/";//默认209服务器
        }
        return systemConfig.getConfigValue();
    }

    @Override
    @Cacheable("api:mbpay-daip-limit")
    public Integer getDaipLimit() {
        LambdaQueryWrapper<SystemConfig> lqw = Wrappers.lambdaQuery();
        lqw.eq(SystemConfig::getConfigKey, "mbpay-daip-limit");
        SystemConfig systemConfig = this.getOne(lqw);
        if (systemConfig == null) {
            return 1000;//默认1000个U
        }
        return Integer.parseInt(systemConfig.getConfigValue());
    }

}
