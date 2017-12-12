package com.bixian365.dzc.utils.dncry.wsc;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 ****************************************************** 
 * @Description：MD5加密
 ****************************************************** 
 */
public class MD5Utils {

	/**
	 * MD5 加密
	 * @param val  需要加密的字符串
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String encrypt(String val) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		digest.update(val.getBytes("UTF-8"));
		byte[] buffer = digest.digest();
		StringBuilder sb = new StringBuilder();
		for (byte b : buffer) {
			if ((b & 0xFF) < 0x10)
				sb.append("0");
			sb.append(Integer.toHexString(b & 0xFF));
		}
		return sb.toString();
	}
}
