package com.jerry.crawler.example.extract;

import java.util.Date;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月1日
 */
public class ChannelLink {
	
	private int linkid;   
	
    private int scid;   
    
    private String linktext;   
    
    private String link;   
    
    private int level;   
    
    private Date creatime;  
    
    private int click;   
    
    private String sitenme;   
    
    private int siteid;   
    
    private String encode;   
    
    private String filterString;

	public int getLinkid() {
		return linkid;
	}

	public void setLinkid(int linkid) {
		this.linkid = linkid;
	}

	public int getScid() {
		return scid;
	}

	public void setScid(int scid) {
		this.scid = scid;
	}

	public String getLinktext() {
		return linktext;
	}

	public void setLinktext(String linktext) {
		this.linktext = linktext;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Date getCreatime() {
		return creatime;
	}

	public void setCreatime(Date creatime) {
		this.creatime = creatime;
	}

	public int getClick() {
		return click;
	}

	public void setClick(int click) {
		this.click = click;
	}

	public String getSitenme() {
		return sitenme;
	}

	public void setSitenme(String sitenme) {
		this.sitenme = sitenme;
	}

	public int getSiteid() {
		return siteid;
	}

	public void setSiteid(int siteid) {
		this.siteid = siteid;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public String getFilterString() {
		return filterString;
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}
    
}
