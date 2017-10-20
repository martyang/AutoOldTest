package com.gionee.eighteenmonth.progress;


import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


/**
 * @Author Viking Den
 * @Version 1.0
 * @Email dengwj@gionee.com
 * @Time 20:31
 */
public abstract class BaseService extends Service {

    protected static final int NOTIFICATION_FLAG_TRACK          = 907 ;
    protected static final int NOTIFICATION_FLAG_PERMCHECK      = 908 ;
    protected static final int NOTIFICATION_FLAG_PT             = 909 ;

    private NotificationManager mNotificationManager;

    private static boolean sIsRunning = false;

    public static boolean isRunning() {
        return sIsRunning;
    }

    protected static void setRunning(boolean running){
        sIsRunning = running ;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * 显示通知
     */
//    protected void showNotifications(int notfificationId , String nTitle ,String nContent){
//        mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, PermTestActivity.class), 0);
//        Notification notify = new Notification.Builder(this)
//                .setSmallIcon(R.drawable.ic_error_outline)
//                .setContentTitle(nTitle)
//                .setContentText(nContent)
//                .setContentIntent(pendingIntent)
//                .setNumber(1)
//                .build();
//        notify.flags |= Notification.FLAG_NO_CLEAR;
//        notify.flags |= Notification.FLAG_FOREGROUND_SERVICE;
//        mNotificationManager.notify(notfificationId, notify);
//        startForeground(notfificationId, notify);
//    }

    /**
     * 清除通知
     */
    protected void clearNotification(){
        stopForeground(true);
    }

}
