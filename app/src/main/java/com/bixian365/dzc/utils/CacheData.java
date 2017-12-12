package com.bixian365.dzc.utils;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * ***************************
 * 报文缓存
 * ***************************
 */
public class CacheData {
	private CacheData() {
	}
	private static CacheData instance;

	public static CacheData getInstance() {
		if (instance == null)
			instance = new CacheData();
		return instance;
	}

	/**
	 *  数据加载  做本地缓存 保存本地
	 * @param path   缓存路径
	 * @param fileName  缓存文件名
	 * @param data  
//	 * @param SXUtils.WirtCacheData(SXUtils.cacheDataPath, "/searchhot.txt",response.toString());
	 */
	public  void  WirtCacheData(String path, String fileName, String data){
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			deleteFolderFile(path+fileName, true);
			try {
				File file = CreateText(path, fileName);
				// 以指定文件创建RandomAccessFile对象
				RandomAccessFile raf = new RandomAccessFile(file, "rw");
				// 将文件记录指针移动最后
				raf.seek(file.length());
				// 输出文件内容
				raf.write(data.getBytes());
				raf.close();
				Logs.i("写入缓存文件成功====","======"+path+fileName);
			} catch (Exception e) {
				Logs.i("写入缓存文件异常====",e.toString()+"");
			}
		}
	}
	/**
	 * 缓存文件读取内容
	 * @param path   缓存路径
	 * @param fileName 缓存文件名
	 * @return
	 */
	public  String ReadCacheData(String path, String fileName){
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// 获取SDcard路径
			StringBuilder sb = new StringBuilder();
			try {
				File file = new File(path, fileName);
				//判断文件夹是否存在,如果不存在则创建文件夹
				if (!file.exists()) {
					return "";
				}

				InputStream inputStream = new FileInputStream(file);
				int len = 0;
				byte[] buffer = new byte[1024];
				while ((len = inputStream.read(buffer)) != -1) {
					sb.append(new String(buffer, 0, len));
				}
				inputStream.close();
				return sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 首页缓存写入数据
	 * @param path   创建目录
	 * @param fielName  文件名
	 */
	public  File CreateText(String path, String fielName) {
		File file = new File(path);
		File f;
		if (!file.exists()) {
			// 若不存在，创建目录
			file.mkdirs();
		}
		//创建文件
		f = new File(path+fielName);
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  f;

	}
	/** 
	 * 删除指定目录下文件及目录 
	 *  
	 * @param filePath
	 * @param deleteThisPath
	 * @return 
	 */  
	public  int deleteFolderFile(String filePath, boolean deleteThisPath)
	{  
		if (!TextUtils.isEmpty(filePath)) {
			File file = new File(filePath);
			if (file.isDirectory()) {// 处理目录  
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {  
					deleteFolderFile(files[i].getAbsolutePath(), true);  
				}
			}  
			if (deleteThisPath) {  
				if (!file.isDirectory()) {// 如果是文件，删除  
					file.delete();  
				} else {// 目录  
					if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除  
						file.delete();  
					}  
				}  
			}  
		}
		return 1;  
	}  
}
