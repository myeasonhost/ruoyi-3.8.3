package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysConfigServiceEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysConfigServiceImplEx implements ISysConfigServiceEx {

    @Autowired
    private ISysConfigService sysConfigService;
    /**
     * 获取谷歌验证码开关
     *
     * @return true开启，false关闭
     */
    @Override
    public boolean selectGoogleEnabled() {
        String googleEnabled = sysConfigService.selectConfigByKey("sys.account.googleEnabled" );
        if (StringUtils.isEmpty(googleEnabled)) {
            return true;
        }
        return Convert.toBool(googleEnabled);
    }
}
