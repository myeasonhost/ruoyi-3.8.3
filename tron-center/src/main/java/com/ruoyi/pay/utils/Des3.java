package com.ruoyi.pay.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Random;


@Slf4j
public final class Des3 {

    private String key = "09876543210987654321098765432109";

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    // 解密数据
    public byte[] decrypt(byte[] message) {

        byte[] keyBytes = null;

        try {
            keyBytes = getKeyByte(key);
        } catch (UnsupportedEncodingException e1) {

            e1.printStackTrace();
        }

        try {
            return des3DecodeECB(keyBytes, message);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    public byte[] encrypt(byte[] byteArray) {

        byte[] keyBytes = null;

        try {
            keyBytes = getKeyByte(key);
        } catch (UnsupportedEncodingException e1) {

            e1.printStackTrace();
        }


        try {
            return des3EncodeECB(keyBytes, byteArray);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;

    }

    public static byte[] convertHexString(String ss) {
        byte digest[] = new byte[ss.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }

        return digest;
    }

    public static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }

        return hexString.toString();
    }

    private static byte[] getKeyByte(String key) throws UnsupportedEncodingException {
        // 加密数据必须是24位，不足补0；超出24位则只取前面的24数据
        byte[] data = key.getBytes();
        int len = data.length;
        byte[] newdata = new byte[24];
        System.arraycopy(data, 0, newdata, 0, len > 24 ? 24 : len);
        return newdata;
    }

    public static byte[] des3EncodeECB(byte[] key, byte[] data) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }

    public static byte[] des3DecodeECB(byte[] key, byte[] data) throws Exception {

        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, deskey);
        byte[] bOut = cipher.doFinal(data);
        return bOut;

    }

    public static byte[] des3EncodeCBC(byte[] key, byte[] keyiv, byte[] data) throws Exception {

        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(data);

        return bOut;
    }

    public static byte[] des3DecodeCBC(byte[] key, byte[] keyiv, byte[] data) throws Exception {

        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv);

        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

        byte[] bOut = cipher.doFinal(data);

        return bOut;

    }

    private static final String sStr = "0123456789abcdefghijklmnopqrstuvwxyz";


    private static String getRndString() {
        String rStr = "";
        Random rn = new Random();
        for (int j = 0; j < 8; ++j) {
            int rNum = rn.nextInt(36);
            rStr = rStr + sStr.charAt(rNum);
        }
        return rStr;
    }

}
