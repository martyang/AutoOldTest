package com.gionee.eighteenmonth.util;

import android.content.ContentValues;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import static android.provider.Telephony.Sms;


public class SmsUtil {

    public static void insert(Context mContext, ContentValues cv, int msgType, String from, int isRead, String body, int SimId) {
        cv.clear();
        cv.put(Sms.TYPE, msgType);
        cv.put(Sms.READ, isRead);
        cv.put(Sms.ADDRESS, from);
        cv.put(Sms.BODY, body);
        cv.put(Sms.DATE, System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            cv.put(Sms.SUBSCRIPTION_ID, SimId);
        }
        mContext.getContentResolver().insert(Sms.CONTENT_URI, cv);
    }

    public static void insert(Context mContext, int count) {
        ContentValues cv = new ContentValues();
        long number = (long) (Math.random() * 10000)+18600000000L;
        for (int i = 0; i < count; i++) {
            insert(mContext, cv, Sms.MESSAGE_TYPE_INBOX, String.valueOf(number), 1, "测试"+(i+1), 1);
        }
    }

    public static void setDefaultSms(Context mContext) {
        String packageName       = mContext.getPackageName();
        String defaultSmsPackage = Sms.getDefaultSmsPackage(mContext);

        if (!defaultSmsPackage.equals(packageName)) {
//            Intent intent = new Intent(Sms.Intents.ACTION_CHANGE_DEFAULT);
//            intent.putExtra(Sms.Intents.EXTRA_PACKAGE_NAME, packageName);
//            mContext.startActivity(intent);
            Settings.Secure.putString(mContext.getContentResolver(),"sms_default_application",packageName);
        }
    }
}
