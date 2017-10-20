package com.gionee.eighteenmonth.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;

import com.gionee.eighteenmonth.R;
import com.gionee.eighteenmonth.progress.FileHelper;
import com.gionee.eighteenmonth.ui.MainActivity;
import com.gionee.gnutils.Log;
import com.gionee.gnutils.MemoryUtil;
import com.gionee.gnutils.ResourceUtil;

import java.io.File;



/*
 *  @项目名：  AutoAging18Month 
 *  @包名：    com.gionee.eighteenmonth.util
 *  @文件名:   ExecuteTask
 *  @创建者:   gionee
 *  @创建时间:  2017/8/1 14:05
 *  @描述：    task
 */


public class ExecuteTask
        extends AsyncTask<Void, String, Void>
{
    private Context        mContext;
    private String[]       mPkgArr;
    private String[]       mDetailsArr;
    private String[]       mLinkArr;
    private PackageManager mPackageManager;
    private int            mTestCount;//总次数
    private int mCurrentCount;//当前的次数

    public ExecuteTask(Context context, int testCount) {
        mContext = context;
        mTestCount = testCount;
        mPackageManager = context.getPackageManager();
        mPkgArr = ResourceUtil.getStringArrayResource(R.array.arr_pkg);
        mDetailsArr = ResourceUtil.getStringArrayResource(R.array.arr_details);
        mLinkArr = ResourceUtil.getStringArrayResource(R.array.arr_link);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for (int i = 0; i < mTestCount; i++) {
            mCurrentCount=i;
            float v = MemoryUtil.getAvailableExternalMemorySizeK() / 1024f / 1024f;
            Log.i("剩余内存:" + v + "M");
            if (v < 200) {
                Log.i("剩余内存不足200M...自动清理");
//                publishProgress("剩余内存不足200M...测试停止");
//                backHome();
//                return null;
                clearCache();
                uninstallApps(Constants.INSTALL_APK_PATH,mContext);//一个月卸载一次
            }
            publishProgress("当前测试轮数:"+(i+1));
            executeTest();
            if (i!=0&&(i%29==0)){
                uninstallApps(Constants.INSTALL_APK_PATH,mContext);//一个月卸载一次
                clearCache();
            }
        }
        publishProgress("测试完成");
        backHome();
        return null;
    }

    private void backHome(){
        SlideUtils.keyBack(5);
        SlideUtils.keyclear();
        SystemClock.sleep(2000);
        startAppByPkgName(mContext.getPackageName());
    }

    private void executeTest() {
        //写入联系人5个联系人，对联系人进行操作20s点击+滑动列表
        contactsOperation();

        //写入短信2条，对信息进行操作10s点击+滑动列表
          mmsOperation();

       //写入13条（通过PC工具，周期的发送信息），对微信进行操作30s+滑动列表
        wechatOperation();

        //写入5个（通过PC工具，周期的发送信息），对QQ进行操作30s+滑动列表
        qqOperation();

        //拍照11张照片，点击查看图片，滑动列表共30s
        mapOperation();

        //安装游戏应用2个，并进入等待3分钟退出 /卸载应用1个
        appInstallAndUninStall();

        //访问24个链接,进行滑动
        browserOperation();

        //在线音乐播放13首歌，对应用进行列表滑动+点击播放操作 30s
        musicOperation();

        //在线播放，对应用进行列表滑动+点击播放操作 30s
        liveOperation();

        //载入2集视频，对应用进行列表滑动+点击播放操作 30s
        qiyiOperation();

        //载入一本小说，对应用进行列表滑动+点击播放操作 30s
        bookOperation();

        //记录典型首次应用响应时间:联系人、RAWapp、图库、相机、微信、浏览器+上面执行的所有应用的首次响应时间
        startAppOperation();

        //清理
        SlideUtils.keyclear();
    }

    @Override
    protected void onProgressUpdate(String... values) {

    }

    //联系人
    private void contactsOperation() {
        SlideUtils.keyBack(2);
        int old = Utils.getContactsCount(mContext);
        for (int i = 0; i < 5; i++) {
            SystemClock.sleep(2000);
            Utils.CopyAll2Phone(5, mContext);
            SystemClock.sleep(2000);
            int newCount = Utils.getContactsCount(mContext);
            if ((newCount-old)==5){
                break;
            }
        }
        startAppPrintLog(mDetailsArr[0], mPkgArr[0]);
        SlideUtils.rltbSwipe(2);
    }

    //短信
    private void mmsOperation() {
        SlideUtils.keyBack(2);
        int old = Utils.getAllSMS(mContext);
        for (int i = 0; i < 5; i++) {
            SystemClock.sleep(2000);
            SmsUtil.insert(mContext, 2);
            SystemClock.sleep(2000);
            int newCount = Utils.getAllSMS(mContext);
            if ((newCount-old)==2){
                break;
            }
            SmsUtil.setDefaultSms(mContext);
        }

        startAppPrintLog(mDetailsArr[1], mPkgArr[1]);
        SlideUtils.rltbSwipe(1);
    }

    //微信
    private void wechatOperation() {
        FileHelper.writeFiles(Constants.WECHAT_CACHE_PATH, 256*2, 10);//8*10M的缓存文件
        startAppPrintLog(mDetailsArr[2], "com.tencent.mm");
        SlideUtils.rltbSwipe(3);
    }

    //qq
    private void qqOperation() {
        FileHelper.writeFiles(Constants.QQ_CACHE_PATH, 256, 10);//8*10M的缓存文件
        startAppPrintLog(mDetailsArr[3], "com.tencent.mobileqq");
        SlideUtils.rltbSwipe(3);
    }

    //图库
    private void mapOperation() {
        startAppPrintLog(mDetailsArr[4], "com.android.camera");
        SystemClock.sleep(5000);
        File file = new File(Constants.CANERA_CACHE_PATH);
        int  oldLength = file.listFiles().length;
        SlideUtils.takePhoto(11);
        for (int i = 0; i < 100; i++) {
            if ((file.listFiles().length-oldLength)==11){
                break;
            }
            SlideUtils.takePhoto(1);
        }
        SlideUtils.keyHome();
        startAppByPkgName("com.gionee.gallery");
        SlideUtils.rltbSwipe(1);
        SlideUtils.clickXy(MainActivity.width / 2, MainActivity.heigth / 2);
        SlideUtils.rltbSwipe(2);
    }

    //浏览器 com.android.browser
    private void browserOperation() {
        startAppPrintLog(mDetailsArr[7], "com.android.browser");
        for (int i = 0; i < 24; i++) {
            browserAccess(mLinkArr[i]);
            SlideUtils.randomSwipe(5);
            SlideUtils.keyBack(1);
            SystemClock.sleep(2000);
        }
        SlideUtils.keyclear();
    }

    //音乐
    private void musicOperation() {
        startAppPrintLog(mDetailsArr[8], "com.netease.cloudmusic");
        //切换13首歌
        for (int i = 0; i < 13; i++) {
            SlideUtils.switchMusic(Constants.KEYEVENT_MEDIA_NEXT);
            SlideUtils.randomSwipe(1);//随机滑动一次
            SlideUtils.randomClickPart(1);//随机点击一次
            SystemClock.sleep(2000);
        }
        SlideUtils.switchMusic(Constants.KEYEVENT_MEDIA_STOP);
        SlideUtils.switchMusic(Constants.KEYEVENT_MEDIA_STOP);
        SlideUtils.keyBack(3);
    }

    //直播
    private void liveOperation() {
        startAppPrintLog(mDetailsArr[9], "com.duowan.kiwi");
        SystemClock.sleep(3000);
        FileHelper.writeFiles(Constants.HUYA_CACHE_PATH, 256, 1);//10M的缓存文件
        //        SlideUtils.randomClickPart(1);//随机点击一次
        SlideUtils.clickXy(MainActivity.width / 4, MainActivity.heigth / 2);//点击 播放一个视频
        //滑动
        SlideUtils.rltbSwipe(3);//滑动30秒
        SlideUtils.keyBack(5);
    }

    //爱奇艺
    private void qiyiOperation() {
        startAppPrintLog(mDetailsArr[10], "com.qiyi.video");
        SystemClock.sleep(3000);
        for (int j = 0; j < 2; j++) {//播放两次
            FileHelper.writeFiles(Constants.QIYI_CACHE_PATH, 2560, 8);//8*10M的缓存文件
            for (int i = 0; i < 3; i++) {
                SlideUtils.swipe(2);
                SystemClock.sleep(1000);
            }
            SlideUtils.clickXy(MainActivity.width / 4, MainActivity.heigth / 2);//点击 播放一个视频
            //滑动
            SystemClock.sleep(20000);
            SlideUtils.keyBack(3);
        }
    }

    //酷我听书
    private void bookOperation() {
        startAppPrintLog(mDetailsArr[11], "cn.kuwo.tingshu");
        SystemClock.sleep(5000);
        SlideUtils.switchMusic(Constants.KEYEVENT_MEDIA_PLAY);//播放书
        SlideUtils.switchMusic(Constants.KEYEVENT_MEDIA_NEXT);//下一章
        //滑动
        SlideUtils.rltbSwipe(2);//滑动30秒
        SlideUtils.switchMusic(Constants.KEYEVENT_MEDIA_STOP);
        SlideUtils.switchMusic(Constants.KEYEVENT_MEDIA_STOP);
        SlideUtils.keyBack(3);
    }

    //启动应用
    private void startAppOperation() {
        SlideUtils.keyclear();
        Log.i(mDetailsArr[12]);
//        publishProgress(mDetailsArr[12]);
        SlideUtils.keyHome();
        for (String pkg : mPkgArr) {
            startAppByPkgName(pkg);
            SystemClock.sleep(3000);
            SlideUtils.keyHome();
            SystemClock.sleep(1000);
        }
    }

    /**
     * 打印log启动应用
     */
    private void startAppPrintLog(String details, String pkg) {
        Log.i(details);
//        publishProgress(details);
        SlideUtils.keyHome();
//        clickSure(1);
        SlideUtils.keyclear();
        startAppByPkgName(pkg);
    }

    /**
     * 包名启动apk
     * @param pkgName apk包名
     */
    private void startAppByPkgName(String pkgName) {

        Intent intent = mPackageManager.getLaunchIntentForPackage(pkgName);

        if (intent != null) {
            SystemClock.sleep(2000);
            mContext.startActivity(intent);
            SystemClock.sleep(2000);
        }else{
            Log.i(pkgName+"is Null...");
        }
    }

    /**
     * 访问链接
     */
    private void browserAccess(String url) {
        Uri    uri           = Uri.parse(url);
        Intent browserIntent = new Intent();
        browserIntent.setDataAndType(uri, "text/html");
        browserIntent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
        mContext.startActivity(browserIntent);
    }

    /**
     * 安装和卸载
     */
    private void appInstallAndUninStall(){
        startAppByPkgName("com.gionee.assistinstall");
        SlideUtils.keyHome();
        //安装两个 卸载一个
        installApp(Constants.INSTALL_APK_PATH,mContext,mCurrentCount%30,true);
        installApp(Constants.UNINSTALL_APK_PATH,mContext,0,true);
        installApp(Constants.UNINSTALL_APK_PATH,mContext,0,false);
        SlideUtils.keyHome();
        //有可能出现清理卸载残留的弹框 点掉
//        clickSure(3);
    }

    /**
     * 点击卸载残留/权限弹框
     */
    private void clickSure(int n){
        float X = MainActivity.width * 0.75f;
        float Y = MainActivity.heigth - 50;
        for (int i = 0; i < n; i++) {
            SystemClock.sleep(3000);
            SlideUtils.clickXy(X, Y);
        }
    }

    private static final String ACTION_INSTALL = "com.gionee.assistinstall_action_install";
    /**
     * 安装/卸载 apk 并启动
     * @param path 文件夹路径
     * @param context 上线文
     * @param i 第几个
     */
    private void installApp(String path,Context context,int i,boolean install){
        startAppByPkgName("com.gionee.assistinstall");
        PackageManager packageManager = context.getPackageManager();
        File[]         file           = new File(path).listFiles();
        for (int j = 0; j < file.length; j++) {
            if (j==i){
                PackageInfo info = packageManager.getPackageArchiveInfo(file[i].getAbsolutePath(),
                                                                        PackageManager.GET_ACTIVITIES);
                if (info!=null){
                    ApplicationInfo applicationInfo = info.applicationInfo;
                    String          packageName     = applicationInfo.packageName;
                    if (!isApkInstalled(packageName,context)){
                        Log.i(packageName+" 没有安装");
                    }
                    if (install){
                        //安装并启动
                        Log.i("安装"+packageName);
                        String command = "pm install -r  " + file[i].getAbsolutePath() ;
                        Log.i("pm: "+command);
                        Intent intent = new Intent(ACTION_INSTALL);
                        intent.putExtra("key",command);
                        context.sendBroadcast(intent);
                        for (int k = 0; k < 4; k++) {
                            if (!isApkInstalled(packageName,context)){
                                SystemClock.sleep(5000);
                            }
                        }
                        startAppByPkgName(packageName);
                        SlideUtils.clickPermissionsBounced(MainActivity.width,MainActivity.heigth);
                        SystemClock.sleep(20000);
                    }else{
                        Log.i("卸载:"+packageName);
                        String command = " pm uninstall   "+packageName;
                        Intent intent = new Intent(ACTION_INSTALL);
                        intent.putExtra("key",command);
                        context.sendBroadcast(intent);
                        SystemClock.sleep(3000);
                    }

                }
            }
        }
    }
    /**
     * 批量卸载 apk
     * @param path 文件夹路径
     * @param context 上线文
     */
    private void uninstallApps(String path,Context context){
        PackageManager packageManager = context.getPackageManager();
        File[]         file           = new File(path).listFiles();
        for (int j = 0; j < file.length; j++) {
                PackageInfo info = packageManager.getPackageArchiveInfo(file[j].getAbsolutePath(),
                                                                        PackageManager.GET_ACTIVITIES);
                if (info!=null){
                    ApplicationInfo applicationInfo = info.applicationInfo;
                    String          packageName     = applicationInfo.packageName;
                    if (!isApkInstalled(packageName,context)){
                        Log.i(packageName+" 没有安装");
                    }
                    Log.i("卸载:"+packageName);
                    String command = " pm uninstall   "+packageName;
                    Intent intent = new Intent(ACTION_INSTALL);
                    intent.putExtra("key",command);
                    context.sendBroadcast(intent);
                    SystemClock.sleep(3000);
                }
            }
        }

    private boolean isApkInstalled(String packagename,Context context) {
        PackageManager localPackageManager = context.getPackageManager();
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
    private void clearCache(){
        Utils.deleteFile(new File(Constants.HUYA_CACHE_PATH));
        Utils.deleteFile(new File(Constants.QIYI_CACHE_PATH));
        Utils.deleteFile(new File(Constants.NETEASE_CACHE_PATH));
        Utils.deleteFile(new File(Constants.KUWO_CACHE_PATH));
        Utils.deleteFile(new File(Constants.CANERA_CACHE_PATH));
        Utils.deleteFile(new File(Constants.QQ_CACHE_PATH));
        Utils.deleteFile(new File(Constants.WECHAT_CACHE_PATH));
    }
}
