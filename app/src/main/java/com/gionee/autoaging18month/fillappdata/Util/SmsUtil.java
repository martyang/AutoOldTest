package com.gionee.autoaging18month.fillappdata.Util;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony.Sms;

import com.gionee.autoaging18month.Utils.Log;


public class SmsUtil {

    public static void insert(Context mContext, ContentValues cv, int msgType, String from, int isRead, String body, int SimId) {
        cv.clear();    
        cv.put(Sms.TYPE, msgType);
        cv.put(Sms.READ, isRead);
        cv.put(Sms.ADDRESS, from);
        cv.put(Sms.BODY, body);
        cv.put(Sms.DATE, System.currentTimeMillis());
        cv.put("sub_id", SimId);
        Log.i("添加短信");
        mContext.getContentResolver().insert(Sms.CONTENT_URI, cv);
    }
    

    public static void setDefaultSms(Context mContext) {
        String packageName = mContext.getPackageName();
//        Util.i("当前包名为:" + packageName);
        String defaultSmsPackage = Sms.getDefaultSmsPackage(mContext);
//        Util.i("默认应用包名：" + defaultSmsPackage);
        if (!defaultSmsPackage.equals(packageName)) {
            Intent intent = new Intent(Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Sms.Intents.EXTRA_PACKAGE_NAME, packageName);
            mContext.startActivity(intent);
        }
    }
}
