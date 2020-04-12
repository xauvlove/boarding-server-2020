package com.xauv.utils;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;

/**
 * DES是一种对称加密算法，所谓对称加密算法:加密和解密使用相同的秘钥的算法
 * @author llp
 *
 */
@Slf4j
public class DESUtil {

    private static Key key;
    //设置秘钥key
    private static String KEY = "boarding-xauvlove";
    private static String CHARSET = "UTF-8";
    private static String ALGORITHM="DES";

    static{
        try{
            //生成DES算法对象
            KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
            //运用SHA1安全策略
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            //设置上密钥种子
            secureRandom.setSeed(KEY.getBytes());
            //初始化基于SHA1的算法对象
            generator.init(secureRandom);
            //生成密钥对象
            key=generator.generateKey();
            generator=null;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取加密的信息
     * @param str
     * @return
     */
    public static String getEncryptString(String str){
        //基于BASE64编码，接收byte[]并转换成String
        BASE64Encoder base64Encoder=new BASE64Encoder();
        try {
            // 按UTF8编码
            byte[] bytes = str.getBytes(CHARSET);
            // 获取加密对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // 初始化密码信息
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 加密
            byte[] doFinal = cipher.doFinal(bytes);
            // byte[]to encode好的String并返回
            return base64Encoder.encode(doFinal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取解密之后的信息
     *
     * @param str
     * @return
     */
    public static String getDecryptString(String str) {
        // 基于BASE64编码，接收byte[]并转换成String
        BASE64Decoder base64decoder = new BASE64Decoder();
        try {
            // 将字符串decode成byte[]
            byte[] bytes = base64decoder.decodeBuffer(str);
            // 获取解密对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // 初始化解密信息
            cipher.init(Cipher.DECRYPT_MODE, key);
            // 解密
            byte[] doFinal = cipher.doFinal(bytes);
            // 返回解密之后的信息
            return new String(doFinal, CHARSET);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

/*    public static void main(String[] args) {
        //加密
        log.info(getEncryptString("root"));//WnplV/ietfQ=
        log.info(getEncryptString("123456"));//QAHlVoUc49w=
        //解密
        log.info(getDecryptString(getEncryptString("root")));//root
        log.info(getDecryptString(getEncryptString("123456")));//123456
    }*/
}