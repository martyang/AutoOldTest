package com.gionee.autoaging18month.slidetest;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

import com.gionee.autoaging18month.Utils.Instrument;
import com.gionee.autoaging18month.Utils.Instrument.Direction;
import com.gionee.autoaging18month.Utils.Log;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import gionee.autotest.AccessUtil;


public class SlideService extends Service {
    public static int currentAppIndex = 0;
    public static String currentPkgName = "";
    private int height, width;
    private long nextTime;
    private AccessUtil accessUtil;
    private ArrayList<String> appPKgs = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        width = dm.widthPixels;
        height = dm.heightPixels;
        accessUtil = new AccessUtil(this);
        accessUtil.setServiceEnable(true);
    }

    @Override
    public void onDestroy() {
        Log.i("onDestroy");
        super.onDestroy();
        Configurator.isTest = false;
        accessUtil.setServiceEnable(false);
        startActivity();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        appPKgs.clear();
        appPKgs.addAll(Configurator.getTestApp(this));
        new Thread() {
            @Override
            public void run() {
                long eachTime = (Configurator.time * 60 * 1000) / appPKgs.size();
                for (int i = 2; i < appPKgs.size(); i++) {
                    currentAppIndex = i;
                    currentPkgName = appPKgs.get(currentAppIndex);
                    launchApp(currentPkgName);
                    try {
                        mSleep(i == 0 ? 5000 : 10000);
                    } catch (IllegalStateException e) {
                        return;
                    }
                    nextTime = eachTime + System.currentTimeMillis();
                    handleSwipe();
                }
                stopSelf();
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startActivity() {
        Intent finish = new Intent(SlideService.this, SlideActivity.class);
        finish.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(finish);
        sendBroadcast(new Intent("testFinish.Slide"));
    }

    private void handleSwipe() {
        if (Configurator.isRandomSlide) {
            randomSlide();
        } else {
            try {
                orderSlide();
            } catch (Exception e) {
                return;
            }
        }
        if (nextTime > System.currentTimeMillis() && Configurator.isTest) {
            handleSwipe();
        }
    }

    private void randomSlide() {
        long a = System.currentTimeMillis();
        int randomInt = new Random().nextInt(appPKgs.size());
        Direction d = Direction.values()[randomInt];
        Instrument.swipe(d, width, height, Configurator.step);
        long b = System.currentTimeMillis();
        long waitTime = Configurator.gapTime - (b - a);
        SystemClock.sleep(waitTime > 0 ? waitTime : 0);
    }

    private void orderSlide() throws TimeoutException {
        Direction[] values = Direction.values();
        for (Direction d : values) {
            long a = System.currentTimeMillis();
            if (nextTime <= System.currentTimeMillis()) {
                throw new TimeoutException();
            }
            if (currentAppIndex == appPKgs.size() - 1) {
                if (d == Direction.FORWARD) {
                    d = Direction.LEFT;
                }
            }
            Instrument.swipe(d, width, height, Configurator.step);
            long b = System.currentTimeMillis();
            long waitTime = Configurator.gapTime - (b - a);
            SystemClock.sleep(waitTime > 0 ? waitTime : 0);
        }
    }


    public void launchApp(String pkg) {
        PackageManager manager = getPackageManager();
        Intent intent = manager.getLaunchIntentForPackage(pkg);
        if (Configurator.isTest) {
            startActivity(intent);
        }
    }

    private void mSleep(int time) {
        time = time < 1000 ? 1000 : time;
        for (int i = 0; i < time / 1000; i++) {
            if (!Configurator.isTest) {
                throw new IllegalStateException();
            }
            SystemClock.sleep(1000);
        }
    }

}
