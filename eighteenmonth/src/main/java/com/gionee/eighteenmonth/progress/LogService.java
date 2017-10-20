package com.gionee.eighteenmonth.progress;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.gionee.eighteenmonth.R;
import com.gionee.eighteenmonth.ui.MainActivity;


public class LogService extends BaseService {

	private LogReaderAsyncTask mLogReaderTask;

	@Override
	public void onCreate() {
		super.onCreate();
		setBeforeServer("正在测试中!");
		}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("logservices_onStartCommand");
		Toast.makeText(this, "开始记录Log", Toast.LENGTH_SHORT).show();
		if (!isRunning()) {
			setRunning(true);
			startLogReader();
		}
		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		setRunning(false);
		// clearNotification();
		stopLogReader();
//		mData.clear();
		Toast.makeText(this, "停止记录Log", Toast.LENGTH_SHORT).show();
		Log.i("logservices_onDestroy");
	}

	/**
	 * 开始记录Log
	 */
	private void startLogReader() {
		mLogReaderTask = new LogReaderAsyncTask(getApplicationContext()) {
			@Override
			protected void onProgressUpdate(String... values) {
				// process the latest logcat lines
				for (String permLine : values) {
                    String[] strs = permLine.split("gionee.os.autotest:");
				}
			}

			@Override
			protected void onPostExecute(Boolean aBoolean) {

			}
		};
		mLogReaderTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		Log.i("Log reader task started");
	}

	/**
	 * 停止记录Log
	 */
	private void stopLogReader() {
		if (mLogReaderTask != null) {
			mLogReaderTask.cancel(true);
		}
		mLogReaderTask = null;
		Log.i("Log reader task stopped");
	}

	/**
	 * 前台服务
	 */
	private void setBeforeServer(String text) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        // 必需的通知内容
        builder.setContentTitle("自动过滤Log并记录")
               .setContentText(text)
               .setSmallIcon(R.mipmap.ic_launcher);
        Intent        notifyIntent        = new Intent(this, MainActivity.class);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, 0);
        builder.setContentIntent(notifyPendingIntent);
        Notification        notification = builder.build();
        startForeground(1, notification);
	}
	


}
