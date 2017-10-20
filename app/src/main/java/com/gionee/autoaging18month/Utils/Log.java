package com.gionee.autoaging18month.Utils;
/**
 * 打印日志
 */
public class Log {
	private static final String  TAG    = "gionee.os.autotest";
	private static final boolean isShow = true; 
	public static void i(String msg){
		if (isShow) {
			android.util.Log.i(TAG, msg);
		}
	}
	public static void d(String msg){
		if (isShow) {
			android.util.Log.d(TAG, msg);
		}
	}
}
