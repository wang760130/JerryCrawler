package com.jerry.crawler.example.chap1;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class PostMethodTest {
	private static String path = "http://www.baidu.com";
	public static void main(String[] args) {
		//创建一个客户端，类似于打开一个浏览器
		HttpClient httpclient=new HttpClient();
		//得到 post 方法
		PostMethod postMethod = new PostMethod(path);
		//使用数组来传递参数
		NameValuePair[] postData = new NameValuePair[1];
		//设置参数
		postData[0] = new NameValuePair("wd", "枪");
		postMethod.addParameters(postData);
		try {
			//回车，获得响应状态码
			int statusCode=httpclient.executeMethod(postMethod);
			System.out.println(statusCode);
			//查看命中情况，可以获得的东西还有很多，比如 head、cookies 等
			System.out.println(postMethod.getResponseBodyAsString());
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
	}

}
