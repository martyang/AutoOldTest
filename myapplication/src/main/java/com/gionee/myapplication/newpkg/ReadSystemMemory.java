package com.gionee.myapplication.newpkg;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Debug;
import android.text.format.Formatter;

import com.jaredrummler.android.processes.ProcessManager;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class ReadSystemMemory {

	/**
	 * 获取android当前可用内存大小
 	 */
	public static String getAvailMemory(Context context) {

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// mi.availMem; 当前系统的可用内存

		return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
	}

	/**
	 * 获取android当前总内存大小
 	 */
	public static  String getTotalMemory(Context context) {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;

		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
//				Log.i("gionee.os.autotest", num + "\t");
			}
//				Log.i("gionee.os.autotest","arrayOfString:"+ arrayOfString[1]);

			initial_memory = Long.valueOf(arrayOfString[1]) * 1024l;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();

		} catch (IOException e) {
//			Log.i("gionee.os.autotest", e.getMessage());
		}
		return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
	}


	public static String getRunningAppProcessInfo(Context context,String pkg) {
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

		//获得系统里正在运行的所有进程
		List<ActivityManager.RunningAppProcessInfo> runningAppProcessesList = mActivityManager.getRunningAppProcesses();

		for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcessesList) {
			// 进程ID号
			int pid = runningAppProcessInfo.pid;
			// 用户ID
			int uid = runningAppProcessInfo.uid;
			// 进程名
			String processName = runningAppProcessInfo.processName;
			int[]              pids       = new int[]{pid};
			Debug.MemoryInfo[] memoryInfo = mActivityManager.getProcessMemoryInfo(pids);
			int                memorySize = memoryInfo[0].dalvikPrivateDirty;
//			Log.i("gionee.os.autotest","processName="+processName+",pid="+pid+",uid="+uid+",memorySize="+memorySize+"kb");

//			if (processName.contains(pkg)) {
//				// 占用的内存
//				int[]              pids       = new int[]{pid};
//				Debug.MemoryInfo[] memoryInfo = mActivityManager.getProcessMemoryInfo(pids);
//				int                memorySize = memoryInfo[0].dalvikPrivateDirty;
//				 Log.i("gionee.os.autotest","processName="+processName+",pid="+pid+",uid="+uid+",memorySize="+memorySize+"kb");
//				return memoryInfo[0].dalvikPrivateDirty + "kb";
//			}
		}
		return "0";
	}

	public static String getRunAppProcessInfo(Context mContext,String pkgName) {
		ActivityManager         am       = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager          pm       = mContext.getPackageManager();
		List<AndroidAppProcess> listInfo = ProcessManager.getRunningAppProcesses();
		if(listInfo.isEmpty() || listInfo.size() == 0){
			return null;
		}
		for (AndroidAppProcess info : listInfo) {
			String processName = info.name;
			try {
				ApplicationInfo applicationInfo = pm.getApplicationInfo(processName, 0);
				//                Log.i(Util.TAG,"processName="+processName+" pkgName="+pkgName+"--"+applicationInfo.processName);
				if (pkgName.contains(processName)) {
					//占用的内存
					int[] myMempid = new int[] { info.pid };
					Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(myMempid);
					//	                int memSize =  memoryInfo[0].dalvikPrivateDirty;
					int memSize =  memoryInfo[0].getTotalPss();

					DecimalFormat df             = new DecimalFormat("#.00");
					String        fileSizeString = "0";
					if (memSize < 1024) {
						fileSizeString = df.format((double) memSize) + " kB";
					} else if (memSize < 1048576) {
						fileSizeString = df.format((double) memSize / 1024) + " MB";
					} else if (memSize < 1073741824) {
						fileSizeString = df.format((double) memSize / 1048576) + " GB";
					}
					return fileSizeString;
				}
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			}
		}
		return "0.00 B";
	}


}
