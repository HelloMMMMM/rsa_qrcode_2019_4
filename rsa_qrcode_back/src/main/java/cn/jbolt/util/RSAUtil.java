package cn.jbolt.util;



import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

public class RSAUtil {
    private static final String RSA_ALGORITHM = "RSA";
    private static final String RSA_PUBLIC_KEY = "RSAPublicKey";
    private static final String RSA_PRIVATE_KEY = "RSAPrivateKey";
    /**
     * RSA最大加密明文大小,计算方式（密钥长度-11）,单位字节,这里密钥1024bit，128byte，128-11=117
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小，同密钥长度，这里1024bit，128byte
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 对字节数组base64编码
     */
    public static String encodeBase64(byte[] binaryData) {
        return Base64.getEncoder().encodeToString(binaryData);
    }

    /**
     * 对base64编码字符串进行解码为字节数组
     */
    public static byte[] decodeBase64(String encoded) {
        return Base64.getDecoder().decode(encoded);
    }

    /**
     * 获取RSA算法私钥、公钥
     */
    public static Map<String, String> getKey() {
        Map<String, String> keyMap = new HashMap<>(2);
        KeyPairGenerator keyPairGen = null;
        try {
            //获取密钥生成实例
            keyPairGen = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            //1024代表密钥二进制位数
            keyPairGen.initialize(1024);
            //生成公钥和私钥
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            //存入map
            keyMap.put(RSA_PUBLIC_KEY, encodeBase64(publicKey.getEncoded()));
            keyMap.put(RSA_PRIVATE_KEY, encodeBase64(privateKey.getEncoded()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keyMap;
    }

    /**
     * 获取公钥
     */
    public static String getPublicKey(Map<String, String> map) {
        return map.get(RSA_PUBLIC_KEY);
    }

    /**
     * 获取私钥
     */
    public static String getPrivateKey(Map<String, String> map) {
        return map.get(RSA_PRIVATE_KEY);
    }


    /**
     * 使用公钥对数据进行加密（一般使用公钥加密）
     */
    public static String encryptPublicKey(String data, String publicKey) throws Exception {
        //将公钥解码为字节数组
        byte[] keyBytes = decodeBase64(publicKey);
        //加密初始化相关实例
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        Key pubKey = keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        //分段加密，因为明文长度有限制，需分段
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        //加密后的字节数组
        byte[] encryptedData = out.toByteArray();
        out.close();
        // 编码为base64
        return encodeBase64(encryptedData);
    }

    /**
     * 使用私钥对数据进行解密(一般使用私钥进行解密)
     */
    public static String decryptPrivateKey(String data, String privateKey) throws Exception {
        //将密文解码为字节数组
        byte[] binaryData = decodeBase64(data);
        //将私钥解码为字节数组
        byte[] keyBytes = decodeBase64(privateKey);
        //解密初始化相关实例
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        Key priKey = keyFactory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        //分段加密，因为密文长度有限制，需分段
        int inputLen = binaryData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(binaryData, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(binaryData, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        // 解密后的内容
        return new String(decryptedData);
    }


    /**
     * 使用私钥对数据进行加密
     */
    public static String encryptPrivateKey(String data, String privateKey) throws Exception {
        byte[] keyBytes = decodeBase64(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        Key priKey = keyFactory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, priKey);

        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串

        return encodeBase64(encryptedData);
    }

    /**
     * 使用公钥对数据进行解密
     */
    public static String decryptPublicKey(String data, String publicKey) throws Exception {
        byte[] binaryData = decodeBase64(data);
        byte[] keyBytes = decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        Key pubKey = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pubKey);

        int inputLen = binaryData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(binaryData, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(binaryData, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        // 解密后的内容
        return new String(decryptedData);
    }
}
