package com.ruoyi.tron.utils;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author ：eason
 * @description：google身份验证器
 * https://blog.csdn.net/weixin_34060299/article/details/92063943?spm=1001.2101.3001.6650.6&utm_medium=distribute.wap_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7Edefault-6.wap_blog_relevant_default&depth_1-utm_source=distribute.wap_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7Edefault-6.wap_blog_relevant_default
 */
public class GoogleAuthenticatorConfig {

    public static final int SECRET_SIZE = 10;

    public static final String SEED = "g8GjEvTbW5oVSV7avLBdwIHqGlUYNzKFI7izOF8GwLDVKs2m0QN7vxRs2im5MDaNCWGmcD2rvcZx";

    public static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";

    int window_size = 3; // default 3 - max 17 (from google docs)最多可偏移的时间

    public void setWindowSize(int s) {
        if (s >= 1 && s <= 17)
            window_size = s;
    }

    /**
     * 验证身份验证码是否正确
     *
     * @param codes       输入的身份验证码
     * @param savedSecret 密钥
     * @return
     */
    public static Boolean authCode(String codes, String savedSecret) {
        long code = 0;
        try {
            code = Long.parseLong(codes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long t = System.currentTimeMillis();
        GoogleAuthenticatorConfig ga = new GoogleAuthenticatorConfig();
        // should give 5 * 30 seconds of grace...
        ga.setWindowSize(3);
        boolean r = ga.checkCode(savedSecret, code, t);
        return r;
    }

    public static String genSecret() {
        String secret = GoogleAuthenticatorConfig.generateSecretKey();
        return secret;
    }

    private static String generateSecretKey() {
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstance(RANDOM_NUMBER_ALGORITHM);
            sr.setSeed(Base64.decodeBase64(SEED));
            byte[] buffer = sr.generateSeed(SECRET_SIZE);
            Base32 codec = new Base32();
            byte[] bEncodedKey = codec.encode(buffer);
            String encodedKey = new String(bEncodedKey);
            return encodedKey;
        } catch (NoSuchAlgorithmException e) {
            // should never occur... configuration error
        }
        return null;
    }

    /**
     * 获取二维码图片URL
     *
     * @param user   用户
     * @param host   域
     * @param secret 密钥
     * @return 二维码URL
     */
    public static String getQRBarcodeURL(String user, String host, String secret) {
        String format = "https://www.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s@%s%%3Fsecret%%3D%s";
        return String.format(format, user, host, secret);
    }

    private boolean  checkCode(String secret, long code, long timeMsec) {
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(secret);
        long t = (timeMsec / 1000L) / 30L;
        for (int i = -window_size; i <= window_size; ++i) {
            long hash;
            try {
                hash = verifyCode(decodedKey, t + i);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
            if (hash == code) {
                return true;
            }
        }
        return false;
    }

    private static int verifyCode(byte[] key, long t)
            throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = new byte[8];
        long value = t;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }
        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);
        int offset = hash[20 - 1] & 0xF;
        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            truncatedHash |= (hash[offset + i] & 0xFF);
        }
        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;
        return (int) truncatedHash;
    }


    public static void main(String[] args) throws Exception {
        /*
         * 注意：先运行前两步，获取密钥和二维码url。 然后只运行第三步，填写需要验证的验证码，和第一步生成的密钥
         */
        // 第一步：获取密钥 QVLBR26DPJU45F6L
        String secret = genSecret();
        System.out.println("secret:" + secret);
        // 第二步：根据密钥获取二维码图片url（可忽略）
        String url = getQRBarcodeURL("gdboss", "http://gdboss", "QVLBR26DPJU45F6L");
        System.out.println("url:" + url);
        // 第三步：验证（第一个参数是需要验证的验证码，第二个参数是第一步生成的secret运行）
        boolean result = authCode("429984", "VXVRLWA2DRNPVTA4");
        System.out.println("result:" + result);
    }

}
