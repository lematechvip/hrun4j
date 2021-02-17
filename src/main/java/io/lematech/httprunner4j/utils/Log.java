package io.lematech.httprunner4j.utils;

import org.testng.Reporter;
/**
 * 
* @Title: Log.java 
* @Package org.tspring.atfwa.util 
* @Description: 日志信息输出
* @author tspring2014@gmail.com   
* @date 2014年4月25日 上午11:23:03 
* @version V1.0
 */

public class Log {

	public static int step = 0;

	public static void comment(String logStr) {
		if(RegExpUtil.isFilter(logStr)){
			//Log4j.logInfo(logStr);
			String dateInfo = new TimeString().getSimpleDateFormat();
			Reporter.log("[" + dateInfo + "] " + logStr+ "",false);
		}

	}

	public static void commentStep(String logStr) {
		if(RegExpUtil.isFilter(logStr)){
			//Log4j.logInfo("STEP " + ++step + ": "+logStr);
			String dateInfo = new TimeString().getSimpleDateFormat();
			Reporter.log("[" + dateInfo + "] " + logStr+ "", false);
		}
	}

	public static void comment(String logStr, boolean screen) {
		if(RegExpUtil.isFilter(logStr)){
			//Log4j.logInfo(logStr);
			String dateInfo = new TimeString().getSimpleDateFormat();
			if (screen)
				logStr = "<a href=\"../snapshot/" + logStr + "\" "
						+ "target=\"_blank\" style=\"color:red\">" + logStr
						+ "</a>";
			Reporter.log("[" + dateInfo + "] " + logStr+ "", false);
		}
	}
}
