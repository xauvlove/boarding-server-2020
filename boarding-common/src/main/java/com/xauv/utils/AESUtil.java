package com.xauv.utils;


import com.xauv.exception.AESEncodeException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.MessageFormat;

/**
 * AES加解密
 *
 * @author zifangsky
 * @date 2018/8/14
 * @since 1.0.0
 */
public class AESUtil {
    /**
     * AES加解密
     */
    private static final String ALGORITHM = "AES";
    /**
     * 默认的初始化向量值
     */
    private static final String IV = "~b!o@a#r$d%i^n&g";
    /**
     * 默认加密的KEY
     */
    private static final String KEY = "boardingxauvlove";
    /**
     * 工作模式：CBC
     */
    private static final String TRANSFORM_CBC_PKCS5 = "AES/CBC/PKCS5Padding";

    /**
     * 工作模式：ECB
     */
    private static final String TRANSFORM_ECB_PKCS5 = "AES/ECB/PKCS5Padding";

    /**
     * 基于CBC工作模式的AES加密
     * @param value 待加密字符串
     * @return java.lang.String
     */
    public static String encryptCbcMode(final String value) throws Exception {
        //密码
        final SecretKeySpec keySpec = getSecretKey(KEY);
        //初始化向量器
        final IvParameterSpec ivParameterSpec = new IvParameterSpec(getUTF8Bytes(IV));
        Cipher encipher = Cipher.getInstance(TRANSFORM_CBC_PKCS5);
        //加密模式
        encipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
        //使用AES加密
        byte[] encrypted = encipher.doFinal(getUTF8Bytes(value));
        //然后转成BASE64返回
        return Base64.encodeBase64String(encrypted);
    }

    /**
     * 基于CBC工作模式的AES解密
     * @param encryptedStr AES加密之后的字符串
     * @return java.lang.String
     */
    public static String decryptCbcMode(final String encryptedStr) throws Exception {
        final SecretKeySpec keySpec = getSecretKey(KEY);
        final IvParameterSpec ivParameterSpec = new IvParameterSpec(getUTF8Bytes(IV));
        Cipher encipher = Cipher.getInstance(TRANSFORM_CBC_PKCS5);
        //加密模式
        encipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
        //先用BASE64解密
        byte[] encryptedBytes = Base64.decodeBase64(encryptedStr);
        //然后再AES解密
        byte[] originalBytes = encipher.doFinal(encryptedBytes);
        //返回字符串
        return new String(originalBytes);
    }


    /**
     * 基于ECB工作模式的AES加密
     * @param value 待加密字符串
     * @return java.lang.String
     */
    public static String encryptEcbMode(final String value) throws AESEncodeException {
        try {
            //密码
            final SecretKeySpec keySpec = getSecretKey(KEY);
            Cipher encipher = Cipher.getInstance(TRANSFORM_ECB_PKCS5);
            //加密模式
            encipher.init(Cipher.ENCRYPT_MODE, keySpec);
            //使用AES加密
            byte[] encrypted = encipher.doFinal(getUTF8Bytes(value));
            //然后转成BASE64返回
            return Base64.encodeBase64String(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AESEncodeException();
        }
    }

    /**
     * 基于ECB工作模式的AES解密
     * @param encryptedStr AES加密之后的字符串
     * @return java.lang.String
     */
    public static String decryptEcbMode(final String encryptedStr) throws AESEncodeException {
        try {
            //密码
            final SecretKeySpec keySpec = getSecretKey(KEY);
            Cipher encipher = Cipher.getInstance(TRANSFORM_ECB_PKCS5);
            //加密模式
            encipher.init(Cipher.DECRYPT_MODE, keySpec);
            //先用BASE64解密
            byte[] encryptedBytes = Base64.decodeBase64(encryptedStr);
            //然后再AES解密
            byte[] originalBytes = encipher.doFinal(encryptedBytes);
            //返回字符串
            return new String(originalBytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AESEncodeException();
        }
    }

    /**
     * 将字符串转化为UTF8格式的byte数组
     *
     * @param input the input string
     * @return UTF8 bytes
     */
    private static byte[] getUTF8Bytes(String input) {
        return input.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 生成加密秘钥
     * @param KEY 明文秘钥
     * @return SecretKeySpec
     */
    private static SecretKeySpec getSecretKey(final String KEY) {
        //生成指定算法密钥
        KeyGenerator generator = null;

        try {
            generator = KeyGenerator.getInstance(ALGORITHM);

            //指定AES密钥长度（128、192、256）
            generator.init(128, new SecureRandom(getUTF8Bytes(KEY)));

            //生成一个密钥
            SecretKey secretKey = generator.generateKey();
            //转换为AES专用密钥
            return new SecretKeySpec(secretKey.getEncoded(), ALGORITHM);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(MessageFormat.format("生成加密秘钥失败,KEY:{0}",KEY));
            ex.printStackTrace();
        }

        return null;
    }

    /*public static void main(String[] args) throws Exception {

        String encryptedStr2 = AESUtil.encryptEcbMode("hello world");
        System.out.println(encryptedStr2);
        String originalStr2 = AESUtil.decryptEcbMode(encryptedStr2);
        System.out.println(originalStr2);

    }*/

}