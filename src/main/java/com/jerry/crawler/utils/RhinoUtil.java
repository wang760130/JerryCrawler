package com.jerry.crawler.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月1日
 */
public class RhinoUtil {
	
	
	public static Context evaluateString(String script) {
		/* 创建一个Javascript的上下文环境，用来存储Javascript的环境信息 */
		Context cx = Context.enter();
		BufferedReader in = null;
		try {
			/* 初始化Javascript标准对象（例如Object, Function, Array等） */
			Scriptable scope = cx.initStandardObjects();

			/* 读取一个.js文件 */
			File file = new File(script);
			in = new BufferedReader(new FileReader(file));
			String s = "";
			while ((s = in.readLine()) != null) {
				script += s + "\n";
			}

			/* 执行代码 */
			cx.evaluateString(scope, script, "[" + file.getName() + "]", 1,null);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			Context.exit();
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return cx;
	}
	
}
