package com.jerry.crawler.chap1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;

public class RetrivePage {
	
	private static HttpClient httpClient = new HttpClient();
	
	// 设置代理服务器
	static {
//		httpClient.getHostConfiguration().setProxy("192.168.1.1", 8000);
	}
	
	public static boolean downloadPage(String path) throws HttpException, IOException, ParserException {
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
			String charset = post.getResponseCharSet();
			// 得到文件名	
			String filename = path.substring(path.lastIndexOf('/') + 1);
			filename += System.currentTimeMillis();
				
			File tempFile = new File(filename);
			if(!tempFile.exists()) {
				tempFile.createNewFile();
			}
			// 获取文件输出流
			output = new FileOutputStream(tempFile);
			
			// 输出到文件
			int tempByte = -1;
			
			while ((tempByte = input.read()) > 0) {
				output.write(tempByte);
			}
			
			if(input != null)
				input.close();
			
			if(output != null)
				output.close();
			
			// 使用 htmlpaser解析
			Parser parser = new Parser(filename);
			parser.setEncoding(charset);
			NodeVisitor nodeVisitor = new NodeVisitor() {
				private boolean flag = false;
				private int index = -1;
				
				public void visitTag (Tag tag) {
					if(tag.getTagName().equals("TBODY")) {
						System.out.println("begin...");
						flag = true;
						index = 0;
					}
					
					if(tag.getTagName().equals("TD") && flag) {
						switch (index) {
						case 0:
							System.out.println("from_to :" + tag.toPlainTextString().trim());
							break;
						case 1:
							System.out.println("carrier :" + tag.toPlainTextString().trim());
							break;
						case 2:
							System.out.println("type :" + tag.toPlainTextString().trim());
							break;
						case 3:
							System.out.println("number :" + tag.toPlainTextString().trim());
							break;
						case 4:
							System.out.println("dicount :" + tag.toPlainTextString().trim());
							break;
						case 5:
							System.out.println("price :" + tag.toPlainTextString().trim());
							break;
						default:
							break;
						}
						index ++;
					}
				}
				
				public void visitEndTag (Tag tag) {
					if(tag.getTagName().equals("TBOOY")) {
						System.out.println("end...");
						flag = false;
					}
				}
			};
			parser.visitAllNodesWith(nodeVisitor);
			return true;
		
		}
		return false;
	}
}
