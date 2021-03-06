package com.jerry.crawler.components.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年8月29日
 */
public class HTTPProxy {

//	private static final Logger logger = Logger.getLogger(HTTPProxy.class);

	// 连接超时时间  
	private static final int CONNECTION_TIMEOUT = 10 * 1000;
	
	// 连接读取时间  
	private static final int READER_TIMEOUT = 5 * 1000;
	
	private static final String DEFAULT_CHARSET = "utf-8";
	
	// 默认最大访问次数  
	private int maxConnectTimes = 3;
	
    private static HttpClient httpClient = new HttpClient();  
    
    // 网页编码方式  
    private String charset = DEFAULT_CHARSET;  

    // 链接源代码  
    private String sourceCode = "";
    
    // 返回头信息 
    private Header[] requestHeaders = null;
    
    // 返回返回体
    private byte[] responseBody = null;
    
    // 返回contentType
    private String contentType = null;
    
    static {  
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT);  
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(READER_TIMEOUT);  
    }  
    
    /**
     * Get方式访问页面 
     * @param url
     * @param params
     * @param defaultCharset
     * @return
     * @throws IOException 
     * @throws HttpException 
     */
    public boolean get(String url, Map<String, String> params) throws HttpException, IOException {
    	GetMethod method = this.getMethod(url, params);
    	return this.execute(method, url);
    }
    
    /**
     * Post方式访问页面 
     * @param url
     * @param params
     * @param defaultCharset
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public boolean post(String url, Map<String, String> params) throws HttpException, IOException {
    	PostMethod method = this.postMethod(url, params);
    	return this.execute(method, url);
    }
    
    private boolean execute(HttpMethod method, String url) throws HttpException, IOException {
    	int connectTimes = maxConnectTimes;
    	
    	while(connectTimes > 0) {
    		InputStream is = null;
    		Reader reader = null;
    		BufferedReader buffer = null;
    		try {
		    	if(httpClient.executeMethod(method) == HttpStatus.SC_OK) {
		    		requestHeaders = method.getRequestHeaders();
		    		responseBody = method.getResponseBody();
		    		sourceCode = method.getResponseBodyAsString();
		    		
//		    		String charset = CpdetectorUtil.getStringEncode(sourceCode, DEFAULT_CHARSET);
//		    		sourceCode = new String(sourceCode.getBytes(charset));
		    		
		    		return true;
		    	} else {
		    		connectTimes --;
		    	}
    		} catch(Exception e) {
    			e.printStackTrace();
    			connectTimes--;
    		} finally {
    			if(buffer != null) {
    				buffer.close();
    			}
    			
    			if(reader != null) {
    				reader.close();
    			}
    			
    			if(is != null) {
    				is.close();
    			}
    		}
    	}
	    return false;
    }
    
	/**
	 * 设置get请求参数 
	 * @param url
	 * @param params
	 * @return
	 */
	private GetMethod getMethod(String url, Map<String, String> params) {
		GetMethod method = new GetMethod(url);
		if(params == null) {
			return method;
		}
		
		Iterator<Entry<String, String>> it = params.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			method.setRequestHeader(key, value);
		}
		
		return method;
	}

	/**
	 * 设置post请求参数 
	 * @param url
	 * @param params
	 * @return
	 */
	private PostMethod postMethod(String url, Map<String, String> params) {
		PostMethod method = new PostMethod(url);
		if(params == null) {
			return method;
		}
		
		Iterator<Entry<String, String>> it = params.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			method.setParameter(key, value);
		}
		
		return method;
	}
	
	/**
	 * 获取网页源代码 
	 * @return
	 */
	public String getSourceCode() {
		return sourceCode;
	}
	
	/**
	 * 获取网页返回头信息 
	 * @return
	 */
	public Header[] getRequestHeaders() {
		return requestHeaders;
	}
	
	/**
	 * 获取ResponseBody 
	 * @return
	 */
	public byte[] getResponseBody() {
		return responseBody;
	}
	
	/**
	 * 返回contentType
	 * @return
	 */
	public String getContentType() {
		return contentType;
	}
	
	/**
	 * 设置连接超时时间 
	 * @param connectionTimeout
	 */
	public void setConnectTimeout(int connectionTimeout) {
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTimeout);  
	}
	
	/**
	 * 设置读取超时时间 
	 * @param readTimeout
	 */
	public void setReadTimeout(int readTimeout){  
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(readTimeout);  
    }
	
	/**
	 * 设置最大访问次数，链接失败的情况下使用 
	 * @param maxConnectTimes
	 */
	public void setMaxConnectTimes(int maxConnectTimes) {
		this.maxConnectTimes = maxConnectTimes;
	}
	
	/**
	 * 设置默认编码方式 
	 * @param charset
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}
}
