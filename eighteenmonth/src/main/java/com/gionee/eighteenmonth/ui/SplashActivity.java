package com.gionee.eighteenmonth.ui;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gionee.eighteenmonth.R;
import com.gionee.eighteenmonth.adapter.ViewPagerAdapter;
import com.gionee.eighteenmonth.util.Constants;
import com.gionee.eighteenmonth.util.ProAsyncTask;
import com.gionee.eighteenmonth.util.SlideUtils;
import com.gionee.eighteenmonth.util.Utils;
import com.gionee.gnutils.Log;
import com.gionee.gnutils.Preference;
import com.gionee.gnutils.ResourceUtil;

import java.io.File;
import java.util.ArrayList;


/*
 *  @项目名：  AutoAging18Month 
 *  @包名：    com.gionee.eighteenmonth.ui
 *  @文件名:   SplashActivity
 *  @创建者:   gionee
 *  @创建时间:  2017/8/15 15:45
 *  @描述：    引导页面
 */


public class SplashActivity
        extends AppCompatActivity
        implements ViewPager.OnPageChangeListener

{
    private ViewPager       mViewPager;
    private int mPagerIndex;
    private PackageManager mPackageManager;

    private String[] mStartApps;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mStartApps = ResourceUtil.getStringArrayResource(R.array.arr_pkg);
        initViews();
        initDatas();
    }

    private void initDatas() {
        mPackageManager = getPackageManager();
    }

    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.splash_viewpager);
        mViewPager.addOnPageChangeListener(this);
        ArrayList<View> views = new ArrayList<>();
        views.add(getPager1());
        views.add(getPager3());
        views.add(getPager4());
        views.add(getPager2());
        views.add(getPager5());
        views.add(getPager6());
        mViewPager.setAdapter(new ViewPagerAdapter(views));
    }
    /**
     * 结束并跳转
     */
    private View.OnClickListener mClickListenerFinish = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
            Preference.putBoolean(SplashActivity.this,Constants.FIRST_TAG_YINDAO,true);
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
        }
    };
    /**
     * 下一个界面
     */
    private View.OnClickListener mClickListenerNext = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mViewPager.setCurrentItem(mPagerIndex+1);
        }
    };
    /**
     * 上一个界面
     */
    private View.OnClickListener mClickListenerLast = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mViewPager.setCurrentItem(mPagerIndex-1);
        }
    };

    private View getPager1() {
        View   view   = LayoutInflater.from(this)
                                      .inflate(R.layout.splash_basepager, null);
        view.findViewById(R.id.pager_last).setVisibility(View.INVISIBLE);
        setViewClickListen(view);
        return view;
    }


    /**
     * 页面跳转
     */
    public View.OnClickListener getGoActivityListener(final String pkg,final String activity){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent();
                    if (activity!=null) {
                        ComponentName cn = new ComponentName(pkg, activity);
                        intent.setComponent(cn);
                        startActivity(intent);
                    }else{
                        Intent intent2 = mPackageManager.getLaunchIntentForPackage(pkg);
                        startActivity(intent2);
                    }

                }catch (Exception e){
                    Toast.makeText(SplashActivity.this,"跳转失败,请先安装应用...",Toast.LENGTH_SHORT).show();
                }

            }
        };
    }

    private View getPager2() {
        View   view   = LayoutInflater.from(this)
                                      .inflate(R.layout.splash_pager2, null);
        setViewClickListen(view);
        Button goSysMsg = view.findViewById(R.id.pager2_btn);
        //跳转系统管家
        goSysMsg.setOnClickListener(getGoActivityListener("com.gionee.softmanager",
                                                          "com.gionee.softmanager.oneclean.WhiteListMrgActivity"));
        TextView tv=  view.findViewById(R.id.tv_shuoming);
        tv.setText(getResources().getText(R.string.condition_2));
        return view;
    }

    private View getPager3() {
        View   view   = LayoutInflater.from(this)
                                      .inflate(R.layout.splash_pager3, null);
        setViewClickListen(view);
        Button goFilemanager =  view.findViewById(R.id.go_filemanager);
        //跳转文件管理
        goFilemanager.setOnClickListener(getGoActivityListener("com.gionee.filemanager",
                                                               "com.gionee.filemanager.FileExplorerTabActivity"));
        TextView tv=  view.findViewById(R.id.tv_shuoming);
        tv.setText(getResources().getText(R.string.condition_3));
        return view;
    }

    /**
     * 安装apk
     */
    private View.OnClickListener mClickListenerInstallApk = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            ProAsyncTask proAsyncTask = new ProAsyncTask(SplashActivity.this, "正在安装", "提示") {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Object[] objects) {
                    try{
                        Intent intent = new Intent();
                        ComponentName componentName =new ComponentName("com.gionee.assistinstall","com.gionee.assistinstall.MainActivity");
                        intent.setComponent(componentName);
                        startActivity(intent);
                        SystemClock.sleep(2000);
                        SlideUtils.keyBack(1);
                    }catch (Exception e){
                        Toast.makeText(SplashActivity.this,"辅助apk没有安装...",Toast.LENGTH_SHORT).show();
                        return null;
                    }

                    File[] file = new File(Constants.YUZHI_INSTALL_APK_PATH).listFiles();
                    for (int j = 0; j < file.length; j++) {
                        String command = "pm install -r  " + file[j].getAbsolutePath();
                        Log.i("pm: " + command);
                        Intent intent = new Intent(Constants.ACTION_INSTALL);
                        intent.putExtra("key", command);
                        sendBroadcast(intent);
                        SystemClock.sleep(10000);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    Toast.makeText(SplashActivity.this,"安装完成...",Toast.LENGTH_SHORT).show();
                }
            };
            proAsyncTask.execute();
        }
    };
    private String[] mPkgs = new String[]{
            "com.duowan.kiwi",
            "cn.kuwo.tingshu",
            "com.qiyi.video",
            "com.netease.cloudmusic",
            "com.example.cta_testload",
            "com.tencent.mobileqq"};
    private String[] mApps = new String[]{
            "虎牙直播",
            "酷我听书",
            "爱奇艺",
            "网易云音乐",
            "RAWapp",
            "QQ"};
    /**
     * 检查安装的apk
     */
    private View.OnClickListener mClickListenerCheckinstall = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < mPkgs.length; i++) {
                if(Utils.isApkInstalled(mPkgs[i],SplashActivity.this)){
                    stringBuilder.append(mApps[i]).append("安装成功\n");
                }else{
                    stringBuilder.append(mApps[i]).append("安装失败\n");
                }
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle("提示");
            builder.setMessage(stringBuilder.toString());
            builder.setPositiveButton("确定", null);
            builder.show();
        }
    };
    private View getPager4() {
        View   view   = LayoutInflater.from(this)
                                      .inflate(R.layout.splash_pager4, null);
        setViewClickListen(view);
        Button goFilemanager =  view.findViewById(R.id.install_apk);
        goFilemanager.setOnClickListener(mClickListenerInstallApk);
        Button checkinstall =  view.findViewById(R.id.checkinstall_apk);
        checkinstall.setOnClickListener(mClickListenerCheckinstall);

        TextView tv=  view.findViewById(R.id.tv_shuoming);
        tv.setText(getResources().getText(R.string.condition_4));
        return view;
    }



    private View getPager5() {
        View   view   = LayoutInflater.from(this)
                                      .inflate(R.layout.splash_pager5, null);
        setViewClickListen(view);
        Button goFilemanager =  view.findViewById(R.id.go_music);
        goFilemanager.setOnClickListener(getGoActivityListener(mStartApps[6],null));
        Button checkinstall =  view.findViewById(R.id.go_kuwo);
        checkinstall.setOnClickListener(getGoActivityListener(mStartApps[9],null));

        TextView tv=  view.findViewById(R.id.tv_shuoming);
        tv.setText(getResources().getText(R.string.condition_5));
        return view;
    }

    private View.OnClickListener mClickListenerGoApp = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           Button go =  view.findViewById(R.id.onebyone_go);
            if (mGoAppIndex==mGoAppList.length){
                go.setText("引导完成...请点击下方的[完成引导]");
            }else if (Utils.isApkInstalled(mGoAppPkgList[mGoAppIndex],SplashActivity.this)){
                Utils.startAppByPkgName(mGoAppPkgList[mGoAppIndex],SplashActivity.this);
                mGoAppIndex++;
                if (mGoAppIndex==mGoAppList.length){
                    go.setText("引导完成...请点击下方的[完成引导]");
                }else {
                    go.setText("跳转到[" + mGoAppList[mGoAppIndex] + "]");
                }
            }else{
                Toast.makeText(SplashActivity.this,mGoAppList[mGoAppIndex]+"安装未...",Toast.LENGTH_SHORT).show();
            }

        }
    };
    private int mGoAppIndex;
    private String[] mGoAppList = ResourceUtil.getStringArrayResource(R.array.arr_goapp);
    private String[] mGoAppPkgList = ResourceUtil.getStringArrayResource(R.array.arr_gopkg);
    private View getPager6() {
        View   view   = LayoutInflater.from(this)
                                      .inflate(R.layout.splash_pager6, null);
        view.findViewById(R.id.pager_next).setVisibility(View.INVISIBLE);
        TextView finishtv = view.findViewById(R.id.pager_finish);
        finishtv.setText("完成引导");
        Button goApp = view.findViewById(R.id.onebyone_go);
        goApp.setOnClickListener(mClickListenerGoApp);
        goApp.setText("跳转到[联系人]");
        setViewClickListen(view);
        TextView tv=  view.findViewById(R.id.tv_shuoming);
        tv.setText(getResources().getText(R.string.condition_6));
        return view;
    }

    private void setViewClickListen(View view) {
        view.findViewById(R.id.pager_finish).setOnClickListener(mClickListenerFinish);
        view.findViewById(R.id.pager_next).setOnClickListener(mClickListenerNext);
        view.findViewById(R.id.pager_last).setOnClickListener(mClickListenerLast);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mPagerIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("提示");
        dialog.setMessage("确定要退出引导界面?");
        dialog.setIcon(com.gionee.gnutils.R.mipmap.ic_launcher);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.setNegativeButton("取消", null);
        dialog.show();
    }
}
