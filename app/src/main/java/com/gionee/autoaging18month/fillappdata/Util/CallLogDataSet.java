package com.gionee.autoaging18month.fillappdata.Util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CallLog.Calls;

import com.gionee.autoaging18month.Utils.Log;

public class CallLogDataSet {
	private static Uri CONTENT_URI = Uri.parse("content://call_log/calls");

	public static synchronized void installCallLog(Context context, CallLogModel call) {
		//get resolver
		ContentResolver resolver = context.getContentResolver();
		System.out.println("方法执行");
		String num = call.getNumber();
		int times = call.getAddTime();
		int type = call.getType();
		int duration = call.getDuration();
		int simId = call.getSim_id();
		Log.i("SimID:" + simId);
		for (int i = 0; i < times; i++) {
			if (!Configration.isTest){
				throw new IllegalStateException();
			}
			//set progress
			long numb = Long.parseLong(num);
			long number = numb + i;
			String number1 = String.valueOf(number);
			long start = System.currentTimeMillis();
			addCall(resolver, number1, type, start, duration, simId);
		}

	}

	public static Uri addCall(ContentResolver resolver, String number, int callType, long start,
							  int duration, int simType) {
		ContentValues values = new ContentValues(5);
		values.put("number", number);
		values.put("type", callType);
		values.put("date", Long.valueOf(start));
		values.put("duration", Long.valueOf(duration));
		values.put("new", Integer.valueOf(1));
		// values.put("sim_id", 1);
//         values.put("subscription", simType); //8600  
		//values.put("simid", simType); //5202
		Uri result = resolver.insert(Calls.CONTENT_URI, values);
		return result;
	}

	/**
	 * Clear call log.
	 */
	public static void clear(ContentResolver resolver) {
		resolver.delete(Calls.CONTENT_URI, null, null);
	}
}
