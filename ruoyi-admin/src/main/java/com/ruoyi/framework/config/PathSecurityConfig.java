package com.ruoyi.framework.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public class PathSecurityConfig extends SecurityConfig {
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        super.configure(httpSecurity);
        httpSecurity.authorizeRequests()
                .antMatchers("/api/**", "/test/**").anonymous();

    }
}
