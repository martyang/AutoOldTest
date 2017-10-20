package com.gionee.myapplication.newpkg;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.gionee.myapplication.BaseActivity;
import com.gionee.myapplication.Log;
import com.gionee.myapplication.R;


/*
 *  @项目名：  AutoAging18Month 
 *  @包名：    com.gionee.myapplication.newpkg
 *  @文件名:   HomeActivity
 *  @创建者:   gionee
 *  @创建时间:  17-6-27 下午4:16
 *  @描述：
 */


public class HomeActivity
        extends BaseActivity
{
   private TextView mTextViewSum;
    private TextView mTextViewUser;
    private TextView mTextViewAvailMemory;
    private TextView mTextViewSumMemory;
    private TextView mTextViewMem;
    public static int width;
    public static int heigth;
    @Override
    protected void initViews() {
        setContentView(R.layout.home_activity);
        mTextViewSum = (TextView) findViewById(R.id.home_tv_sum);
        mTextViewUser = (TextView) findViewById(R.id.home_tv_user);
        mTextViewAvailMemory = (TextView) findViewById(R.id.home_tv_cpuAvailMemory);
        mTextViewSumMemory = (TextView) findViewById(R.id.home_tv_cpuSumMemory);
        mTextViewMem = (TextView) findViewById(R.id.home_tv_mem);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width  = dm.widthPixels;
        heigth = dm.heightPixels;

    }

    public static Handler       mHandler;
    @Override
    protected void initDatas() {
        Condition.addToWhileList(this,"com.gionee.myapplication");
        verifyStoragePermissions(this);
//        new ExeTask(this).execute();
        mThread.start();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        mTextViewUser.setText("内部存储器总的空间:"+Utils.getTotalExternalMemorySize());
                        mTextViewSum.setText ("内部存储器剩余空间:"+Utils.getAvailableExternalMemorySize());
                        mTextViewSumMemory.setText ("总内存大小:"+ReadSystemMemory.getTotalMemory(HomeActivity.this));
                        mTextViewAvailMemory.setText ("剩余内存大小:"+ReadSystemMemory.getAvailMemory(HomeActivity.this));
                        mTextViewMem.setText(ReadSystemMemory.getRunningAppProcessInfo(HomeActivity.this,"com.qiyi.video"));
//                        Log.i("内部存储器总的空间:"+Utils.getTotalExternalMemorySize()+"\n"+
                        //                                      "内部存储器剩余空间:"+Utils.getAvailableExternalMemorySize()+"\n"+
                        //                                      "总内存大小:"+ReadSystemMemory.getTotalMemory(HomeActivity.this)+"\n"+
                        //                                      "剩余内存大小:"+ReadSystemMemory.getAvailMemory(HomeActivity.this)+"\n"
                        //                        );
                        Log.i("内部存储器剩余空间:"+Utils.getAvailableExternalMemorySize());
                        break;
                }
            }
        };

    }
    private Thread mThread =  new Thread() {
        @Override
        public void run() {
            try {
                for (int i = 0; i < 1000000; i++) {
                    Thread.sleep(40000);
                    Message message = Message.obtain();
                    message.what = 1;
                    mHandler.sendMessage(message);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    /**
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                                                            Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                                              REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }else{
            finish();
        }
    }
}
