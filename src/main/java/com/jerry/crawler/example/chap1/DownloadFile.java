package com.jerry.crawler.example.chap1;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class DownloadFile {
	
	/**
	 * 根据URL和网页类型生成需要保存的网页的文件名，去除URL中的非文件名字符
	 * @param url
	 * @param contentType
	 * @return
	 */
	public String getFileNameByUrl(String url, String contentType) {
		// 移除 http://
		url = url.substring(7);
		
		// text/html类型
		if(contentType.indexOf("html")!=-1) {
			url= url.replaceAll("[\\?/:*|<>\"]", "_")+".html";
			return url;
		} else {
			//如application/pdf类型
			return url.replaceAll("[\\?/:*|<>\"]", "_")+"."+contentType.substring(contentType.lastIndexOf("/")+1);
		}	
	}
	
	/**
	 * 保存网页字节数据到本地文件，filePath为要保存的相对地址
	 * @param data
	 * @param filePath
	 */
	private void saveToLocal(byte[] data, String filePath) {
		DataOutputStream out = null;
		try {
			out = new DataOutputStream(new FileOutputStream(new File(filePath)));
			for(int i = 0; i < data.length; i++) {
				out.write(data[i]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 下载URL指向的网页
	 * @param url
	 * @return
	 */
	public String downloadFile(String url) {
		String filePath = null;
		HttpClient httpClient = new HttpClient();
		// 设置http连接超时5s
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		GetMethod getMethod = new GetMethod(url);
		// 设置getMethod对象并设置参数
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		// 设置请求重试处理
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if(statusCode != HttpStatus.SC_OK) {
				// failed
				filePath = null;
			}
			// 读取为文字数组
			byte[] responseBody = getMethod.getResponseBody();
			// 根据网页 url 生成保存时的文件名
			filePath = "temp\\" + getFileNameByUrl(url, getMethod.getResponseHeader("Content-Type").getValue());
			this.saveToLocal(responseBody, filePath);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		return filePath;
	}
}	
