package com.jerry.crawler.zhifu;

import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		String url = "http://www.zhihu.com/explore/recommendations";
		// 获取知乎推荐首页
		String content = Spider.SendGet(url);

		// 获取推荐内容详细内容
		ArrayList<ZhifuModel> zhihuList = Spider.GetRecommendations(content);

		// 写入文档
		for (ZhifuModel zhihu : zhihuList) {
			FileReaderWriter.writeIntoFile(zhihu.writeString(),
					"D:/知乎_编辑推荐.txt", true);
		}
	}
}
