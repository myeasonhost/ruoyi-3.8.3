package com.ruoyi.tron.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.tron.domain.TronWebConfig;
import com.ruoyi.tron.mapper.TronWebConfigMapper;
import com.ruoyi.tron.service.ITronWebConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 矿机设置Service业务层处理
 *
 * @author eason
 * @date 2022-05-24
 */
@Service
public class TronWebConfigServiceImpl extends ServiceImpl<TronWebConfigMapper, TronWebConfig> implements ITronWebConfigService {

    @Override
    public List<TronWebConfig> queryList(TronWebConfig tronWebConfig) {
        LambdaQueryWrapper<TronWebConfig> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(tronWebConfig.getAgencyId())){
            lqw.eq(TronWebConfig::getAgencyId ,tronWebConfig.getAgencyId());
        }
        return this.list(lqw);
    }
}
