package com.jerry.crawler.components.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月10日
 */
public class WebGraphMemory {
	
	// 把每个URL映射为一个整数，存储在web图中
	private Map<Integer, String> identifyerToURL;
	
	// 存储web图的Hash表
	private Map<String, Map<String, Integer>> urlToIdentifyer;
	
	/**
	 * 存储入度，其中整数的第一个参数是URL的ID，
	 * 第二个参数存放指向这个URL链接的Map，
	 * Double表示权重
	 */
	private Map<Integer, Map<Integer, Double>> inLinks;
	
	/**
	 * 存储出度，其中整数的第一个参数是URL的ID，
	 * 第二个参数存放网页中的超链接，
	 * Double表示权重
	 */
	private Map<Integer, Map<Integer, Double>> outLinks;
	
	// 图中节点的数目
	private int nodeCount;
	
	public WebGraphMemory() {
		identifyerToURL = new HashMap<Integer, String>();
		urlToIdentifyer = new HashMap<String, Map<String, Integer>>();
		inLinks = new HashMap<Integer, Map<Integer, Double>>();
		outLinks = new HashMap<Integer, Map<Integer, Double>>();
		nodeCount = 0;
	}
	
	public WebGraphMemory(File file) {
		this();
		FileReader filereader = null;
		BufferedReader reader = null;
		try {
			filereader = new FileReader(file);
			reader = new BufferedReader(filereader);
			
			String line = "";
			while((line = reader.readLine()) != null) {
				int index = line.indexOf("->");
				if(index == -1) {
					addLink(line.trim());
				} else {
					String fromUrl = line.substring(0, index).trim();
					String toUrl = line.substring(index + 2).trim();;
					
					Double strength = new Double(1.0);
					index = toUrl.indexOf(" ");
					
					if(index != -1) {
						try {
							strength = new Double(toUrl.substring(index + 1).trim());
							toUrl = toUrl.substring(0, index).trim();
						} catch (Exception e) {
							e.printStackTrace();
						}
						addLink(fromUrl, toUrl, strength);
					}
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
		}
	}

	/**
	 * 根据url制定他的id
	 * @param url
	 * @return
	 */
	public Integer urlToIdentifyer(String url) {
		String host;
		String name;
		
		int index = 0, index2 = 0;
		
		if(url.startsWith("http://")) {
			index = 7;
		} else if(url.startsWith("ftp://")) {
			index = 6;
		}
		
		index2 = url.substring(index).indexOf("/");
		if(index2 != -1) {
			host = url.substring(0, index + index2);
			name = url.substring(index + index2 + 1);
		} else {
			host = url;
			name = "";
		}
		
		Map<String, Integer> map = urlToIdentifyer.get(host);
		if(map == null) {
			return null;
		}
		return map.get(name);
	}
	
	/**
	 * 根据id获得url
	 * @param id
	 * @return
	 */
	public String identifyerToUrl(Integer id) {
		return identifyerToURL.get(id);
	}
	 
	/**
	 * 图中增加一个节点
	 * @param fromUrl
	 * @param toUrl
	 * @param strength
	 */
	private Integer addLink(String link) {
		Integer id = urlToIdentifyer(link);
		if(id == null) {
			id = new Integer(++nodeCount);
			String host;
			String name;
			
			int index = 0, index2 = 0;
			
			if(link.startsWith("http://")) {
				index = 7;
			} else if(link.startsWith("ftp://")) {
				index = 6;
			}
			
			index2 = link.substring(index).indexOf("/");
			if(index2 != -1) {
				host = link.substring(0, index + index2);
				name = link.substring(index + index2 + 1);
			} else {
				host = link;
				name = "";
			}
			
			Map<String, Integer> map = urlToIdentifyer.get(host);
			if(map == null) {
				map = new HashMap<String, Integer>();
				urlToIdentifyer.put(host, map);
			}
			
			map.put(name, id);
			
			identifyerToURL.put(id, link);
			inLinks.put(id, new HashMap<Integer, Double>());
			outLinks.put(id, new HashMap<Integer, Double>());
		}
		return id;
	}
	
	/**
	 * 在两个节点中增加一个对应关系。如果节点不存在，就新创建节点
	 * @param fromUrl
	 * @param toUrl
	 * @param strength
	 */
	private Double addLink(String fromLink, String toLink, Double weight) {
		Integer fromId = addLink(fromLink);
		Integer toId = addLink(toLink);
		return addLink(fromId, toId, weight);
	}
	
	/**
	 * 在两个节点中增加一个对应关系。如果节点不存在，就新创建节点
	 * @param fromLink
	 * @param toLink
	 * @param weight
	 * @return
	 */
	private Double addLink(Integer fromLink, Integer toLink, Double weight) {
		Double aux;
		
		Map<Integer, Double> toLinkMap = inLinks.get(toLink);
		Map<Integer, Double> fromLinkMap = outLinks.get(fromLink);
		
		aux = toLinkMap.get(fromLink);
		if(aux == null) {
			toLinkMap.put(fromLink, weight);
		} else if(aux.doubleValue() < weight.doubleValue()) {
			toLinkMap.put(fromLink, weight);
		} else {
			weight = new Double(aux.doubleValue());
		}
		
		aux = fromLinkMap.get(toLink);
		if(aux == null) {
			fromLinkMap.put(toLink, weight);
		} else if(aux.doubleValue() < weight.doubleValue()) {
			fromLinkMap.put(toLink, weight);
		} else {
			weight = new Double(aux.doubleValue());
			fromLinkMap.put(fromLink, weight);
		}
		
		inLinks.put(toLink, toLinkMap);
		outLinks.put(fromLink, fromLinkMap);
		return weight;
	}
	
	/**
	 * 针对指定的Url返回包含他的所有入度链接的map
	 * @param url
	 * @return
	 */
	public Map<Integer, Double> inLinks(String url) {
		Integer id = urlToIdentifyer(url);
		return inLinks(id);
	}

	/**|
	 * 针对指定的Url返回包含他的所有入度链接的map
	 * @param link
	 * @return
	 */
	public Map<Integer, Double> inLinks(Integer link) {
		if(link == null) {
			return null;
		}
		Map<Integer, Double> aux = inLinks.get(link);
		return aux;
	}
	
	/**
	 * 针对指定的url返回包含它的出度的链接的map
	 * @param url
	 * @return
	 */
	public Map<Integer, Double> outLinks(String url) {
		Integer id = urlToIdentifyer(url);
		return outLinks(id);
	}

	/**
	 * 针对指定的url返回包含它的出度的链接的map
	 * @param id
	 * @return
	 */
	public Map<Integer, Double> outLinks(Integer link) {
		if(link == null) {
			return null;
		}
		Map<Integer, Double> aux = outLinks.get(link);
		return aux;
	}
	
	/***
	 * 返回两个节点之间的权重，如果节点没有链接，就返回0
	 * @param fromLink
	 * @param toLink
	 * @return
	 */
	public Double inLink(String fromLink, String toLink) {
		Integer fromId = urlToIdentifyer(fromLink);
		Integer toId = urlToIdentifyer(toLink);
		return inLink(fromId, toId);
	}

	/**
	 * 返回两个节点之间的权重，如果节点没有链接，就返回0
	 * @param fromLink
	 * @param toLink
	 * @return
	 */
	public Double outLink(String fromLink, String toLink) {
		Integer fromId = urlToIdentifyer(fromLink);
		Integer toId = urlToIdentifyer(toLink);
		return outLink(fromId, toId);
	}
	
	/**
	 * 返回两个节点之间的权重，如果节点没有链接，就返回0
	 * @param fromId
	 * @param toId
	 * @return
	 */
	public Double inLink(Integer fromLink, Integer toLink) {
		Map<Integer, Double> aux = inLinks(toLink);
		if(aux == null) {
			return new Double(0);
		}
		
		Double weight = aux.get(fromLink);
		return (weight == null) ? new Double(0) : weight;
	}
	
	/**
	 * 返回两个节点之间的权重，如果节点没有链接，就返回0
	 * @param fromLink
	 * @param toLink
	 * @return
	 */
	public Double outLink(Integer fromLink, Integer toLink) {
		Map<Integer, Double> aux = outLinks(fromLink);
		if(aux == null) {
			return new Double(0);
		}
		Double weight = aux.get(toLink);
		return (weight == null) ? new Double(0) : weight;
	}

	/**
	 * 把有向图变为无向图
	 */
	public void transformUnidirectional() {
		Iterator<Integer> it = outLinks.keySet().iterator();
		while(it.hasNext()) {
			Integer link1 = it.next();
			Map<Integer, Double> auxMap = outLinks.get(link1);
			Iterator<Integer> it2 = auxMap.keySet().iterator();
			while(it2.hasNext()) {
				Integer link2 = it.next();
				Double weight = auxMap.get(link2);
				addLink(link2, link1, weight);
			}
		}
	}
	
	/**
	 * 删除内部链接，内部链接就是指在同一主机上的链接
	 */
	public void removeIntegernalLinks() {
		int index1;
		Iterator<Integer> it = outLinks.keySet().iterator();
		while(it.hasNext()) {
			Integer link1 = it.next();
			Map<Integer, Double> auxMap = outLinks.get(link1);
			Iterator<Integer> it2 = auxMap.keySet().iterator();
			if(it2.hasNext()) {
				String url1 = identifyerToURL.get(link1);
				
				index1 = url1.indexOf("://");
				if(index1 != -1) {
					url1 = url1.substring(index1 + 3);
				}
				
				index1 = url1.indexOf("/");
				if(index1 != -1) {
					url1 = url1.substring(0, index1);
				}
				
				while(it2.hasNext()) {
					Integer link2 = it.next();
					
					String url2 = identifyerToURL.get(link2);
					index1 = url2.indexOf("://");
					if(index1 != -1) {
						url2 = url1.substring(index1 + 3);
					}
					
					index1 = url2.indexOf("/");
					if(index1 != -1) {
						url2 = url1.substring(0, index1);
					}
					
					if(url1.equals(url2)) {
						auxMap.remove(link2);
						outLinks.put(link1, auxMap);
						auxMap = inLinks.get(link2);
						auxMap.remove(link1);
						inLinks.put(link2, auxMap);
					}
				} 
			}
		} 
	}
	
	/**
	 * 删除内部导航链接
	 */
	public void removeNepotistic() {
		removeIntegernalLinks();
	}
	
	/**
	 * 删除终止URL
	 * @param stopURLs
	 */
	public void remvoeStopLinks(Map<String, Map<String, Integer>> stopURLs) {
		int index1;
		Iterator<Integer> it = outLinks.keySet().iterator();
		while(it.hasNext()) {
			Integer link1 = it.next();
			String url1 = identifyerToURL.get(link1);
			index1 = url1.indexOf("://");
			if(index1 != -1) {
				url1 = url1.substring(index1 + 3);
			}
			
			index1 = url1.indexOf("/");
			if(index1 != -1) {
				url1 = url1.substring(0, index1);
			}
			
			if(stopURLs.containsKey(url1)) {
				outLinks.put(link1, new HashMap<Integer, Double>());
				inLinks.put(link1, new HashMap<Integer, Double>());
			}
		}
	}
	
	public int numNodes() {
		return nodeCount;
	}
}
