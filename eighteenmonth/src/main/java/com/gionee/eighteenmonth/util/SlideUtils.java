package com.gionee.eighteenmonth.util;

/*
 *  @项目名：  AutoAging18Month 
 *  @包名：    com.gionee.eighteenmonth.util
 *  @文件名:   SlideUtils
 *  @创建者:   gionee
 *  @创建时间:  2017/8/1 14:34
 *  @描述：    滑动 点击
 */


import android.app.Instrumentation;
import android.os.SystemClock;
import android.view.MotionEvent;

import com.gionee.eighteenmonth.ui.MainActivity;
import com.gionee.gnutils.ShellUtil;

public class SlideUtils {

    /**
     * 上下左右滑动
     */
    public static void swipe(int i) {
        int    x     = MainActivity.width;
        int    y     = MainActivity.heigth;
        int    y2    = y / 2;
        int    x2    = x / 2;
        String right = " 20 " + y2 + " " + x + " " + y2;
        String left  = x2 + " " + y2 + " 0 " + y2;
        String top   = x2 + " " + y2 + " " + x2 + " 0 ";
        String below = x2 + " " + y2 + " " + x2 + " " + y;
        switch (i) {
            case 0:
                ShellUtil.execCommand(" input swipe " + right, false);
                break;
            case 1:
                ShellUtil.execCommand(" input swipe " + left, false);
                break;
            case 2:
                ShellUtil.execCommand(" input swipe " + top, false);
                break;
            case 3:
                ShellUtil.execCommand(" input swipe " + below, false);
                break;
        }
    }

    /**
     * 坐标滑动
     */
    public void swipe(int startX, int startY, int endX, int endY) {
        String str = String.valueOf(startX) + " " + startY + " " + endX + " " + endY + " ";
        ShellUtil.execCommand(" input swipe " + str, false);
    }

    /**
     * 部分区域随机点击
     */
    public static void randomClickPart(int n) {
        randomClickForXy(0,
                         MainActivity.heigth / 4,
                         MainActivity.width,
                         MainActivity.heigth / 2,
                         n);
    }

    /**
     * 屏幕内随机点击
     */
    public static void randomClick(int n) {
        randomClickForXy(0, 0, MainActivity.width, MainActivity.heigth, n);
    }

    /**
     * 区域内随机点击
     */
    public static void randomClickForXy(int startX, int startY, int endX, int endY, int n) {
        for (int i = 0; i < n; i++) {
            int x = (int) (Math.random() * endX) + startX;
            int y = (int) (Math.random() * endY) + startY;
            clickXy(x, y);
        }
    }

    /**
     * 上下左右 随机滑动
     */
    public static void randomSwipe(int n) {
        for (int i = 0; i < n; i++) {
            int r = (int) (Math.random() * 4);
            swipe(r);
        }
    }

    /**
     * 左右滑动+上下滑动 10秒滑动
     */
    public static void rltbSwipe(int n) {
        for (int i = 0; i < n; i++) {
            swipe(0);
            SystemClock.sleep(500);
            swipe(1);
            SystemClock.sleep(500);
            swipe(2);
            SystemClock.sleep(500);
            swipe(3);
            SystemClock.sleep(500);
            swipe(0);
            SystemClock.sleep(500);
            swipe(1);
            SystemClock.sleep(500);
            swipe(1);
            SystemClock.sleep(500);
        }
    }

    /**
     * 点击坐标
     * @param x X
     * @param y Y
     */
    public static void clickXy(float x, float y) {
        Instrumentation instrumentation = new Instrumentation();
        instrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                                                           SystemClock.uptimeMillis(),
                                                           MotionEvent.ACTION_DOWN,
                                                           x,
                                                           y,
                                                           0));//按下
        instrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                                                           SystemClock.uptimeMillis(),
                                                           MotionEvent.ACTION_UP,
                                                           x,
                                                           y,
                                                           0));//抬起
        SystemClock.sleep(200);
    }

    /**
     * 一键清理
     */
    public static void keyclear() {
        float x = MainActivity.width / 2f;
        float y = MainActivity.heigth * (1660f / 1920f);
        ShellUtil.execCommand(" input keyevent 3 ", false);
        SystemClock.sleep(1000);
        ShellUtil.execCommand(" input keyevent 187 ", false);
        SystemClock.sleep(1000);
        clickXy(x, y);
    }

    /**
     * home键
     */
    public static void keyHome() {
        ShellUtil.execCommand(" input keyevent 3 ", false);
        SystemClock.sleep(1000);
    }

    /**
     * home键
     */
    public static void keyBack(int n) {
        for (int i = 0; i < n; i++) {
            ShellUtil.execCommand(" input keyevent 4 ", false);
            SystemClock.sleep(1000);
        }
    }

    /**
     * 拍照
     * @param n 张数
     */
    public static void takePhoto(int n) {
        for (int i = 0; i < n; i++) {
            ShellUtil.execCommand(" input keyevent 27 ", false);
            SystemClock.sleep(4000);
        }
    }

    /**
     * 切换音乐
     */
    public static void switchMusic(String event) {
        switch (event) {
            case Constants.KEYEVENT_MEDIA_PLAY:
                ShellUtil.execCommand(" input keyevent 85 ", false);
                break;
            case Constants.KEYEVENT_MEDIA_STOP:
                ShellUtil.execCommand(" input keyevent 86 ", false);
                break;
            case Constants.KEYEVENT_MEDIA_NEXT:
                ShellUtil.execCommand(" input keyevent 87 ", false);
                break;
            default:
                break;
        }
    }

    /**
     * 点掉第三方应用的权限弹框
     */
    public static void clickPermissionsBounced(float x, float y) {
        float X = x * 0.75f;
        float Y = y - 50;
        //权限弹框4次
        for (int i = 0; i < 4; i++) {
            clickXy(X, Y);
            SystemClock.sleep(500);
        }
        //进入应用10次
        for (int i = 0; i < 5; i++) {
            clickXy(X, Y - 10 * i);
            SystemClock.sleep(500);
        }
        //权限弹框4次
        for (int i = 0; i < 4; i++) {
            clickXy(X, Y);
            SystemClock.sleep(500);
        }


    }
}
