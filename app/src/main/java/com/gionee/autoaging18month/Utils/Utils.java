package com.gionee.autoaging18month.Utils;

/*
 *  @项目名：  AutoMonitorWifi 
 *  @包名：    com.gionee.automonitorwifi.util
 *  @文件名:   Utils
 *  @创建者:   gionee
 *  @创建时间:  2017/1/3 11:36
 *  @描述：    工具
 */


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Utils {

    public static boolean isLoginEmail(Context context) {
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(Uri.parse("content://com.android.email.provider/account"), null, null, null, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            Log.i("null");
            return false;
        }
        ArrayList<String> strings = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int account_type = cursor.getColumnIndex("senderName");
            String string = cursor.getString(account_type);
            strings.add(string);
            Log.i("senderName:" + string);
        }
        return !strings.isEmpty();
    }

    public static void toastMakeText(String msg) {
        Toast.makeText(ContextUtil.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获得日期时间
     */
    public static String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

    /**
     * 设置一个闹钟
     *
     * @param context 上线文
     * @param time    时间s
     * @param action  意图
     */
    public static void startAlarm(Context context, Float time, String action) {
        Log.i("添加了一个闹钟：在" + time + "秒后执行");
        Intent intent = new Intent(action);
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        PendingIntent pending = PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        long triggerTime = (long) (SystemClock.elapsedRealtime() + time * 1000);
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pending);
    }

    /**
     * 取消一个闹钟
     *
     * @param mContext 上下文
     * @param action   意图
     */
    public static void cancelAlarm(Context mContext, String action) {
        Intent intent = new Intent(action);
        AlarmManager mAlarm = (AlarmManager) mContext
                .getSystemService(Context.ALARM_SERVICE);
        PendingIntent mPending = PendingIntent.getBroadcast(mContext, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarm.cancel(mPending);
    }

    /**
     * 设置灭屏时间
     */
    public static void setScreenTime(Context context, int time) {
        ContentResolver cr = context.getContentResolver();
        Settings.System.putInt(cr, Settings.System.SCREEN_OFF_TIMEOUT, time);
    }

    public static TranslateAnimation up() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }

    public static void setUpAnimation(ArrayList<View> views) {
        setUpAnimation(views.toArray(new View[]{}));
    }

    public static void setUpAnimation(View... views) {
        for (View view : views) {
            view.setAnimation(up());
        }
    }

    public static ArrayList<View> getAllChild(View view) {
        ViewGroup vp = (ViewGroup) view;
        int childCount = vp.getChildCount();
        ArrayList<View> views = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            View childAt = vp.getChildAt(i);
            if (childAt instanceof ViewGroup) {
                views.addAll(getAllChild(childAt));
            } else {
                views.add(childAt);
            }
        }
        return views;
    }

}
