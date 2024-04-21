package com.ruoyi.tron.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.tron.domain.OrgAccountInfo;
import com.ruoyi.tron.mapper.OrgAccountInfoMapper;
import com.ruoyi.tron.service.IOrgAccountInfoService;
import com.ruoyi.tron.utils.GoogleAuthenticatorConfig;
import com.ruoyi.tron.utils.IpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 商户信息Service业务层处理
 *
 * @author doctor
 * @date 2022-07-20
 */
@Service
public class OrgAccountInfoServiceImpl extends ServiceImpl<OrgAccountInfoMapper, OrgAccountInfo> implements IOrgAccountInfoService {

    @Override
    public List<OrgAccountInfo> queryList(OrgAccountInfo orgAccountInfo) {
        LambdaQueryWrapper<OrgAccountInfo> lqw = Wrappers.lambdaQuery();
        if (orgAccountInfo.getUserId() != null) {
            lqw.eq(OrgAccountInfo::getUserId, orgAccountInfo.getUserId());
        }
        if (StringUtils.isNotBlank(orgAccountInfo.getAgencyId())) {
            lqw.eq(OrgAccountInfo::getAgencyId, orgAccountInfo.getAgencyId());
        }
        return this.list(lqw);
    }

    @Override
    public R<String> whiteIpAndGoogleCodeLogin(String username, String googleCode, HttpServletRequest request) {
        String visitedIp = IpUtil.getIpAddress(request);
        LambdaQueryWrapper<OrgAccountInfo> lqw = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(username)) {
            lqw.eq(OrgAccountInfo::getAgencyId, username);
        }
        OrgAccountInfo orgAccountInfo = this.baseMapper.selectOne(lqw);
        if (orgAccountInfo == null) {
            return R.fail("商户不存在，请联系客服申请" );
        }
        boolean result = GoogleAuthenticatorConfig.authCode(googleCode, orgAccountInfo.getGoogleSecretCode());
        if (!result) {
            return R.fail("谷歌验证码错误" );
        }
        if (StringUtils.isEmpty(orgAccountInfo.getWhiteIp()) || !orgAccountInfo.getWhiteIp().contains(visitedIp + "," )) {
            log.error(orgAccountInfo.getWhiteIp() + "不在白名单内【" + visitedIp + "】" );
            return R.fail("白名单错误" );
        }
        return R.ok();
    }

}
