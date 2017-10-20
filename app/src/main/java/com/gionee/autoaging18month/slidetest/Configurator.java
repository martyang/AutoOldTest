package com.gionee.autoaging18month.slidetest;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.List;

class Configurator {
    public static long time = 20;
    static int gapTime = 500;//每次滑动间隔
    static int step = 10;
    static boolean isRandomSlide = true;
    static boolean isTest = false;

    public static final String[] appNames = {"今日头条", "腾讯新闻", "掌阅"};
    public static final String[] appPKgs = {"com.ss.android.article.news", "com.tencent.news", "com.chaozh.iReaderGionee"};
    public static final String iReader_pkg = "com.chaozh.iReaderFree";
    private static ArrayList<String> nameList;

    public static ArrayList<String> getTestAppName(Context context) {
        getTestApp(context);
        return nameList;
    }

    public static ArrayList<String> getTestApp(Context context) {
        ArrayList<String> pkgList = new ArrayList<>();
        nameList = new ArrayList<>();
        for (int i = 0; i < appPKgs.length; i++) {
            if (isAppExist(context, appPKgs[i])) {
                pkgList.add(appPKgs[i]);
                nameList.add(appNames[i]);
            } else {
                if (i == appPKgs.length-1) {
                    if (isAppExist(context, iReader_pkg)) {
                        pkgList.add(iReader_pkg);
                        nameList.add(appNames[i]);
                    }
                }
            }
        }
        return pkgList;
    }

    private static boolean isAppExist(Context context, String pkg) {
        PackageManager pm = context.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mainIntent.setPackage(pkg);
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, 0);
        return resolveInfos != null && resolveInfos.size() != 0 && resolveInfos.get(0) != null;
    }
}
