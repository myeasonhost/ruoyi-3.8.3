package com.ruoyi.tron.config;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.ruoyi.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class EncryptHandler extends BaseTypeHandler<String> {
    /**
     * 线上运行后勿修改，会影响已加密数据解密
     */
    private static final byte[] KEYS = "shc987654321camp".getBytes(StandardCharsets.UTF_8);

    /**
     * 设置参数
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        if (StringUtils.isEmpty(parameter)) {
            ps.setString(i, null);
            return;
        }
        SecureUtil.disableBouncyCastle();
        AES aes = SecureUtil.aes(KEYS);
        String encrypt = aes.encryptHex(parameter);
        ps.setString(i, encrypt);
    }

    /**
     * 获取值
     */
    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return decrypt(rs.getString(columnName));
    }

    /**
     * 获取值
     */
    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return decrypt(rs.getString(columnIndex));
    }

    /**
     * 获取值
     */
    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return decrypt(cs.getString(columnIndex));
    }

    public String decrypt(String value) {
        if (null == value) {
            return null;
        }
        SecureUtil.disableBouncyCastle();
        try {
            String decryptStr = SecureUtil.aes(KEYS).decryptStr(value);
            return decryptStr;
        } catch (Exception e) {
            log.error("【解密异常】密文value={}，错误ERROR={}", value, e.getMessage());
            return value;
        }
    }

    public static void main(String[] args) {
        String content = "TXNuNhajxXGELDgGsbBKZy2d16qS7DQN1j";
        SecureUtil.disableBouncyCastle();
        // 随机生成密钥
        byte[] key = "shc987654321camp".getBytes(StandardCharsets.UTF_8);
        AES aes = SecureUtil.aes(key);
        byte[] encrypt = aes.encrypt(content);
        byte[] decrypt = aes.decrypt(encrypt);
        String encryptHex = aes.encryptHex(content);
        System.out.println(encryptHex);
        String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
        System.out.println(decryptStr);

    }
}
