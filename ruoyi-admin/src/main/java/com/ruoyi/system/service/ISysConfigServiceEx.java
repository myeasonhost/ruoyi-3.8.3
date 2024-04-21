package com.ruoyi.system.service;

public interface ISysConfigServiceEx {
    /**
     * 获取谷歌验证码开关
     *
     * @return true开启，false关闭
     */
    public boolean selectGoogleEnabled();
}
