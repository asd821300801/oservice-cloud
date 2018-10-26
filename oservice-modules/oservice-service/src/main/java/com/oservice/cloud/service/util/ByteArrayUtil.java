package com.oservice.cloud.service.util;

import java.util.ArrayList;
import java.util.List;

public class ByteArrayUtil {
	/**
	 * byte[]与十六进制字符串转换
	 * @param bytes
	 * @return
	 */
    public static List<String> bytesToHexs(byte[] bytes) {
        List<String> hexs = new ArrayList<>();
        for(byte b : bytes) { // 使用String的format方法进行转换
        	hexs.add(String.format("%02x", b & 0xff));
        }
        return hexs;
    }
	/**
	 * 将16进制字符串转换为byte[]
	 *
	 * @param str
	 * @return
	 */
	public static byte[] toBytes(String str) {
		if(str == null || str.trim().equals("")) {
			return new byte[0];
		}

		byte[] bytes = new byte[str.length() / 2];
		for(int i = 0; i < str.length() / 2; i++) {
			String subStr = str.substring(i * 2, i * 2 + 2);
			bytes[i] = (byte) Integer.parseInt(subStr, 16);
		}

		return bytes;
	}
}
