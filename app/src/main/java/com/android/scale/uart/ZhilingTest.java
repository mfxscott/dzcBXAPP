package com.android.scale.uart;

import com.bixian365.dzc.utils.Logs;

public class ZhilingTest {

	public static void main(String[] args) {

	}
	public static byte[] PricbD(){
		//                    P > M B D   0X08     0X00 0X00 0X75
		// 0X30            0X0A         0X0A              0X4E
		// 0X20                 0X2F
		//                               (长度=8) （量程=0x00007530 =
		// 30.000kg）（分度值=10g）（追零大小=10g）（标定砝码0x4e20=20.000kg） （校验）
		// 指令头
		String head = stringToAscii("P>MBD");
//		503e4d424408000003e80a0a03e8f2
		// 长度
		String len = "08";
		// 　最大量程 例如 30千克
		String range= bytesToHexString(intToBytes(30*1000)); // 这里要换成单位 克
		// 分度值 例如 10g
		String a = Integer.toHexString(10); // 转成16进制
		while (a.length() < 2) {
			a = "0" + a;
		}

		// 追零大小 例如 10g
		String b = Integer.toHexString(5); // 转成16进制
		while (b.length() < 2) {
			b = "0" + b;
		}
		// 标定砝码的重量 例如20 千克
		String reference = bytesToHexString(intTo2Bytes(15 * 1000)); // 这里要换成单位克
		// 校验
		String tmp = len + range + a + b + reference;
		byte[] newData = hexStringToBytes(tmp);
		int sum = 0;
		for (int i = 0; i < newData.length; i++) {
			sum += newData[i];
		}
		String crc = Integer.toHexString(sum);
		if (crc.length() > 2) {
			crc = crc.substring(crc.length() - 2, crc.length());
		} else if (crc.length() < 2) {
			crc = "0" + crc;
		}
		// 最后全部指令
		String cmd = head + len + range + a + b + reference + crc;
		// 转成byte
		byte[] sendData = hexStringToBytes(cmd);
		// sendData 通过串口发出去
		System.out.println(cmd);
		Logs.i("+++++++++++++++++++"+cmd);
		return sendData;
	}
	/**
	 * 置零
	 * @return
	 */
	public static byte[] ZL(){
		String  head = stringToAscii("P>MZO");
		String len = "01";
		String d = "0001";
		String cmd = head+len+d;
		byte[] sendData = hexStringToBytes(cmd);
		return  sendData;
	}

	/**
	 * 去皮
	 * @return
	 */
	public static byte[] QP(){
		String  head = stringToAscii("P>MTA");
		String len = "01";
		String d = "0001";
		String cmd = head+len+d;
		byte[] sendData = hexStringToBytes(cmd);
		return sendData;
	}
	/**
	 * 字符串转ascii码
	 */
	public static String stringToAscii(String value) {
		StringBuffer sbu = new StringBuffer();
		char[] chars = value.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			String a = Integer.toHexString((int) chars[i]);
			if (a.length() == 1) {
				a = "0" + a;
			}
			sbu.append(a);
		}
		return sbu.toString();
	}

	/**
	 * int转byte数组
	 */
	public static byte[] intToBytes(int value) {
		byte[] src = new byte[4];
		src[0] = (byte) ((value >> 24) & 0xFF);
		src[1] = (byte) ((value >> 16) & 0xFF);
		src[2] = (byte) ((value >> 8) & 0xFF);
		src[3] = (byte) (value & 0xFF);
		return src;
	}

	/**
	 * int转byte数组
	 */
	public static byte[] intTo2Bytes(int value) {
		byte[] src = new byte[2];
		src[0] = (byte) ((value >> 8) & 0xFF);
		src[1] = (byte) (value & 0xFF);
		return src;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 将byte数组转成十六进制字符串
	 *
	 * @param src
	 *            需要转换的byte数组
	 * @return 十六进制字符串
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * 将十六进制字符串转成byte数组
	 *
	 * @param hexString
	 *            十六进制字符串
	 * @return 一个byte数组
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}
}

