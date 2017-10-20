package com.gionee.eighteenmonth.progress;


/**
 * Created by Administrator on 2016/5/10 0010.
 */
public class Log {

//    private static final String TAG = "PermCheck" ;
//    private static final String TAG = "com.gionee.appprogresstest" ;
    private static final String TAG = "gionee.os.autotest" ;

    public static void i(String msg){
        android.util.Log.i(TAG,msg);
    }

    public static void i(String tag , String msg){
        android.util.Log.i(tag, msg);
    }
}
