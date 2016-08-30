package com.jerry.crawler.example.chap1;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;

public class HttpProxyTest {
	public static void main(String[] args) {
		// 创建 HttpClient 相当于打开一个代理	
		HttpClient httpClient = new HttpClient();
		// 设置代理服务器的 IP 地址和端口
		httpClient.getHostConfiguration().setProxy("192.168.0.1", 9527);
		// 告诉 httpClient，使用抢先认证，否则你会收到“你没有资格”的恶果
		httpClient.getParams().setAuthenticationPreemptive(true);
		// MyProxyCredentialsProvder 返回代理的 credential(username/password)
//		httpClient.getParams().setParameter(CredentialsProvider.PROVIDER,
//				new MyProxyCredentialsProvider());
		// 设置代理服务器的用户名和密码
		httpClient.getState().setProxyCredentials(
				new AuthScope("192.168.0.1", AuthScope.ANY_PORT,
						AuthScope.ANY_REALM),
				new UsernamePasswordCredentials("username", "password"));
	}
}
