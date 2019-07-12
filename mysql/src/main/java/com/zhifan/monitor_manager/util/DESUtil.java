package com.zhifan.monitor_manager.util;

import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

public class DESUtil {


    private static String password = "9588888888880288";
    //测试
    public static void main(String args[]) {
        //待加密内容
        DESUtil desUtil = new DESUtil();
        String str = "huxinxing";

        String result = desUtil.encrypt(str,password);

        BASE64Encoder base64en = new BASE64Encoder();
//        String strs = new String(base64en.encode(result));

        System.out.println("加密后："+result);
        //直接将如上内容解密
        try {
            String decryResult = desUtil.decryptor(result,password);
            System.out.println("解密后："+new String(decryResult));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    /**
     *
     * @Method: encrypt
     * @Description: 加密数据
     * @param data
     * @return
     * @throws Exception
     * @date 2016年7月26日
     */
    public static String encrypt(String data,String secretKey) {  //对string进行BASE64Encoder转换
        byte[] bt = encryptByKey(data.getBytes(), secretKey);
        BASE64Encoder base64en = new BASE64Encoder();
        String strs = new String(base64en.encode(bt));
        return strs;
    }

    /**
     *
     * @Method: encrypt
     * @Description: 解密数据
     * @param data
     * @return
     * @throws Exception
     * @date 2016年7月26日
     */
    public static String decryptor(String data,String secretKey) throws Exception {  //对string进行BASE64Encoder转换
        sun.misc.BASE64Decoder base64en = new sun.misc.BASE64Decoder();
        byte[] bt = decrypt(base64en.decodeBuffer(data), secretKey);
        String strs = new String(bt);
        return strs;
    }


    /**
     * 加密
     * @param datasource byte[]
     * @param key String
     * @return byte[]
     */
    private static byte[] encryptByKey(byte[] datasource, String key) {
        try{
//            SecureRandom random = new SecureRandom();
//
//            DESKeySpec desKey = new DESKeySpec(key.getBytes());
//            //创建一个密匙工厂，然后用它把DESKeySpec转换成
//            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//            SecretKey securekey = keyFactory.generateSecret(desKey);
//            //Cipher对象实际完成加密操作
//            Cipher cipher = Cipher.getInstance("DES");
//            //用密匙初始化Cipher对象
//            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            Cipher cipher = (Cipher) generateCipher(key,1);
            //现在，获取数据并加密
            //正式执行加密操作
            return cipher.doFinal(datasource);
        }catch(Throwable e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 解密
     * @param src byte[]
     * @param key String
     * @return byte[]
     * @throws Exception
     */
    private static byte[] decrypt(byte[] src, String key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        Cipher cipher = (Cipher) generateCipher(key,2);

        return cipher.doFinal(src);
    }


    /**
     * 创建des
     */
    private static Cipher generateCipher(String key,Integer type) throws Exception {
        SecureRandom random = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(key.getBytes());
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        if (type == 1){
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
        }else if (type == 2){
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        }

        return cipher;
    }

}
