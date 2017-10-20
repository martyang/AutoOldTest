package com.gionee.myapplication.newpkg;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Environment;

import com.gionee.myapplication.Log;

import java.io.File;
import java.util.Collections;
import java.util.List;


/*
 *  @项目名：  AutoAging18Month 
 *  @包名：    com.gionee.myapplication.newpkg
 *  @文件名:   ExeTask
 *  @创建者:   gionee
 *  @创建时间:  2017/7/7 13:54
 *  @描述：    执行任务
 */


public class ExeTask
        extends AsyncTask<Void,Integer,Void>
{
    private Context mContext;
    private String mApkPath;
    private PackageManager mPackageManager;
    FileHelper mFileHelper;
    String mFileName;
    File mExcelFileName;

    public ExeTask(Context context) {
        mContext = context;
        mApkPath = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"applist";
        mPackageManager = mContext.getPackageManager();
        mFileHelper = new FileHelper();
        mFileName = Constants.LOG_PATH+Utils.getTime()+".txt";
        mExcelFileName = new File(Constants.EXCEL_PATH+Utils.getTime()+".xls");
    }

    @Override
    protected Void doInBackground(Void... voids) {

        PackageManager packageManager = mContext.getPackageManager();
        File[] file = new File(mApkPath).listFiles();
        for (File apkPath : file) {
            PackageInfo info = packageManager.getPackageArchiveInfo(apkPath.getAbsolutePath(),
                                                                                  PackageManager.GET_ACTIVITIES);
            if (info!=null){
                ApplicationInfo applicationInfo = info.applicationInfo;
//                String          appName               = applicationInfo.loadLabel(packageManager)
//                                                                 .toString();
                String packageName = applicationInfo.packageName;
                String          appName               = pkgNameGetAppName(packageName);
                String  installSize = "";
                String startSize;
                if (isApkInstalled(packageName)){
                    startSize = startAndCheckDisk(appName,packageName);
                }else{
                    //安装并启动
                    long startmemory = Utils.getAvailableExternalMemorySizeForB();
                    Log.i(appName+" 安装前，内部存储空间大小："+Utils.getAvailableExternalMemorySize());
                    String command = " pm install -r  "+apkPath.getAbsolutePath();
                    ShellUtil.execCommand(command,false);
                    long endmemory = Utils.getAvailableExternalMemorySizeForB();
                    Log.i(appName+" 安装后，内部存储空间大小："+Utils.getAvailableExternalMemorySize());
                    Log.i(appName+" 安装空间大小："+Utils.formatFileSize(startmemory-endmemory));
                    installSize = Utils.formatFileSize(startmemory-endmemory);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startSize = startAndCheckDisk(appName,packageName);
                }
                Utils.keyclear();
                String content = Utils.getTime() + "," + appName + "," + packageName + "," + startSize + "," + installSize;
                mFileHelper.write(mFileName, content, true);
            }

        }

        JxlUtil.writeAppinfoData(mExcelFileName,mFileName);
        return null;
    }

    private boolean isApkInstalled(String packagename)
    {
        PackageManager localPackageManager = mContext.getPackageManager();
        try
        {
            PackageInfo localPackageInfo = localPackageManager.getPackageInfo(packagename, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
            return false;
        }

    }

    /**
     * 包名启动apk
     * @param pkgName apk包名
     */
    private void startAppByPkgName(String pkgName){
        Intent intent;

        intent = mPackageManager.getLaunchIntentForPackage(pkgName);

        if(intent != null){
            mContext.startActivity(intent);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            float x = HomeActivity.width * 0.75f;
            float y = HomeActivity.heigth-50;
            Utils.clickCoordinate(x,y);
        }
    }

    /**
     * 启动记录存储空间
     */
    private String startAndCheckDisk(String appName,String packageName){
        //启动前内存大小
        long startmemory = Utils.getAvailableExternalMemorySizeForB();
        Log.i(appName+" 启动前，内部存储空间大小："+Utils.getAvailableExternalMemorySize());
        //直接启动
        startAppByPkgName(packageName);
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //启动前内存大小
        long endmemory = Utils.getAvailableExternalMemorySizeForB();
        Log.i(appName+" 启动后，内部存储空间大小："+Utils.getAvailableExternalMemorySize());
        long l = startmemory - endmemory;
        if (l<0){
            l=0;
        }
        Log.i("启动空间大小："+Utils.formatFileSize(l));
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Utils.formatFileSize(l);
    }

    /**
     * 启动activity
     */
    private void startActivtiy(String packageName) {
        try{
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            // 通过查询，获得所有ResolveInfo对象.
            List<ResolveInfo> resolveInfos = mPackageManager.queryIntentActivities(mainIntent, 0);

            // 调用系统排序 ， 根据name排序
            // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
            Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(mPackageManager));
            for (ResolveInfo reInfo : resolveInfos) {
                String   name            = reInfo.loadLabel(mPackageManager)
                                              .toString();
                String   activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
                String   pkgName      = reInfo.activityInfo.packageName; // 获得应用程序的包名
                if (pkgName.equals(packageName)){
                    // 为应用程序的启动Activity 准备Intent
                    Log.i("activityName:"+activityName);
                    Log.i("pkgName:"+pkgName);
                    Intent launchIntent = new Intent();
                    launchIntent.setComponent(new ComponentName(pkgName, activityName));
                    launchIntent.setAction("android.intent.action.MAIN");
                    mContext.startActivity(launchIntent);
                    return;
                }
            }
        }catch(Exception e){
            Log.i(e.toString());
            Log.i("包名启动");
            startAppByPkgName(packageName);
        }
    }

    /**
     * pkgName获取APPname
     */
    private String pkgNameGetAppName(String packageName) {
            Log.i("packageName:"+packageName);
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            // 通过查询，获得所有ResolveInfo对象.
            List<ResolveInfo> resolveInfos = mPackageManager.queryIntentActivities(mainIntent, 0);
            // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
            Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(mPackageManager));
            for (ResolveInfo reInfo : resolveInfos) {
                String   name            = reInfo.loadLabel(mPackageManager)
                                                 .toString();
                String   pkgName      = reInfo.activityInfo.packageName; // 获得应用程序的包名
                if (pkgName.equals(packageName)){
                    Log.i("name:"+name);
                    return name;
                }

            }
        Log.i("name:"+packageName);
        return packageName;
    }

}
