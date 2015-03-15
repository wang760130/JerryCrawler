package com.jerry.crawler.chap1;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	public static String getMD5(byte[] source) {
		String s = null;
		// 用来将字节转换成十六进制表示的字符
		char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source);
			// MD5的计算结果是一个128位的长整型，用字节表示就是16格字节
			byte tmp[] = md.digest();
			// 每个字节用十六进制表示的话，使用两个字符，所以表示成十六进制需要32个字符
			char str[] = new char[16 * 2];
			// 白哦是转换结果中对应的字符位置
			int k = 0;
			for(int i = 0; i < 16; i++) {
				// 从第一个字符开始，将MD5的每一个字节转换成十六进制字符
				// 取第i个字符
				byte byte0 = tmp[i];
				// 取字节中高4位的数字转换
				// >>>为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				// 取字节中低4为的数据转换
				str[k++] = hexDigits[byte0 & 0xf];
			}
			s = new String(str);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return s;
	}
}
