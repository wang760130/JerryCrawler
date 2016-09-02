package com.jerry.crawler.example.extract;

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

import com.jerry.crawler.common.Global;
import com.jerry.crawler.common.Path;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月1日
 */
public class ExtractContext {

	private static final StringBuffer LINE_START = new StringBuffer("<p style=\"text-indent:2em\">");
	private static final StringBuffer LINE_END = new StringBuffer("</p>" + Global.LINE_SIGN);
	
	/**
	 * 收集HTML页面信息
	 * @param link
	 */
	public void makeContext(ChannelLink channelLink) {
		
		String title = "<title>{0}</title>";
		String keywords = "<meta name=\"keywords\" content=\"{0}\" />";
		String description = "<meta name=\"description\" content=\"{0}\" />";

		
		String titleTag = "#title";
		String keywordsTag = "#keywords";
		String descriptionTag = "#description";

		
		try {
			String link = channelLink.getLink();
			String url = this.getLinkUrl(link);
			Parser parser = new Parser(link);
			parser.setEncoding(channelLink.getEncode());
			
			for(NodeIterator i = parser.elements(); i.hasMoreNodes();) {
				Node node = (Node)i.nextNode();
				if(node instanceof Html) {
					PageContext context = new PageContext();
					context.setNumber(0);
					context.setTextBuffer(new StringBuffer());
					
					// 抓取出内容
					this.extractHtml(node, context, url);
					
					BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(Path.EXTRACT_SRC_FILE), "UTF-8"));
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Path.EXTRACT_DEST_FILE), "UTF-8"));
					
					String lineContext = context.getTextBuffer().toString();
					String line = null;
					
					while((line = reader.readLine()) != null) {
						int start = 0;
								
						start = line.indexOf(titleTag);
						if(start >= 0) {
							title = title.replace("{0}", channelLink.getLinktext());
							writer.write(title + Global.LINE_SIGN);
							continue;
						}
						
						start = line.indexOf(keywordsTag);
						if(start >= 0) {
							keywords = keywords.replace("{0}", channelLink.getLinktext());
							writer.write(keywords + Global.LINE_SIGN);
							continue;
						}
						
						start = line.indexOf(descriptionTag);
						if(start >= 0) {
							description = description.replace("{0}",channelLink.getLinktext());
							writer.write(description + Global.LINE_SIGN);
							continue;
						}
						writer.write(line + Global.LINE_SIGN_SIZE);
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
	 * @param pnode
	 * @param context
	 * @param url
	 * @return
	 */
	private List<Object> extractHtml(Node pnode, PageContext context, String url) {
		NodeList nodeList = pnode.getChildren();
		List<Object> tableList = new ArrayList<Object>();
		StringBuffer temp = null;
		
		if(nodeList == null || nodeList.size() == 0) {
			if(pnode instanceof ParagraphTag) {
				temp = new StringBuffer();
				temp.append("<p style=\"text-indent:2em\">");
				tableList.add(temp);
				
				temp = new StringBuffer();
				temp.append("</p>").append(Global.LINE_SIGN);
				tableList.add(temp);
				
				return tableList;
			}
			
			return null;
		}
		
		if(pnode instanceof ParagraphTag) {
			tableList = new ArrayList<Object>();
			temp = new StringBuffer();
			temp.append("<p style=\"text-indent:2em\">");
			tableList.add(temp);
			
			this.extractPatagraph(pnode, url, tableList);
			
			temp = new StringBuffer();
			temp.append("</p>").append(Global.LINE_SIGN);
			tableList.add(temp);
			return tableList;
		}
		
		try {
			for(NodeIterator i = nodeList.elements(); i.hasMoreNodes();) {
				Node node = i.nextNode();
				if(node instanceof LinkTag) {
					tableList.add(node);
					this.setLinkImg(node, url);
				} else if(node instanceof ImageTag) {
					ImageTag image = (ImageTag) node;
					if(image.getImageURL().toLowerCase().indexOf("http://") < 0) {
						image.setImageURL(url + image.getImageURL());
					} else {
						image.setImageURL(image.getImageURL());
					}
					tableList.add(node);
				} else if(node instanceof TextNode) {
					if(node.getText().length() > 0) {
						temp = new StringBuffer();
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
					
					List<Object> tempList = this.extractHtml(node, context, url);
					if(tempList != null && tempList.size() > 0) {
						Iterator<Object> it = tempList.iterator();
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
		
		boolean b1 = false;
		if(pnode instanceof TableTag || pnode instanceof Div) {
			b1 = true;
		}
		
		if(tableList != null && tableList.size() > 0) {
			if(b1) {
				temp = new StringBuffer();
				
				int wordSize = 0;
				StringBuffer node = null;
				int status = 0;
				
				Iterator<Object> it = tableList.iterator();
				
				while(it.hasNext()) {
					Object k = it.next();
					
					if(k instanceof LinkTag) {
						if(status == 0) {
							temp.append(LINE_START);
							status = 1;
						}
						
						node = new StringBuffer(((LinkTag) k).toHtml());
						temp.append(node);
					} else if(k instanceof ImageTag) {
						if(status == 0) {
							temp.append(LINE_START);
							status = 1;
						}
						
						node = new StringBuffer(((ImageTag)k).toHtml());
						temp.append(node);
					} else if(k instanceof TableTag) {
						if(status == 0) {
							temp.append(LINE_START);
							status = 1;
						} 
						
						node = new StringBuffer(((TableTag) k).toHtml());
						temp.append(node);
					} else if(k instanceof Div) {
						if(status == 0) {
							temp.append(LINE_START);
							status = 1;
						}
						
						node = new StringBuffer(((Div) k).toHtml());
						temp.append(node);
					} else {
						node = (StringBuffer) k;
						if(status == 0) {
							if(node.indexOf("<p") < 0) {
								temp.append(LINE_START);
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
									temp.append(LINE_END);
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
					temp.append(LINE_END);
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
	 * @param url
	 * @param tableList
	 * @return
	 */
	private List<Object> extractPatagraph(Node pnode, String url, List<Object> tableList) {
		NodeList nodeList = pnode.getChildren();
		StringBuffer temp = null;
		
		if(nodeList == null || nodeList.size() == 0) {
			if(pnode instanceof ParagraphTag) {
				temp = new StringBuffer();
				temp.append(LINE_START);
				tableList.add(temp);
				
				temp = new StringBuffer();
				temp.append(LINE_END);
				tableList.add(temp);
				return tableList;
			}
			return null;
		}
		
		try {
			for(NodeIterator i = nodeList.elements(); i.hasMoreNodes();) {
				Node node = i.nextNode();
				if (node instanceof ScriptTag || node instanceof StyleTag || node instanceof SelectTag) {
					
				} else if(node instanceof LinkTag) {
					tableList.add(node);
					this.setLinkImg(node, url);
				} else if(node instanceof ImageTag) {
					ImageTag image = (ImageTag) node;
					if(image.getImageURL().toLowerCase().indexOf("http://") < 0) {
						image.setImageURL(url + image.getImageURL());
					} else {
						image.setImageURL(image.getImageURL());
					}
					tableList.add(node);
				} else if(node instanceof TextNode) {
					if(node.getText().trim().length() > 0) {
						String text = this.collspse(node.getText().replaceAll("&nbsp;", "").replaceAll(" ", ""));
						temp = new StringBuffer();
						temp.append(text);
						tableList.add(temp);
					}
				} else if(node instanceof Span) {
					StringBuffer spanWord = new StringBuffer();
					this.getSpanWord(node, spanWord);
					if(spanWord != null && spanWord.length() > 0) {
						String text = this.collspse(spanWord.toString().replaceAll("&nbsp;", "").replaceAll(" ", ""));
						temp = new StringBuffer();
						temp.append(text);
						tableList.add(temp);
					}
				} else if(node instanceof TagNode) {
					String tag = node.toHtml();
					if(tag.length() <= 10) {
						tag = tag.toLowerCase();
						if(tag.indexOf("strong") >= 0 || tag.indexOf("b") > 0) {
							temp = new StringBuffer();
							temp.append(tag);
							tableList.add(temp);
						}
					} else {
						if(node instanceof TableTag || node instanceof Div) {
							TableValid tableValid = new TableValid();
							this.isValidTable(node, tableValid);
							if(tableList.add(node));
							continue;
						}
						this.extractPatagraph(node, url, tableList);
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
	 * @param pnode
	 * @param url
	 */
	private void setLinkImg(Node pnode, String url) {
		NodeList nodeList = pnode.getChildren();
		if(nodeList != null && nodeList.size() > 0) {
			try {
				for(NodeIterator i = nodeList.elements(); i.hasMoreNodes();) {
					Node node = i.nextNode();
					if (node instanceof ImageTag) {
						ImageTag image = (ImageTag) node;
						
						if(image.getImageURL().toUpperCase().indexOf("http://") < 0) {
							image.setImageURL(url + image.getImageURL());
						} else {
							image.setImageURL(image.getImageURL());
						}
					}
				}
			} catch (ParserException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void getSpanWord(Node pnode, StringBuffer spanWord) {
		NodeList nodeList = pnode.getChildren();
		
		try {
			for(NodeIterator i = nodeList.elements(); i.hasMoreNodes();) {
				Node node = i.nextNode();
				if(node instanceof ScriptTag || node instanceof StyleTag || node instanceof SelectTag) {
					
				} else if(node instanceof TextNode) {
					spanWord.append(node.getText());
				} else if(node instanceof Span) {
					this.getSpanWord(node, spanWord);
				} else if(node instanceof ParagraphTag) {
					this.getSpanWord(node, spanWord);
				} else if(node instanceof TagNode) {
					String tag = node.toHtml().toLowerCase();
					if(tag.length() <= 10) {
						if(tag.indexOf("strong") >= 0 || tag.indexOf("b") >= 0) {
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
	 * @param pnode
	 * @param tableValid
	 */
	private void isValidTable(Node pnode, TableValid tableValid) {
		NodeList nodeList = pnode.getChildren();
		
		// 如果该表单没有子节点则返回
		if(nodeList == null || nodeList.size() == 0) {
			return ;
		}
		
		try {
			for(NodeIterator i = nodeList.elements(); i.hasMoreNodes();) {
				Node node = i.nextNode();
				if(node instanceof ScriptTag || node instanceof StyleTag || node instanceof SelectTag) {
					return ;
				} else if(node instanceof TableColumn) {
					return ;
				} else if(node instanceof TableRow) {
					TableColumnValid tableColumnValid = new TableColumnValid();
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
							if(tableColumnValid.getTdNum() == 0) {
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
					} else {
						this.isValidTable(node, tableValid);
					}
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
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
		if(nodeList == null || nodeList.size() == 0) {
			return ;
		}
		
		try {
			for(NodeIterator i = nodeList.elements(); i.hasMoreNodes();) {
				Node node = i.nextNode();
				
				if(node instanceof TableTag || node instanceof Div 
						|| node instanceof TableRow || node instanceof TableHeader) {
					tableColumnValid.setValid(false);
					return ;
				} else if(node instanceof ScriptTag || node instanceof StyleTag || node instanceof SelectTag) {
					tableColumnValid.setValid(false);
					return ;
				} else if(node instanceof TableColumn) {
					tableColumnValid.setTdNum(tableColumnValid.getTdNum());
				} else {
					this.findTD(node, tableColumnValid);
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}
	
	private String collspse(String str) {
		int chars = 0;
		int length = 0;
		int state = 0;
		char character;
		
		StringBuffer sb = new StringBuffer();
		chars = str.length();
		
		if(chars != 0) {
			length = sb.length();
			state = ((0 == length) || (sb.charAt(length - 1) == ' ') || ((Global.LINE_SIGN_SIZE <= length)
					&& sb.substring(length - Global.LINE_SIGN_SIZE, length).equals(Global.LINE_SIGN))) ? 0 : 1;
			
			for(int i = 0; i < chars; i++) {
				character = str.charAt(i);
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
						sb.append(' ');
					}
					state = 2;
					sb.append(character);
					break;
				}
			}
		}
		
		return sb.toString();
	}
	
	public static void main(String[] args) {
		ChannelLink channelLink = new ChannelLink();
		channelLink.setLink("http://china.ynet.com/3.1/1609/02/11681637.html");
		channelLink.setEncode("utf-8");
		channelLink.setLinktext("test");
		ExtractContext context  = new ExtractContext();
		context.makeContext(channelLink);
	}
}
