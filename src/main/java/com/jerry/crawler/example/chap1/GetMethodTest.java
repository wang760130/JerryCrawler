package com.jerry.crawler.example.chap1;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

public class GetMethodTest {
	private static String path = "http://www.baidu.com";
	public static void main(String[] args) {
		//创建一个客户端，类似于打开一个浏览器
		HttpClient httpclient=new HttpClient();
		//创建一个 get 方法，类似于在浏览器地址栏中输入一个地址
		GetMethod getMethod=new GetMethod(path);
		try {
			//回车，获得响应状态码
			int statusCode=httpclient.executeMethod(getMethod);
			System.out.println(statusCode);
			
			//查看命中情况，可以获得的东西还有很多，比如 head、cookies 等
			System.out.println(getMethod.getResponseBodyAsString());
			
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//释放
			getMethod.releaseConnection();
		}
	}
}
