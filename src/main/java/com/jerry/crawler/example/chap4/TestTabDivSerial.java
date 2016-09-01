package com.jerry.crawler.example.chap4;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.Html;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.tags.StyleTag;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class TestTabDivSerial {
	/**
	 * 新行
	 */
	private static final String NEWLINE = System.getProperty("line.spatator");
	/**
	 * 字符串长度
	 */
	private static final int NEWLINE_SIZE = NEWLINE.length();
	private String url = "";
	private final String oriEncode=  "gb2312, utf-8, gbk, iso-8859-1";
	private List<TableContext> htmlContext = new ArrayList<TableContext>();
	private String urlEncode = "";
	private int tableNumber = 0;
	private int channelNumber = 0;
	private int totalNumber = 0;
	
	//URL正则表达
	private String domain = "";
	private String urlDomaiPattern = "";
	private String urlPattern = "";
	private Pattern pattern = null;
	private Pattern patternPost = null;
	
	public final static int CHANNELLINKTYPE = 1;
	
	public final static int COMMONLINKTYPE = 2;
	
	public final static int OUTDOMAINLINKTYPE = 3;
	
	public void channelParseProcess() {
		/**
		 * 提取本站信息的正则表达式
		 */
		urlDomaiPattern = "(http://[^/]*?)" + domain + "/)(.*?";
		urlPattern = "(http://[^/]*?" + domain + "/[^.]*?).(shtml|html|htm|shtm|php|asp#|sap|cgi|jsp|aspx)";
		pattern = Pattern.compile(urlDomaiPattern, Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
		patternPost = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
		/**
		 * 收集表单集合
		 */
		SplitManager splitManager = ExtractLinkConsole.context.getBean("splitManager");
		urlEncode = this.dectedEncode(url);
		if(urlEncode == null) {
			return;
		}
		this.singContext(url);
		Iterator<TableContext> it = htmlContext.iterator();
		if(htmlContext.size() == 0) {
			return;
		}
		totalNumber = htmlContext.size();
		// 分析表单集合
		while(it.hasNext()) {
			TableContext tableContext = it.next();
			this.tableNumber = tableContext.getTableRow();
			if((tableContext.getTableRow() == this.channelNumber) || (this.channelNumber == -1)) {
				List<Object> linkList = tableContext.getLinkList();
				
				// 如果没有任何连接
				if((linkList == null) || (linkList.size() == 0)) {
					continue;
				}
				
				Iterator<Object> h1 = linkList.iterator();
				// 分析单个表单
				while(h1.hasNext()) {
					LinkTag linkTag = (LinkTag) h1.next();
					// 非法link
					if(this.isValidLink(linkTag.getLink()) == OUTDOMAINLINKTYPE) {
						continue;
					}
					if(linkTag.getLinkText().length() < 8) {
						continue;
					}
					
					// 过滤无效link
					if(splitManager.isChannelList(linkTag.getLinkText()) != COMMONLINKTYPE) {
						continue;
					}
				}
				
			}
		}
	} 
	
	
	/**
	 * 
	 * 判断是否有效连接
	 */
	public int isValidLink(String link) {
		Matcher matcher = pattern.matcher(link);
		while (matcher.find()) {

//			int start = matcher.start(2);
			int end = matcher.end(2);
			String postUrl = link.substring(end).trim();
			// 如果是目录型连接
			if ((postUrl.length() == 0) || (postUrl.indexOf(".") < 0)) {
				return CHANNELLINKTYPE;
			} else {
				Matcher matcherPost = patternPost.matcher(link);
				if (matcherPost.find()) {
					return COMMONLINKTYPE;
				} else {
					return OUTDOMAINLINKTYPE;
				}
			}
		}
		return OUTDOMAINLINKTYPE;
	}

	
	/**
	 * 收集HTML页面信息
	 * @param url
	 */
	public void singContext(String url) {
		try {
			Parser parser = new Parser(url);
			parser.setEncoding(urlEncode);
			tableNumber = 0;
			for(NodeIterator e = parser.elements(); e.hasMoreNodes();) {
				Node node = e.nextNode();
				if(node  instanceof Html) {
					this.extrachHtml(node);
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 递归钻取信息
	 * @param node
	 * @return
	 */
	public List<Node> extrachHtml(Node nodeP) {
		NodeList nodeList = nodeP.getChildren();
		boolean b1 = false;
		if((nodeList == null) || (nodeList.size() == 0)) {
			return null;
		}
		if((nodeP instanceof TableTag) || (nodeP instanceof Div)) {
			b1 = true;
		}
		List<Node> tableList = new ArrayList<Node>();
		try {
			for(NodeIterator e = nodeList.elements(); e.hasMoreNodes();) {
				Node node = (Node) e.nextNode();
				if(node instanceof LinkTag) {
					tableList.add(node);
				} else if(node instanceof ScriptTag || node instanceof StyleTag || node instanceof SelectTag) {
					
				} else if(node instanceof TextNode) {
					if(node.getText().trim().length() > 0) {
						tableList.add(node);
					}
				} else {
					List<Node> tempList = this.extrachHtml(node);
					if((tempList != null ) && (tableList.size() > 0)) {
						Iterator<Node> it = tableList.iterator();
						while(it.hasNext()) {
							tableList.add(it.next());
						}
					}
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
		if((tableList != null) && (tableList.size() > 0 )) {
			if(b1) {
				TableContext tableContext = new TableContext();
				tableContext.setLinkList(new ArrayList<Object>());
				tableContext.setTextBuffer(new StringBuffer());
				tableNumber++;
				tableContext.setTableRow(tableNumber);
				Iterator<Node> it = tableList.iterator();
				while(it.hasNext()) {
					Node node  = it.next();
					if(node instanceof LinkTag) {
						tableContext.getLinkList().add(node);
					} else {
						tableContext.getTextBuffer().append(this.collapse(node.getText().replaceAll(" ", "")));
					}
				}
				htmlContext.add(tableContext);
				return null;
			} else {
				return tableList;
			}
		}
		return null;
	}
	
	/**
	 * 去除无效字符
	 * @param string
	 * @return
	 */
	protected String collapse(String string) {
		int chars = 0;
		int length = 0;
		int state = 0;
		char character;
		StringBuffer buffer = new StringBuffer();
		chars = string.length();
		if( chars != 0) {
			length = buffer.length();
			state = ((0 == length) || (buffer.charAt(length - 1) == ' ') || ((NEWLINE_SIZE < length) 
					&& buffer.substring(length - NEWLINE_SIZE, length).equals(NEWLINE))) ? 0 : 1;
			for(int i = 0; i < chars; i++) {
				character = string.charAt(i);
				switch (character) {
				case '\u0020' :
				case '\u0009' :
				case '\u000C' :
				case '\u200B' :
				case '\u00a0' :
				case '\r':
				case '\n' :
					if(0 != state) {
						state = 1;
					}
					break;
				default:
					if(1 == state) {
						buffer.append(' ');
					} 
					state = 2;
					buffer.append(character);
				}
			}
		}
		return buffer.toString();
	}
	
	/**
	 * 检测字符级
	 * @param url
	 * @return
	 */
	private String dectedEncode(String url) {
		String[] encodes = oriEncode.split(",");
		for(int i = 0; i < encodes.length; i++) {
			if(this.dectedCode(url, encodes[i])) {
				return encodes[i];
			}
		}
		return null;
	}
	
	private boolean dectedCode(String url, String encode) {
		try {
			Parser parser = new Parser(url);
			parser.setEncoding(encode);
			for(NodeIterator e = parser.elements(); e.hasMoreNodes();) {
				Node node = (Node) e.nextNode();
				if(node instanceof Html) {
					return true;
				}
			}
		} catch(Exception e) {}
		return false;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<TableContext> getHtmlContext() {
		return htmlContext;
	}

	public void setHtmlContext(List<TableContext> htmlContext) {
		this.htmlContext = htmlContext;
	}

	public String getUrlEncode() {
		return urlEncode;
	}

	public void setUrlEncode(String urlEncode) {
		this.urlEncode = urlEncode;
	}

	public int getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(int tableNumber) {
		this.tableNumber = tableNumber;
	}

	public int getChannelNumber() {
		return channelNumber;
	}

	public void setChannelNumber(int channelNumber) {
		this.channelNumber = channelNumber;
	}

	public int getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getUrlDomaiPattern() {
		return urlDomaiPattern;
	}

	public void setUrlDomaiPattern(String urlDomaiPattern) {
		this.urlDomaiPattern = urlDomaiPattern;
	}

	public String getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public Pattern getPatternPost() {
		return patternPost;
	}

	public void setPatternPost(Pattern patternPost) {
		this.patternPost = patternPost;
	}

	public String getOriEncode() {
		return oriEncode;
	}
	
	
}
