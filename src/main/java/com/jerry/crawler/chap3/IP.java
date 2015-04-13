package com.jerry.crawler.chap3;

import java.io.IOException;
import java.net.InetAddress;

public class IP {
	public static void main(String[] args) {
		String hostname = "jerry.wang";
		try {
			InetAddress ipAddress = InetAddress.getByName(hostname);
			System.out.println("Ip Address :" + ipAddress.getHostAddress());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
