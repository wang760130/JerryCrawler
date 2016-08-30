package com.jerry.crawler.components.crawler;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
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
	
	// 默认最大访问次数  
	private int maxConnectTimes = 3;
	
    private static HttpClient httpClient = new HttpClient();  
    
    // 网页默认编码方式  
    private String charset = "iso-8859-1";  

    // 链接源代码  
    private String sourceCode = "";
    
    // 返回头信息 
    private Header[] headers = null;
    
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
    public boolean get(String url, Map<String, String> params, String defaultCharset) throws HttpException, IOException {
    	GetMethod method = this.getMethod(url, params);
    	return this.execute(method, url, defaultCharset);
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
    public boolean post(String url, Map<String, String> params, String defaultCharset) throws HttpException, IOException {
    	PostMethod method = this.postMethod(url, params);
    	return this.execute(method, url, defaultCharset);
    }
    
    private boolean execute(HttpMethod method, String url, String defaultCharset) throws HttpException, IOException {
    	int connectTimes = maxConnectTimes;
    	
    	while(connectTimes > 0) {
    		InputStream is = null;
    		Reader reader = null;
    		BufferedReader buffer = null;
    		try {
		    	if(httpClient.executeMethod(method) == HttpStatus.SC_OK) {
		    		// 获取头信息 
		    		headers = method.getRequestHeaders();
		    		
		    		is = method.getResponseBodyAsStream();
		    		reader = new InputStreamReader(is, charset);
		    		buffer = new BufferedReader(reader);
		    		StringBuffer sb = new StringBuffer();
		    		String line = null;
		    		while((line = buffer.readLine()) != null) {
		    			sb.append(line);
		    			sb.append("\n");
		    		}
		    		sourceCode = sb.toString();
		    		
		    		is = new ByteArrayInputStream(sourceCode.getBytes(charset));
		    		String charset = CpdetectorUtil.getInputStreamEncode(is, defaultCharset);
		    		
		    		if(!charset.toLowerCase().equals(charset.toLowerCase())) {
		    			sourceCode = new String(sourceCode.getBytes(charset));
		    		}
		    		
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
	public Header[] getHeader() {
		return headers;
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
