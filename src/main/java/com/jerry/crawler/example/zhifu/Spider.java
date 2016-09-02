package com.jerry.crawler.example.zhifu;

import java.util.ArrayList;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Spider {
	
	//获取指定Url页面内容
	//采用http-client和http-core 4.3 jar包
	public static String SendGet(String url) {

		CloseableHttpClient client = HttpClients.createDefault();
		try{
			HttpGet request = new HttpGet(url);
			CloseableHttpResponse resp = client.execute(request);
			
			String result = EntityUtils.toString(resp.getEntity());
			
			return result;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				client.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}

	// 获取推荐内容详细内容url
	public static ArrayList<ZhifuModel> GetRecommendations(String content) {
		
		ArrayList<ZhifuModel> results = new ArrayList<ZhifuModel>();
		Document doc = Jsoup.parse(content);
		Elements items =  doc.getElementsByClass("zm-item");          //推荐内容元素
		for(Element item:items){
			Element h2TagEle = item.getElementsByTag("h2").first();   //推荐内容标题元素
			Element aTagEl = h2TagEle.getElementsByTag("a").first();  //推荐内容的Url超链接元素
			String href = aTagEl.attr("href");                        //推荐内容url
			if(href.contains("question")){                            //去除不规范url
				results.add(new ZhifuModel(href));
			}
		}
		return results;
	}

}