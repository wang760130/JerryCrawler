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
	public String getFileName(String url, String contentType) {
		
		// 移除 http://
		url = url.substring(7);
		
		if(contentType.indexOf("html") != -1) {
			return url.replaceAll("[\\?/:*|<>\"]", "_") + ".html";
		} else {
			//如application/pdf类型
			return url.replaceAll("[\\?/:*|<>\"]", "_") + "." + contentType.substring(contentType.lastIndexOf("/") + 1);
		}
	}

	/**
	 * 下载URL指向的网页
	 * @param url
	 * @return
	 */
	public String downloadFile(String url) {
		HTTPProxy proxy = new HTTPProxy();
		DataOutputStream out = null;
		try {
			proxy.get(url, null);
			
			byte[] responseBody = proxy.getResponseBody();
			String contentType = proxy.getContentType();
			String filePath = "temp\\" + getFileName(url, contentType);
			
			out = new DataOutputStream(new FileOutputStream(new File(filePath)));
			for(int i = 0; i < responseBody.length; i++) {
				out.write(responseBody[i]);
			}
			
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
