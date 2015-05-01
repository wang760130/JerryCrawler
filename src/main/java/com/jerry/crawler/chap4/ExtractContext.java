package com.jerry.crawler.chap4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.Html;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.StyleTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableHeader;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * 正文抽取主程序
 * @author Jerry Wang
 *
 */
public class ExtractContext {
	private static final String lineSign = System.getProperty("line.separator");
	private static final int lineSignSize = lineSign.length();
	
	/**
	 * 定义系统下上文
	 */
//	public static final ApplicationContext context = new ClassPathApplicationContext(new String[] {
//	});
	
	/**
	 * 收集HTML页面信息
	 * @param channelLinkDO
	 */
	public void makeContext(ChannelLinkDO channelLinkDO) {
		String metakeywords = "<META content={0} name=keyword>";
		String metatitle = "<TITLE>{0}</TITLE>";
		String metadesc = "<META content={0} name=description>";
		String netshap = "<p>正文快照：时间{0}</p>";
		
		String tempLeate = "<LI class=active><A href=\"{0}\" target=_blank>{1}</A></LI>";
		String crop = "<p><A href=\"{0}\" target=_blank>{1}</A></p>";
		
		
		try {
			String siteUrl = this.getLinkUrl(channelLinkDO.getLink());
			Parser parse = new Parser(channelLinkDO.getLink());
			parse.setEncoding(channelLinkDO.getEncode());
			for(NodeIterator i = parse.elements(); i.hasMoreNodes();) {
				Node node = (Node)i.nextNode();
				if(node instanceof Html) {
					PageContext context = new PageContext();
					context.setNumber(0);
					context.setTextBuffer(new StringBuffer());
					
					// 抓取出内容
					this.extractHtml(node, context, siteUrl);
					StringBuffer testContext = context.getTextBuffer();
					String srcfilePath = "D://context.vm";
					String destfilePath = "D://text.html";
					BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(srcfilePath), "UTF-8"));
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destfilePath), "UTF-8"));
					String lineContext = context.getTextBuffer().toString();
					String line = null;
					while((line = reader.readLine()) != null) {
						int start = line.indexOf("#content");
						if(start >= 0) {
							String tempCrop = crop.replace("{0}", channelLinkDO.getLink());
							tempCrop = tempCrop.replace("{1}", "原文连接：" + channelLinkDO.getLink());
							writer.write(tempCrop + lineSign);
							writer.write(netshap + lineSign);
							writer.write(lineContext + lineSign);
							continue;
						}
						
						start = line.indexOf("#titledesc");
						if(start >= 0) {
							String tempLine = tempLeate.replace("{0}", "test.htm");
							tempLine = tempLine.replace("{1}", "标题：" + channelLinkDO.getLinktext());
							writer.write(tempLine + lineSign);
							continue;
						}
						
						start = line.indexOf("#metatitle");
						if(start >= 0) {
							metatitle = metatitle.replace("{0}", channelLinkDO.getLinktext());
							writer.write(metatitle + lineSign);
							continue;
						}
						
						start = line.indexOf("#metadesc");
						if(start >= 0) {
							metadesc = metadesc.replace("{0}",channelLinkDO.getLinktext());
							writer.write(metadesc + lineSign);
							continue;
						}
						writer.write(line + lineSignSize);
					}
					writer.flush();
					writer.close();
					reader.close();
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
	
	/**
	 * 递归钻取正文信息
	 * @param pnode
	 * @param context
	 * @param siteUrl
	 * @return
	 * @throws Exception
	 */
	private List extractHtml(Node pnode, PageContext context, String siteUrl) {
		NodeList nodeList = pnode.getChildren();
		boolean b1 = false;
		if((nodeList == null) || (nodeList.size() == 0)) {
			if(pnode instanceof ParagraphTag) {
				ArrayList<StringBuffer> tableList = new ArrayList<StringBuffer>();
				StringBuffer temp = new StringBuffer();
				temp.append("<p style=\"text-indent:2em\">");
				tableList.add(temp);
				temp = new StringBuffer();
				temp.append("</p>").append(lineSign);
				tableList.add(temp);
				return tableList;
			}
			return null;
		}
		
		if((pnode instanceof TableTag) || (pnode instanceof Div)) {
			b1 = true;
		}
		
		if(pnode instanceof ParagraphTag) {
			ArrayList<StringBuffer> tableList = new ArrayList<StringBuffer> ();
			StringBuffer temp = new StringBuffer();
			temp.append("<p style=\"text-indent:2em\">");
			tableList.add(temp);
			this.extractPatagraph(pnode, siteUrl, tableList);
			temp = new StringBuffer();
			temp.append("</p>").append(lineSign);
			tableList.add(temp);
			return tableList;
		}
		
		ArrayList tableList = new ArrayList();
		
		try {
			for(NodeIterator i = nodeList.elements(); i.hasMoreNodes();){
				Node node = (Node) i.nextNode();
				if(node instanceof LinkTag) {
					tableList.add(node);
					this.setLinkImg(node, siteUrl);
				} else if (node instanceof ImageTag) {
					ImageTag image = (ImageTag) node;
					if(image.getImageURL().toLowerCase().indexOf("http://") < 0) {
						image.setImageURL(siteUrl + image.getImageURL());
					} else {
						image.setImageURL(image.getImageURL());
					}
					tableList.add(node);
				} else if (node instanceof TextNode) {
					if(node.getText().length() > 0) {
						StringBuffer temp = new StringBuffer();
						String text = this.collspse(node.getText().replaceAll("&nbsp;", "").replaceAll(" ", ""));
						temp.append(text.trim());
						tableList.add(temp);
					}
				} else {
					if(node instanceof TableTag || node instanceof Div) {
						TableValid tableValid = new TableValid();
						this.isValidTable(node, tableValid);
						if(tableValid.getTrnum() > 2) {
							tableList.add(node);
							continue;
						}
					}
					
					List tempList = this.extractHtml(node, context, siteUrl);
					if((tempList != null) && (tempList.size() > 0)) {
						Iterator it = tempList.iterator();
						while(it.hasNext()) {
							tableList.add(it.next());
						}
					}
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
			return null;
		} 
		
		if((tableList != null ) && (tableList.size() > 0)) {
			if(b1) {
				StringBuffer temp = new StringBuffer();
				Iterator it = tableList.iterator();
				int wordSize = 0;
				StringBuffer node;
				int status = 0;
				StringBuffer lineStart = new StringBuffer("<p style=\"text-indent:2em\">");
				StringBuffer lineEnd = new StringBuffer("</p>" + lineSign);
				while(it.hasNext()) {
					Object k = it.next();
					if(k instanceof LinkTag) {
						if(status == 0) {
							temp.append(lineStart);
							status = 1;
						}
						node = new StringBuffer(((LinkTag) k ).toHtml());
						temp.append(node);
					} else if(k instanceof ImageTag) {
						if(status == 0) {
							temp.append(lineStart);
							status = 1;
						}
						node = new StringBuffer(((ImageTag) k).toHtml());
						temp.append(node);
					} else if(k instanceof TableTag) {
						if(status == 0) {
							temp.append(lineStart);
							status = 1;
						}
						
						node = new StringBuffer(((TableTag) k).toHtml());
						temp.append(node);
						
					} else if(k instanceof Div) {
						if(status == 0 ) {
							temp.append(lineStart);
							status = 1;
						}
						node = new StringBuffer(((Div)k).toHtml());
						temp.append(node);
					} else {
						node = (StringBuffer) k;
						if(status == 0) {
							if(node.indexOf("<p") < 0) {
								temp.append(lineStart);
								temp.append(node);
								wordSize = wordSize + node.length();
								status = 1;
							} else {
								temp.append(node);
								status = 1;
							}
 						} else if(status == 1) {
 							if(node.indexOf("</p") < 0) {
 								if(node.indexOf("<p") < 0) {
 									temp.append(node);
 									wordSize = wordSize + node.length();
 								} else {
 									temp.append(lineEnd);
 									temp.append(node);
 									status = 1;
 								}
 							} else {
 								temp.append(node);
 								status = 0;
 							}
 						}
					}
				} 
				
				if(status == 1) {
					temp.append(lineEnd);
				}
				
				if(wordSize > context.getNumber()) {
					context.setNumber(wordSize);
					context.setTextBuffer(temp);
				}
				return null;
			} else {
				return tableList;
			}
		}
		return null;
	}
	
	/**
	 * 钻取段落中的内容
	 * @param pnode
	 * @param siteUrl
	 * @param tableList
	 * @return
	 */
	private List extractPatagraph(Node pnode, String siteUrl, List tableList) {
		NodeList nodeList = pnode.getChildren();
		if((nodeList == null) || (nodeList.size() == 0)) {
			if(pnode instanceof ParagraphTag) {
				StringBuffer temp = new StringBuffer();
				temp.append("<p style=\"text-indent:2em\">");
				tableList.add(temp);
				temp = new StringBuffer();
				temp.append("</p>").append(lineSign);
				tableList.add(temp);
				return tableList;
			}
			return null;
		}
		
		try {
			for(NodeIterator i = nodeList.elements(); i.hasMoreNodes();) {
				Node node = (Node) i.nextNode();
				if(node instanceof ScriptTag || node instanceof StyleTag || node instanceof SelectTag) {
					
				} else if (node instanceof LinkTag) {
					tableList.add(node);
					this.setLinkImg(node, siteUrl);
				} else if (node instanceof ImageTag) {
					ImageTag image = (ImageTag) node;
					if(image.getImageURL().toLowerCase().indexOf("http://") < 0) {
						image.setImageURL(siteUrl + image.getImageURL()); 
					} else {
						image.setImageURL(image.getImageURL());
					}
					tableList.add(node);
 				} else if (node instanceof TextNode) {
 					if(node.getText().trim().length() > 0) {
 						String text = this.collspse(node.getText().replaceAll("&nbsp;", "").replaceAll(" ", ""));
 						StringBuffer temp = new StringBuffer();
 						temp.append(text);
 						tableList.add(temp);
 					}
 				} else if (node instanceof Span) {
 					StringBuffer spanWord = new StringBuffer();
 					this.getSpanWord(node, spanWord);
 					if((spanWord != null) && (spanWord.length() > 0)) {
 						String text = this.collspse(spanWord.toString().replaceAll("&nbsp;", "").replaceAll(" ", ""));
 						StringBuffer temp = new StringBuffer();
 						temp.append(text);
 						tableList.add(temp);
 					}
 				} else if (node instanceof TagNode) {
 					String tag = node.toHtml();
 					if(tag.length() <= 10) {
 						tag = tag.toLowerCase();
 						if((tag.indexOf("strong") >=0 ) || (tag.indexOf("b") > 0 )) {
 							StringBuffer temp = new StringBuffer();
 							temp.append(tag);
 							tableList.add(temp);
 							
 						}
 					} else {
 						if(node instanceof TableTag || node instanceof Div) {
 							TableValid tableValid = new TableValid();
 							this.isValidTable(node, tableValid);
 							if(tableValid.getTrnum() > 2) {
 								tableList.add(node);
 								continue;
 							}
 							this.extractPatagraph(node, siteUrl, tableList);
 						}
 					}
 				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
		return tableList;
	}
	
	
	/**
	 * 设置图像连接
	 * @param node
	 * @param siteUrl
	 */
	private void setLinkImg(Node pnode, String siteUrl) {
		NodeList nodeList = pnode.getChildren();
		try {
			for(NodeIterator i = nodeList.elements(); i.hasMoreNodes();) {
				Node node = (Node) i.nextNode();
				if(node instanceof ImageTag) {
					ImageTag image = (ImageTag) node;
					if(image.getImageURL().toLowerCase().indexOf("http://") < 0) {
						image.setImageURL(siteUrl + image.getImageURL());
					} else {
						image.setImageURL(image.getImageURL());
					}
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return ;
	}
	
	private void getSpanWord(Node pnode, StringBuffer spanWord) {
		NodeList nodeList = pnode.getChildren();
		
		try {
			for(NodeIterator i = nodeList.elements(); i.hasMoreNodes();) {
				Node node = (Node) i.nextNode();
				if(node instanceof ScriptTag || node instanceof StyleTag ||
						node instanceof SelectTag) {
					
				} else if (node instanceof TextNode) {
					spanWord.append(node.getText());
				} else if (node instanceof Span) {
					this.getSpanWord(node, spanWord);
				} else if (node instanceof ParagraphTag) {
					this.getSpanWord(node, spanWord);
				} else if (node instanceof TagNode) {
					String tag = node.toHtml().toLowerCase();
					if(tag.length() <= 10) {
						if((tag.indexOf("strong") >= 0) || (tag.indexOf("b") >= 0)) {
							spanWord.append(tag);
						}
					}
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return ;
	}
	
	/**
	 * 判断TABLE是否是表单
	 * @param node
	 * @param tableValid
	 */
	private void isValidTable(Node pnode, TableValid tableValid) {
		NodeList nodeList = pnode.getChildren();
		/**
		 * 如果该表单没有子节点则返回
		 */
		if((nodeList == null) || (nodeList.size() == 0)) {
			return ;
		}
		
		try {
			for(NodeIterator i = nodeList.elements(); i.hasMoreNodes();) {
				Node node = (Node) i.nextNode();
				if (node instanceof ScriptTag || node instanceof StyleTag || node instanceof SelectTag) {
					return ;
				} else if (node instanceof TableColumn) {
					return ;
				} else if (node instanceof TableRow) {
					TableColumnValid  tableColumnValid = new TableColumnValid();
					tableColumnValid.setValid(true);
					this.findTD(node, tableColumnValid);
					if(tableColumnValid.isValid()) {
						if(tableColumnValid.getTdNum() < 2) {
							if(tableValid.getTdnum() > 0) {
								return ;
							} else {
								continue;
							}
						} else {
							if(tableValid.getTdnum() == 0) {
								tableValid.setTdnum(tableColumnValid.getTdNum());
								tableValid.setTdnum(tableValid.getTrnum() + 1);
							} else {
								if(tableValid.getTdnum() == tableColumnValid.getTdNum()) {
									tableValid.setTrnum(tableValid.getTrnum() + 1);
								} else {
									return ;
								}
							}
						}
					}
				} else {
					this.isValidTable(node, tableValid);
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
			return ;
		}
		return ;
	}
	
	/**
	 * 判断是否有效TR
	 * @param pnode
	 * @param tableColumnValid
	 */
	private void findTD(Node pnode, TableColumnValid tableColumnValid) {
		NodeList nodeList = pnode.getChildren();
		// 如果该表单没有子节点则返回
		if((nodeList == null) || (nodeList.size() == 0)) {
			return ;
		}
		
		try {
			for(NodeIterator i = nodeList.elements(); i.hasMoreNodes();) {
				Node node = i.nextNode();
				if(node instanceof TableTag || node instanceof Div || 
						node instanceof TableRow || node instanceof TableHeader) {
					tableColumnValid.setValid(false);
					return ;
				} else if (node instanceof ScriptTag || node instanceof StyleTag || node instanceof SelectTag) {
					tableColumnValid.setValid(false);
					return ;
				} else if (node instanceof TableColumn) {
					tableColumnValid.setTdNum(tableColumnValid.getTdNum() + 1);
				} else {
					findTD(node, tableColumnValid);
				}
			}
		} catch(Exception e) {
			tableColumnValid.setValid(false);
			e.printStackTrace();
		}
		
	}
	
	private String collspse(String string) {
		int chars = 0;
		int length = 0;
		int state = 0;
		char character;
		StringBuffer buffer = new StringBuffer();
		chars = string.length();
		if(0 != chars) {
			length = buffer.length();
			state = ((0 == length) || (buffer.charAt(length - 1) == ' ') || ((lineSignSize <= length) &&
					buffer.substring(length - lineSignSize,length).equals(lineSign))) ? 0 : 1;
			for(int i = 0; i < chars; i++) {
				character = string.charAt(i);
				switch (character) {
				case '\u0020':
				case '\u0009':
				case '\u000C':
				case '\u200B':
				case '\u00a0':
				case '\r':
				case '\n':
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
					break;
				}
			}
		}
		return buffer.toString();
	}
	
	public static void main(String[] args) {
		ExtractContext extractContext = new ExtractContext();
		ChannelLinkDO  channelLinkDO = new ChannelLinkDO();
		channelLinkDO.setEncode("gb2312");
		channelLinkDO.setLink("");
		channelLinkDO.setLinktext("test");
		extractContext.makeContext(channelLinkDO);
	}
}
