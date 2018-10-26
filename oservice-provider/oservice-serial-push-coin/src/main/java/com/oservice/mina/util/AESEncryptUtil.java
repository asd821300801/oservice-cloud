package com.oservice.mina.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class AESEncryptUtil {
    private static String encryptPass= "SDF9JK09765S";

	public static byte[] encryptAES(byte[] content, String password) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(password.getBytes());
        kgen.init(128, secureRandom);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        
        SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] result = cipher.doFinal(content);
        return result;
    }

    public static byte[] decryptAES(byte[] content, String password) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
        secureRandom.setSeed(password.getBytes());
        kgen.init(128, secureRandom);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        
        SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] result = cipher.doFinal(content);
        return result;
    }

    public static String encryptByteStr(String src) {
        byte[] srcByte= src.getBytes();
        byte[] retDest= new byte[0];
        try {
            retDest = encryptAES(srcByte, encryptPass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.toJSON(retDest).toString();
    }

    public static String decryptByteStr(String encrypt) {
        Object[] jsonArr= JSONArray.parseArray(encrypt).toArray();
        byte[] entryptByte= new byte[jsonArr.length];
        for (int i=0; i<jsonArr.length; i++){
            byte bt= Byte.valueOf(jsonArr[i].toString());
            entryptByte[i]= bt;
        }

        byte[] decryptByte= new byte[0];//byte[] retSrc2= decryptAES(destByte, encryptPass);
        String decrypt= "";
        try {
            decryptByte = decryptAES(entryptByte, encryptPass);
            decrypt= new String(decryptByte,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decrypt;
    }

}
