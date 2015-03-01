package com.jerry.crawler.lietu.chap1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class RetrivePage {
	
	private static HttpClient httpClient = new HttpClient();
	
	// 设置代理服务器
	static {
//		httpClient.getHostConfiguration().setProxy("192.168.1.1", 8000);
	}
	
	public static boolean downloadPage(String path) throws HttpException, IOException {
		InputStream input = null;
		OutputStream output = null;
		
		// 得到post方法
		PostMethod post = new PostMethod(path);
		
		// 设置post方法的参数
		NameValuePair[] postData = new NameValuePair[2];
		
		postData[0] = new NameValuePair("name","lietu");
		postData[1] = new NameValuePair("password","lietu");
		
		post.addParameters(postData);
		
		// 执行， 返回状态码
		int statusCode = httpClient.executeMethod(post);
		
		// 针对状态码进行处理
		if(statusCode == HttpStatus.SC_OK) {
			input = post.getResponseBodyAsStream();
			
			// 得到文件名	
			String filename = path.substring(path.lastIndexOf('/') + 1);
			
			// 获取文件输出流
			output = new FileOutputStream(filename);
			
			// 输出到文件
			int tempByte = -1;
			
			while ((tempByte = input.read()) > 0) {
				output.write(tempByte);
			}
			
			if(input != null)
				input.close();
			
			if(output != null)
				output.close();
			
			return true;
		
		}
		return false;
	}
}
