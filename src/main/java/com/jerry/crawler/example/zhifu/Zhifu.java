package com.jerry.crawler.example.zhifu;

import java.util.List;

public class Zhifu {

	// 链接地址
	private String url;
	// 问题
	private String question;
	// 问题描述
	private String questionDescription;
	// 回答
	private List<String> answers;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getQuestionDescription() {
		return questionDescription;
	}
	public void setQuestionDescription(String questionDescription) {
		this.questionDescription = questionDescription;
	}
	public List<String> getAnswers() {
		return answers;
	}
	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}
	@Override
	public String toString() {
		return "Zhifu [url=" + url + ", question=" + question
				+ ", questionDescription=" + questionDescription + ", answers="
				+ answers + "]";
	}
	
}
