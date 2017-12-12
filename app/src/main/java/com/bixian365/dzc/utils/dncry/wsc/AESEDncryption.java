package com.bixian365.dzc.utils.dncry.wsc;

import android.os.Bundle;

import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.httpClient.AppClient;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESEDncryption {

	private String iv ="";
	private String SecretKey = "";
	private IvParameterSpec ivspec;
	private SecretKeySpec keyspec;
	private Cipher cipher;
	private Bundle bundle;

	public AESEDncryption() {
		//测试时直接写死，生成时从bundle获取
		iv= AppClient.iv;
		SecretKey=AppClient.SecretKey;
		ivspec = new IvParameterSpec(iv.getBytes());
		keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");
		try {
			cipher = Cipher.getInstance("AES/CBC/NoPadding");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			Logs.i(e.toString()+"");
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			Logs.i(e.toString()+"");
		}
	}
	public AESEDncryption(String iv, String key) {
		ivspec = new IvParameterSpec(iv.getBytes());
		keyspec = new SecretKeySpec(key.getBytes(), "AES");
		try {
			cipher = Cipher.getInstance("AES/CBC/NoPadding");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			Logs.i(e.toString()+"");
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			Logs.i(e.toString()+"");
		}
	}
	/**
	 * 把字符串转换为 byte
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public byte[] encryptByte(String text) throws Exception
	{
		if(text == null || text.length() == 0)
			throw new Exception("Empty string");
		byte[] encrypted = null;
		try {
			cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
			encrypted = cipher.doFinal(padString(text).getBytes());
		} catch (Exception e)
		{                       
			throw new Exception("[encrypt] " + e.getMessage());
		}

		return encrypted;
	}
	public String encrypt(String text) throws Exception {
		if (text == null || text.length() == 0)
			throw new Exception("Empty string");

		byte[] encrypted = null;
		try {
			cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
			encrypted = cipher.doFinal(padString(text).getBytes());
		} catch (Exception e) {
			throw new Exception("[encrypt] " + e.getMessage());
		}
		return bytesToHex(encrypted);
	}

	public byte[] decrypt(String code) throws Exception {
		if (code == null || code.length() == 0)
			throw new Exception("Empty string");
		byte[] decrypted = null;
		try {
			cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
			decrypted = cipher.doFinal(hexToBytes(code));
		} catch (Exception e) {
			throw new Exception("[decrypt] " + e.getMessage());
		}
		return decrypted;
	}

	public static String bytesToHex(byte[] data) {
		if (data == null) {
			return null;
		}

		int len = data.length;
		String str = "";
		for (int i = 0; i < len; i++) {
			if ((data[i] & 0xFF) < 16)
				str = str + "0" + Integer.toHexString(data[i] & 0xFF);
			else
				str = str + Integer.toHexString(data[i] & 0xFF);
		}
		return str;
	}

	public static byte[] hexToBytes(String str) {
		if (str == null) {
			return null;
		} else if (str.length() < 2) {
			return null;
		} else {
			int len = str.length() / 2;
			byte[] buffer = new byte[len];
			for (int i = 0; i < len; i++) {
				buffer[i] = (byte) Integer.parseInt(
						str.substring(i * 2, i * 2 + 2), 16);
			}
			return buffer;
		}
	}

	private static String padString(String source) {
		char paddingChar = ' ';
		int size = 16;
		int x = source.length() % size;
		int padLength = size - x;

		for (int i = 0; i < padLength; i++) {
			source += paddingChar;
		}

		return source;
	}
}