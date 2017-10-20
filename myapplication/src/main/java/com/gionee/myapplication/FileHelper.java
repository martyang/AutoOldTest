package com.gionee.myapplication;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileHelper {
	public FileHelper() {
		File destDir =new File(Constants.LOG_PATH);
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
		File file = new File(Constants.LOG_PATH , fileName);
		BufferedWriter buff = null;
		try {
			buff = new BufferedWriter(new FileWriter(file, is));
			buff.write(content);
			buff.newLine();
			buff.flush();
		} catch (IOException e) {
			e.printStackTrace();
			Log.i("File error : "+e.getMessage());
		} finally {
			close(buff);
		}
	}

	/**
	 * 读文件
	 *            文件名
	 * @return
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

}
