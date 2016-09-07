package com.jerry.crawler.example.zhifu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jerry.crawler.components.crawler.HTTPProxy;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月7日
 */
public class Crawler {

	private static final String URL = "http://www.zhihu.com/explore/recommendations";
	
	private static List<String> getQuestionUrl() {
		List<String> questionUrlList = new ArrayList<String>();
		try {
			HTTPProxy proxy = new HTTPProxy();
			proxy.get(URL, null);
			String sourceCode = proxy.getSourceCode();
			
			Document document = Jsoup.parse(sourceCode);
			//推荐内容元素
			Elements items =  document.getElementsByClass("zm-item");         
			for(Element item : items){
				
				// 推荐内容标题元素
				Element h2TagElement = item.getElementsByTag("h2").first();   
				// 推荐内容的Url超链接元素
				Element aTagElement = h2TagElement.getElementsByTag("a").first();  
				// 推荐内容url
				String href = aTagElement.attr("href");    
				// 去除不规范url
				if(href.contains("question")){                            
					Pattern pattern = Pattern.compile("question/(.*?)/");
					Matcher matcher = pattern.matcher(href);
					if (matcher.find()) {
						String questionUrl = "http://www.zhihu.com/question/" + matcher.group(1);
						questionUrlList.add(questionUrl);
					}
				}
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return questionUrlList;
	}
	
	private static List<Zhifu> getAnswers() {
		List<Zhifu> list = new ArrayList<Zhifu>();
		Zhifu zhifu = null;
		
		List<String> quesionUrlList = getQuestionUrl();
		for(String questionUrl : quesionUrlList) {
			try {
				HTTPProxy proxy = new HTTPProxy();
				proxy.get(questionUrl, null);
				String sourceCode = proxy.getSourceCode();
				
				Document doc = Jsoup.parse(sourceCode);
				// 获取标题，即用户发布的问题
				String title = doc.title();
				
				// 问题消息标书
				String questionDescription = "";
				Element despElement = doc.getElementById("zh-question-detail");
				if(despElement != null){
					questionDescription = despElement.text();
				}
				
				// 解答
				List<String> answers = new ArrayList<String>();
				Elements ansItems = doc.getElementsByClass("zm-item-answer");
				for(Element ansItem:ansItems){
					Element textElement = ansItem.getElementsByClass("zm-item-rich-text").first();
					if(despElement != null){
						answers.add(textElement.text());
					}
				}
				
				zhifu = new Zhifu();
				zhifu.setUrl(questionUrl);
				zhifu.setQuestion(title);
				zhifu.setQuestionDescription(questionDescription);
				zhifu.setAnswers(answers);
				list.add(zhifu);
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public static void crawling() {
		List<Zhifu> zhifuList = getAnswers();
		for(Zhifu zhifu : zhifuList) {
			System.out.println(zhifu.getQuestion());
		}
		
	}
	
	public static void main(String[] args) {
		Crawler.crawling();
	}
}
