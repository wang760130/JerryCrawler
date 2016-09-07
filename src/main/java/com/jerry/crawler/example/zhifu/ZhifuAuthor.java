package com.jerry.crawler.example.zhifu;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月7日
 */
public class ZhifuAuthor {

	// 链接地址
	private String url;
	
	// 链接地址MD5值
	private String md5;
	
	// 作者名称
	private String name;
	
	// 作者介绍
	private String description;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ZhifuAuthor [url=" + url + ", md5=" + md5 + ", name=" + name
				+ ", description=" + description + "]";
	}
	
}
