package com.gionee.myapplication.newpkg;

/*
 *  @项目名：  AutoAging18Month 
 *  @包名：    com.gionee.myapplication.newpkg
 *  @文件名:   Utils
 *  @创建者:   gionee
 *  @创建时间:  17-6-27 下午3:55
 *  @描述：
 */


import android.app.Instrumentation;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.view.MotionEvent;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

    /**
     * 获取SDCARD总的存储空间
     */
   public static String getTotalExternalMemorySize() {
        File   path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        return formatFileSize(stat.getBlockSizeLong() * stat.getBlockCountLong());
    }

    /**
     * 获取SDCARD剩余存储空间/xxxxxxxxxxxB
     */
    public static long getAvailableExternalMemorySizeForB() {
        File   path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        return stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
    }

    /**
     * 获取SDCARD剩余存储空间/xxG_xxM_xxK_xxB
     */
    public static String getAvailableExternalMemorySize() {
        File   path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long   l    = stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
        return formatFileSize(stat.getAvailableBlocksLong() * stat.getBlockSizeLong());
    }
    private static DecimalFormat fileIntegerFormat = new DecimalFormat("#0");
    private static DecimalFormat fileDecimalFormat = new DecimalFormat("#0.00#");
    /**
     * 单位换算
     * @param size 单位为B
     * @param isInteger 是否返回取整的单位
     * @return 转换后的单位
     */
    private static String formatFileSize(long size, boolean isInteger) {
        DecimalFormat df             = isInteger
                                       ? fileIntegerFormat
                                       : fileDecimalFormat;
        String        fileSizeString = "0M";
        if (size < 1024 && size > 0) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1024 * 1024) {
            fileSizeString = df.format((double) size / 1024) + "K";
        } else if (size < 1024 * 1024 * 1024) {
            fileSizeString = df.format((double) size / (1024 * 1024)) + "M";
        } else {
            fileSizeString = df.format((double) size / (1024 * 1024 * 1024)) + "G";
        }
        return fileSizeString;
    }
    /**
     * 单位换算
     * @param size 单位为B
     * @return 转换后的单位
     */
    public static String formatFileSize(long size) {

        String fileSizeString = "";
        if (size >= 1024 * 1024 * 1024) {
            fileSizeString += size / (1024 * 1024 * 1024) + "G_";
            size = size % (1024 * 1024 * 1024);
        }
        if (size >= 1024 * 1024) {
            fileSizeString += size / (1024 * 1024) + "M_";
            size = size % (1024 * 1024);
        }
        if (size >= 1024) {
            fileSizeString += size / (1024) + "K_";
            size = size % 1024;
        }
        if (size != 0){
            fileSizeString += size  + "B";
        }else{
            fileSizeString += "0B";
        }
        return fileSizeString;
    }


    /**
     * 点击屏幕坐标
     */
    public static  void clickCoordinate(float x,float y){
        float X = x * 0.75f;
        float Y = y-50;
        //权限弹框4次
        for (int i = 0; i < 4; i++) {
            clickXy(X,Y);
            SystemClock.sleep(500);
        }
        //进入应用10次
//        for (int i = 0; i < 7; i++) {
//            clickXy(X,Y-100*i);
//            SystemClock.sleep(500);
//        }
//        //权限弹框4次
//        for (int i = 0; i < 4; i++) {
//            clickXy(X,Y);
//            SystemClock.sleep(500);
//        }
        //滑动5次
        //开始体验

        }

    private static void clickXy(float x,float y){
        Instrumentation instrumentation = new Instrumentation();
        instrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0));//按下
        instrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0));//抬起

    }
    public static void keyclear(){
        float x = HomeActivity.width / 2f;
        float y = HomeActivity.heigth*(1660f/1920f);
        ShellUtil.execCommand(" input keyevent 3 ",false);
        SystemClock.sleep(1000);
        ShellUtil.execCommand(" input keyevent 187 ",false);
        SystemClock.sleep(1000);
        clickXy(x,y);
    }
}
