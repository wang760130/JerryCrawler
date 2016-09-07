package com.jerry.crawler.example.zhifu;

import java.util.List;

/**
 * 知乎问题
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月7日
 */
public class ZhifuQuestion {

	// 链接地址
	private String url;
	
	// 链接地址MD5值
	private String md5;
	
	// 作者
	private ZhifuAuthor zhifuAuthor;
	
	// 问题
	private String questionTitle;
	
	// 问题描述
	private String questionDetail;
	
	// 标签
	private String tag;
	
	// 回答
	private List<ZhifuAnswer> answerList;
	
	// 最后编辑时间
	private String lastEditDate;
	
	// 浏览次数
	private long browsingTimes;

	// 关注人数
	private long followNum;

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

	public ZhifuAuthor getZhifuAuthor() {
		return zhifuAuthor;
	}

	public void setZhifuAuthor(ZhifuAuthor zhifuAuthor) {
		this.zhifuAuthor = zhifuAuthor;
	}

	public String getQuestionTitle() {
		return questionTitle;
	}

	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	public String getQuestionDetail() {
		return questionDetail;
	}

	public void setQuestionDetail(String questionDetail) {
		this.questionDetail = questionDetail;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public List<ZhifuAnswer> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<ZhifuAnswer> answerList) {
		this.answerList = answerList;
	}

	public String getLastEditDate() {
		return lastEditDate;
	}

	public void setLastEditDate(String lastEditDate) {
		this.lastEditDate = lastEditDate;
	}

	public long getBrowsingTimes() {
		return browsingTimes;
	}

	public void setBrowsingTimes(long browsingTimes) {
		this.browsingTimes = browsingTimes;
	}

	public long getFollowNum() {
		return followNum;
	}

	public void setFollowNum(long followNum) {
		this.followNum = followNum;
	}

	@Override
	public String toString() {
		return "ZhifuQuestion [url=" + url + ", md5=" + md5 + ", zhifuAuthor="
				+ zhifuAuthor + ", questionTitle=" + questionTitle
				+ ", questionDetail=" + questionDetail + ", tag=" + tag
				+ ", answerList=" + answerList + ", lastEditDate="
				+ lastEditDate + ", browsingTimes=" + browsingTimes
				+ ", followNum=" + followNum + "]";
	}

}
