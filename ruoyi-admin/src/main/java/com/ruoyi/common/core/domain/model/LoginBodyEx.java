package com.ruoyi.common.core.domain.model;

public class LoginBodyEx extends LoginBody {
    /**
     * 谷歌验证码
     */
    private String googleCode;


    public String getGoogleCode() {
        return googleCode;
    }

    public void setGoogleCode(String googleCode) {
        this.googleCode = googleCode;
    }
}
