package com.gionee.eighteenmonth.progress;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Util {

	/**
	 * 开始时清除LogCat中的缓冲
	 */
	public static void clearLogcatBuffer() {
		try {
			Process process = RuntimeHelper.exec(new ArrayList<>(Arrays
					.asList("logcat", "-c")));
			process.waitFor();
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}
	public static void clearLogcatBufferForEvents() {
		try {
			Process process = RuntimeHelper.exec(new ArrayList<>(Arrays
					.asList("logcat", "-b","events","-c")));
			process.waitFor();
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 获得当前日期
	 * 
	 * @return
	 */
	public static String getTime() {
		// yyyyMMddHHmmSS
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());
		return formatter.format(curDate);
	}

	public static <T> T[] toArray(List<T> list, Class<T> clazz) {
		@SuppressWarnings("unchecked")
		T[] result = (T[]) Array.newInstance(clazz, list.size());
		for (int i = 0; i < list.size(); i++) {
			result[i] = list.get(i);
		}
		return result;
	}

}
