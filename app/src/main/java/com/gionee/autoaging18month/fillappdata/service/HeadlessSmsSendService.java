package com.gionee.autoaging18month.fillappdata.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HeadlessSmsSendService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
