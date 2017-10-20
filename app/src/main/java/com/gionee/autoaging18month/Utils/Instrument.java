package com.gionee.autoaging18month.Utils;


import android.app.Instrumentation;
import android.os.SystemClock;
import android.view.MotionEvent;


public class Instrument {

    private static Instrumentation mInst = new Instrumentation();

    public static void click(float x, float y) {
        mInst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0));
        mInst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0));
    }

    public static void clickKey(final int KeyCode) {
        new Thread() {
            @Override
            public void run() {
                mInst.sendCharacterSync(KeyCode);
            }
        }.start();
    }

    public static void swipe(final float startX, final float startY, final float endX, final float endY, final int step) {
        new Thread() {
            @Override
            public void run() {
                long dowTime = SystemClock.uptimeMillis();
                mInst.sendPointerSync(MotionEvent.obtain(dowTime, dowTime,
                        MotionEvent.ACTION_DOWN, startX, startY, 0));
                for (int i = 0; i < step; i++) {
                    mInst.sendPointerSync(MotionEvent.obtain(dowTime, dowTime,
                            MotionEvent.ACTION_MOVE, startX + (endX - startX) * i / step, startY + (endY - startY) * i / step, 0));
                }
                mInst.sendPointerSync(MotionEvent.obtain(dowTime, dowTime + 40,
                        MotionEvent.ACTION_UP, endX, endY, 0));
            }
        }.start();
    }

    public static void swipe(Direction direction, int width, int height, int step) {
        switch (direction) {
            case LEFT:
                swipe(width * 9 / 10, height / 2, width / 10, height / 2, step);
                break;
            case RIGHT:
                swipe(width / 10, height / 2, width * 9 / 10, height / 2, step);
                break;
            case FORWARD:
                swipe(width / 2, height * 9 / 10, width / 2, height / 10, step);
                break;
            case BACKWARD:
                swipe(width / 2, height / 10, width / 2, height * 9 / 10, step);
                break;
            default:
                break;
        }
    }

    public enum Direction {
        LEFT, RIGHT, FORWARD, BACKWARD
    }

}
