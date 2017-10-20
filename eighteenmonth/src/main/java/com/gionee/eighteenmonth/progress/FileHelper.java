package com.gionee.eighteenmonth.progress;


import com.gionee.eighteenmonth.util.Constants;
import com.gionee.gnutils.UuidUtil;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FileHelper {

	public FileHelper() {
		File destDir = new File(Constants.RESULT_PATH);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		destDir = null;
	}

	/**
	 * 把字符串写入到文件中
	 * 
	 * @param fileName
	 *            文件名
	 * @param content
	 *            写入的内容
	 * @param is
	 *            每次写入时是否覆盖上次的内容
	 */
	public void write(String fileName, String content, boolean is) {
		File file = new File(fileName);
		BufferedWriter buff = null;
		try {
			buff = new BufferedWriter(new FileWriter(file, is));
			buff.write(content);
			buff.newLine();
			buff.flush();
		} catch (IOException e) {
			e.printStackTrace();
			Log.i("error:"+e.getMessage());
		} finally {
			close(buff);
		}
	}

	/**
	 * 读文件
	 */
	private void close(Closeable close) {
		if (close != null) {
			try {
				close.close();
			} catch (IOException e) {
				close = null;
			}
		}
	}

	/**
	 * size=1 为4k
	 */
	private static String getContent(int size)
	{
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < size * 64; i++) {
			builder.append("0000000000000000000000000000000000000000000000000000000000000000");
		}
		return builder.toString();
	}

	/**
	 * 字节流复制文件
	 * @param path 目标路径
	 * @param size 文件大小 1 size = 4k
	 * @param n 文件个数
	 */
	public static void writeFiles(String path, int size,int n) {
		FileOutputStream outputStream = null;
		try {
			for (int i = 0; i < n; i++) {
				File file2 = new File(path, UuidUtil.getUUID());
				if (!file2.exists()) {
					if (file2.createNewFile()) {Log.i("create succeed");}
				}
				byte[] srtbyte = getContent(size).getBytes();
				outputStream = new FileOutputStream(file2);
				outputStream.write(srtbyte, 0, srtbyte.length);
				outputStream.flush();
				outputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			Log.i(e.getMessage() + "------"+path);
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
