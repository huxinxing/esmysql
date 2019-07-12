package com.zhifan.monitor_manager.util;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class GoogleAuthenticatorUtil {

    public static final int SECRET_SIZE = 10;
    public static final String SEED = "g8GjEvTbW5oVSV7avLBdwIHqGlUYNzKFI7izOF8GwLDVKs2m0QN7vxRs2im5MDaNCWGmcD2rvcZx";
    public static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";
    public static final String codes= "676526";
    public static final String savedSecret= "5ODF2DGKUSMBZTHY";

    public static final String secret="BJU76BK25JVVDXMY";//私钥

    public static int  window_size = 0;


    /**
     * 生成googleCode的私钥
     * @return  googleCode的私钥
     */
    public static String generateSecretKey() {
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstance(RANDOM_NUMBER_ALGORITHM);
            sr.setSeed(Base64.decodeBase64(SEED));
            byte[] buffer = sr.generateSeed(SECRET_SIZE);
            Base32 codec = new Base32();
            byte[] bEncodedKey = codec.encode(buffer);
            String encodedKey = new String(bEncodedKey);
            return encodedKey;
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * GoogleCode用于生成二维码
     * @param user 用户名
     * @param host 主机
     * @param secret googleCode
     * @return  字符串
     */
    public static String getQRBarcodeURL(String user, String host, String secret) {
        String format = "https://www.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s@%s%%3Fsecret%%3D%s";
        return String.format(format, user, host, secret);
    }


    /**
     * 验证google验证码是否正确
     * @param code googleCode验证码
     * @param timeMsec  //系统时间
     * @param googleSecret  //google验证吗私钥
     * @return true|false
     */
    public static boolean  check_code(long code, long timeMsec, String googleSecret) { //secret写死。code前台传。timeMsec系统时间 精确到s
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(googleSecret);
        long t = (timeMsec / 1000L) / 30L;
        for (int i = -window_size; i <= window_size; ++i) {
            long hash;
            try {
                hash = verify_code(decodedKey, t + i);
                System.out.println("hash="+hash);
                System.out.println("code="+code);
            }catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
            if (hash == code) {
                return true;
            }
        }
        return false;
    }

    private static int verify_code(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
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



}
