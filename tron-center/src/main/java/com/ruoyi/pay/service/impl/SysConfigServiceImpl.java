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

}
