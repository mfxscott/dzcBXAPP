package com.bixian365.dzc.utils.dncry.wsc;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 *
 ******************************************************
 * @author LK
 * @date 2014-12-17 上午11:06:40
 * @Company 深圳市优讯信息技术有限公司
 * @Description：整个报文加解密工具
 * 	<li>1、BASE64转码得到字符串</li>
 * 	<li>2、MD5加密后得到字符串和1得到的字符串组成JSON</li>
 * 	<li>3、自动生成Key</li>
 * 	<li>4、使用该Key的前16位作为AES的secretKey，后16位作为ivKey，对2得到的JSON进行AES加密</li>
 * 	<li>5、将Key进行AES加密（使用默认的secretKey和ivKey）</li>
 * 	<li>6、最后将得到的5得到的加密Key、4得到的JSON、servicename放入到JSON中。</li>
 ******************************************************
 */
public class UXUNMSGEncrypt {
	private final static String LOG = "EncryptUtils";

	private static final String REQ_MSG = "reqMsg";
	private static final String RSP_MSG = "rspMsg";
	private static final String DATA = "data";
	private static final String OUT_DATA = "outdata";
	private static final String SOURCE = "source";
	private static final String COMMON_REQMSG = "commonReqMsg";
	private static final String COMMON_RSPMSG = "commonRspMsg";
	private static final String REQ_SN = "reqsn";
	private static final String RSP_SN = "rspsn";
	private static final String SERVICESIGN = "serviceSign";

	private static UXUNMSGEncrypt instance = null;
	private BASE64Encoder base64;

	private UXUNMSGEncrypt() {
		base64 = new BASE64Encoder();
	}

	public static UXUNMSGEncrypt getInstance() {
		if (instance == null) {
			instance = new UXUNMSGEncrypt();
		}
		return instance;
	}

	/**
	 * 存储数据加密
	 * @param content
	 * @return
	 */
	public String strEncrypt(String content){
// AES加密
		String encryptKey="";
		try {
			AESEDncryption mAes = new AESEDncryption();
			encryptKey = mAes.encrypt(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptKey;
	}
	/**
	 * 存储数据解密字符串
	 * @param content
	 * @return
     */
	public String strDecrypt(String content){
// AES加密
		String encryptKey="";
		try {
			AESEDncryption mAes = new AESEDncryption();
			encryptKey = AESEDncryption.bytesToHex(mAes.decrypt(content));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptKey;
	}

	/**
	 * 加密
	 * @param source  		原请求报文
	 * @param serviceSign	命令common
	 * @return 报文为null或者异常返回null
	 */
	public String encrypt(String source, int serviceSign) {
		if (source == null && source.length() == 0)
			return null;
		try {
			// BASE64 转码
			String Str = base64.encode(source.getBytes("UTF-8"));
			// MD5 加密
			String md5Str = MD5Utils.encrypt(Str);
			JSONObject json = getMD5Json(Str, md5Str);
			// AES加密
			AESEDncryption mAes = new AESEDncryption();
			// 生成Key
			String key = getKey();
			String encryptKey = mAes.encrypt(key);
			Str = AESEncrypt(key, json.toString());

			// 封装最终请求JSON报文
			JSONObject jsonData = getReqMsgJson(Str, serviceSign, encryptKey);

			return jsonData.toString();

		} catch (Exception e) {
			e.printStackTrace();
			Log.e(LOG, "报文加密失败........" + e.getMessage());
		}
		return null;
	}

	/**
	 * 解密
	 * @param json
	 * @return 返回"MD5NO"表示两次的MD5值不相同，返回null表面json为空
	 */
	public String decrypt(JSONObject json) {
		if (json == null)
			return null;
		try {
			JSONObject json2 = json.getJSONObject(COMMON_RSPMSG);
			String aesdata = json2.getString(OUT_DATA);

			// AES 解密
			String key = json2.getString(RSP_SN);
			String msg = AESDecrypt(key, aesdata);

			JSONObject json3 = new JSONObject(msg);
			String md5Str = json3.getString(DATA);
			String Msg = json3.getString(RSP_MSG);
			// MD5 解密
			String md5Str2 = MD5Utils.encrypt(Msg);
			// 与原MD5值进行比较
			if (md5Str.equals(md5Str2)) {
				// BASE64 解码
				return new String(base64.decode(Msg));
			} else {
				Log.e(LOG, "生成的MD5和接收到的MD5值不同，接收到的MD5=[" + md5Str + "]" + "，生成的MD5=["+ md5Str2 +"]");
				return "MD5NO";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(LOG, "报文解密失败........" + e.getMessage());
		}
		return null;
	}

	private JSONObject getReqMsgJson(String data, int name, String key)
			throws JSONException {
		JSONObject jsonData = new JSONObject();
		jsonData.put(OUT_DATA, data);
		jsonData.put(SERVICESIGN, name);
		jsonData.put(REQ_SN, key);
		jsonData.put(SOURCE, "pluginpay");//每个工程请求source不一样

		JSONObject reqMsg = new JSONObject();
		reqMsg.put(COMMON_REQMSG, jsonData);
		return reqMsg;
	}

	private JSONObject getMD5Json(String msg, String md5) throws JSONException {
		JSONObject json = new JSONObject();
		json.put(REQ_MSG, msg);
		json.put(DATA, md5);
		return json;
	}

	private String AESEncrypt(String key, String text) throws Exception {
		String secretKey = key.substring(0, 16).trim();
		String ivKey = key.substring(16).trim();
		AESEDncryption aes = new AESEDncryption(ivKey, secretKey);
		return aes.encrypt(text);
	}

	private String AESDecrypt(String key, String data) throws Exception {
		AESEDncryption aes = new AESEDncryption();
		String key2 = new String(aes.decrypt(key), "UTF-8");
		String secretKey = key2.substring(0, 16).trim();
		String ivKey = key2.substring(16).trim();
		aes = new AESEDncryption(ivKey, secretKey);
		return new String(aes.decrypt(data), "UTF-8");
	}

	public String getKey() throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		SecureRandom secureRandom = new SecureRandom();
		String str = String.valueOf((secureRandom.nextInt() * 1000000));
		str += System.currentTimeMillis();
		return MD5Utils.encrypt(str).toUpperCase();
	}
}
