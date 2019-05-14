package com.hellom.rsaandqrcode.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyUtil {
    public static Map<String, String> keys = initKeys();

    private static final String RSA_PUBLIC_KEY = "RSAPublicKey";
    private static final String RSA_PRIVATE_KEY = "RSAPrivateKey";

    private static final String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMvjkJD809" +
            "olP1nZ6/hfwJIIqRWj17kXF6uaiV7AwFoeDMob6ANTUk3GDJiLDjXFd4zXWVFZ5J+1/34AzCdGh0C" +
            "xamynv86JP6k4EOtPdmfEdHJuA6Y7oO3/+baZt3RuTt2f+2mu8HTBqmTXjJVpX7vCvXbZ/eGCYf" +
            "8AcdHOGm4vAgMBAAECgYEAvEMaH+Z0L7+o6WxhiOvRQoagqgypioyaHKgLI+TTN+L7aDuwVjG/k3TF+Ky" +
            "dqGT3aKMIJCffBOHmUhCmcbWb+CbM2+pvdvlTFTsjsVDMVajcMzwRRGdh7VtEhaNPKFoPRhGkyTXbL6i9" +
            "YOjSvFlUgd5yIRDClb21jbw2S3w3L5ECQQD5gG48sKVhc+4/eNNBAm40t7rgIyuKpSirNbDsY4NgaKG0j" +
            "6WTVtUkSEHNUBE/uHdvbk4899uc/cCykebpSSVDAkEA0TMCCLIrh5S1A4E0l8QDN/2CgxH7UaUrvqMJtq6" +
            "m33NrdQ745pusTm5FbrgOgX2GQb6XExIg3jYXfslnjclOpQJBANLkBsnPugp0dmY6F750cnim8BNyRuSLE6Q" +
            "b3u8AclS0PDKl1JuEouZnGQ+U5O+KWZHbl9Aa5Qt+xTlq79YyGIcCQETUrpej37SlVgWILoC8yn5Sl+1+Lh2" +
            "H5eO8iwajYl5lAHcsbPKbR88qcwfh4UvzvAfHqLdqoIvcalCZ/ctl780CQQCdkiET+fj9It66/8v4L65A7Nd+X" +
            "hkTwOtFwL6FR5hW53Bg38QlMy6wvwL6G8OpWlJ+8ZRkiOQdc4W7p50Ss2fT";

    private static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDL45CQ/NPaJT9Z2ev" +
            "4X8CSCKkVo9e5FxermolewMBaHgzKG+gDU1JNxgyYiw41xXeM11lRWeSftf9+AMwnRodAsWpsp7/OiT+pOBDrT3" +
            "ZnxHRybgOmO6Dt//m2mbd0bk7dn/tprvB0wapk14yVaV+7wr122f3hgmH/AHHRzhpuLwIDAQAB";

    private static Map<String, String> initKeys() {
        Map<String, String> keys = new HashMap<>(2);
        keys.put(RSA_PUBLIC_KEY, publicKey);
        keys.put(RSA_PRIVATE_KEY, privateKey);
        return keys;
    }

    private static List<String> targetCodes = new ArrayList<>();

    private static void initTargetCodes() {
        //测试的防伪码格式为 TestCode_1  ~  TestCode_100  共100条
        for (int i = 1; i <= 100; i++) {
            targetCodes.add("TestCode_" + i);
        }
    }

    public static boolean containerThisCode(String code) {
        if (targetCodes.size() == 0) {
            initTargetCodes();
        }
        for (int i = 0; i < targetCodes.size(); i++) {
            if (code.equals(targetCodes.get(i))) {
                return true;
            }
        }
        return false;
    }
}
