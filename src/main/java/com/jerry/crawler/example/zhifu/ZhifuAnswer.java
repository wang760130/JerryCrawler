package com.jerry.crawler.example.zhifu;

/**
 * 知乎答案
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月7日
 */
public class ZhifuAnswer {

	// 答案内容
	private String content;
		
	// 作者
	private ZhifuAuthor zhifuAuthor;
	
	// 评论次数
	private long commentNum;

	// 编辑时间
	private String editDate;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ZhifuAuthor getZhifuAuthor() {
		return zhifuAuthor;
	}

	public void setZhifuAuthor(ZhifuAuthor zhifuAuthor) {
		this.zhifuAuthor = zhifuAuthor;
	}

	public long getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(long commentNum) {
		this.commentNum = commentNum;
	}

	public String getEditDate() {
		return editDate;
	}

	public void setEditDate(String editDate) {
		this.editDate = editDate;
	}

	@Override
	public String toString() {
		return "ZhifuAnswer [content=" + content + ", zhifuAuthor="
				+ zhifuAuthor + ", commentNum=" + commentNum + ", editDate="
				+ editDate + "]";
	}
	
}
