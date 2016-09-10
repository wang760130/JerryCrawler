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
				if(href.startsWith("/question/")){                            
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
	
	private static List<ZhifuQuestion> getAnswers() {
		List<ZhifuQuestion> list = new ArrayList<ZhifuQuestion>();
		ZhifuQuestion question = null;
		
		List<String> quesionUrlList = getQuestionUrl();
		for(String questionUrl : quesionUrlList) {
			try {
				HTTPProxy proxy = new HTTPProxy();
				proxy.get(questionUrl, null);
				String sourceCode = proxy.getSourceCode();
				
				Document document = Jsoup.parse(sourceCode);
				
				// 问题标题
				String questionTitle = "";
				Element questionTitleElement = document.getElementById("zh-question-title");
				if(questionTitleElement != null) {
					Elements editableContentElement = questionTitleElement.getElementsByClass("zm-editable-content");
					if(editableContentElement != null && editableContentElement.size() > 0) {
						questionTitle = editableContentElement.first().text();
					}
					
				}
				
				// 问题描述
				String questionDetail = "";
				Element questionDetailElement = document.getElementById("zh-question-detail");
				if(questionDetailElement != null){
					questionDetail = questionDetailElement.text();
				}
				
				
				// Tag
				/*Elements tagItems = document.getElementsByClass("zm-item-tag");
				for(Element tagItem : tagItems) {
					tagItem.text();
				}*/
				
				// 解答
				ZhifuAnswer answer = null;
				List<ZhifuAnswer> answerList = new ArrayList<ZhifuAnswer>();
				Elements answerItems = document.getElementsByClass("zm-item-answer");
				for(Element answerItem : answerItems){
					answer = new ZhifuAnswer();
					
					String content = "";
					Element contentElement = answerItem.getElementsByClass("zm-item-rich-text").first();
					if(questionDetailElement != null){
						content = contentElement.text();
						answer.setContent(content);
					}
					
					answerList.add(answer);
				}
				
				question = new ZhifuQuestion();
				question.setUrl(questionUrl);
				question.setQuestionTitle(questionTitle);
				question.setQuestionDetail(questionDetail);
				question.setAnswerList(answerList);
				
				System.out.println(question);
				list.add(question);
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public static void crawling() {
		getAnswers();		
	}
	
	public static void main(String[] args) {
		Crawler.crawling();
	}
}
