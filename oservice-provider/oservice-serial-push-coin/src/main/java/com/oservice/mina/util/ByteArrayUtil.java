package com.oservice.mina.util;

import java.util.ArrayList;
import java.util.List;

public class ByteArrayUtil {
	
	/**
	 * 连接到下位机(娃娃机 推币机)
	 * @return
	 */
	public static byte[] getCon(){
		return new byte[]{(byte)0x8a, (byte)0x03, (byte)0x01, (byte)0x01, (byte)0x05, (byte)0x55};
	}

	/**
	 * 连接到下位机(骰子机)
	 * @return
	 */
	public static byte[] getDice(){
		return new byte[]{(byte)0x7a, (byte)0x03, (byte)0x02, (byte)0x01, (byte)0x06, (byte)0x55};
	}


	/**
	 * 恢复出厂设置
	 * @return
	 */
	public static byte[] getFactoryReset(){
		return new byte[]{(byte)0x8a, (byte)0x05, (byte)0x14, (byte)0x01, (byte)0x01, (byte)0x00, (byte)0x1b, (byte)0x55};
	}
	
	/**
	 * 设备码
	 * @return
	 */
	public static String getArrayStr(List<String> array){
		StringBuilder sb = new StringBuilder();
		for(String str : array){
			sb.append(str);
		}
    	return sb.toString();
	}
	
	
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
}
