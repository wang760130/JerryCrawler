package com.jerry.crawler.components.crawler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.httpclient.HttpException;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年8月30日
 */
public class DownloadFile {

	/**
	 * 根据URL和网页类型生成需要保存的网页的文件名，去除URL中的非文件名字符
	 * @param url
	 * @param contentType
	 * @return
	 */
	public String getFileName(String url) {
		// 移除 http://
		url = url.substring(7);
		url = url.replaceAll("[\\?/:*|<>\"#.]", "_");
		
		if(url.endsWith("_")) {
			url = url.substring(0, url.length() - 1);
		}
		return url;
		
	}

	/**
	 * 下载URL指向的网页
	 * @param url
	 * @return
	 */
	public String downloadFile(String url, String path) {
		HTTPProxy proxy = new HTTPProxy();
		DataOutputStream out = null;
		try {
			proxy.get(url, null);
			
			byte[] responseBody = proxy.getResponseBody();
			String file = path + File.separator + getFileName(url);
			
			out = new DataOutputStream(new FileOutputStream(new File(file)));
			for(int i = 0; i < responseBody.length; i++) {
				out.write(responseBody[i]);
			}
			out.flush();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
