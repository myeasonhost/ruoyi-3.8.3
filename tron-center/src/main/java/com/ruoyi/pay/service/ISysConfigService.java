package com.ruoyi.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.pay.domain.SystemConfig;

/**
 * 参数配置Service接口
 *
 * @author doctor
 * @date 2022-09-12
 */
public interface ISysConfigService extends IService<SystemConfig> {

    Integer getPayTimeOut();

}
