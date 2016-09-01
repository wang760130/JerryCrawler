package com.jerry.crawler.components.extract;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.ParserException;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月1日
 */
public class ExtractContext {

	/**
	 * 收集HTML页面信息
	 * @param link
	 */
	public void makeContext(ChannelLink channelLink) {
		try {
			String link = channelLink.getLink();
			String url = this.getLinkUrl(link);
			Parser parser = new Parser(link);
			parser.setEncoding(channelLink.getEncode());
			
			for(NodeIterator i = parser.elements(); i.hasMoreNodes();) {
				Node node = (Node)i.nextNode();
				if(node instanceof Html) {
					
				}
			}
			
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从一个字符串中提去出连接
	 */
	private String getLinkUrl(String link) {
		String urlDomaiPattern = "(http://[^/]*?" + "/)(.*?)";
		Pattern pattern = Pattern.compile(urlDomaiPattern, Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
		Matcher matcher = pattern.matcher(link);
		String url = "";
		while(matcher.find()) {
			int start = matcher.start(1);
			int end  = matcher.end(1);
			url = link.substring(start, end - 1).trim();
		}
		return url;
	}
}
