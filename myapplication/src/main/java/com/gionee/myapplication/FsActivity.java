package com.gionee.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/*
 *  @项目名：  AutoAging18Month 
 *  @包名：    com.gionee.autoaging18month.fillstorage
 *  @文件名:   FsActivity
 *  @创建者:   gionee
 *  @创建时间:  2017/2/24 14:09
 *  @描述：    填充内部存储空间
 */


public class FsActivity
        extends BaseActivity
        implements View.OnClickListener
{

    private       TextView      mTvAvailable;
    private       TextView      mTvResult;
    private       TextView      mTvNowCount;
    private       Button        mBtnStart;
    public static Handler       mHandler;
    private       FileAsyncTask mFileAsyncTask;
    private       EditText      mEtCount;
    private boolean mIsTest = true;
    public  int            mCount;
    private ProgressDialog mProgressDialog;

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_fs);
        mTvAvailable = (TextView) findViewById(R.id.fs_tv_available);
        mTvResult = (TextView) findViewById(R.id.fs_tv_result);
        mTvAvailable.setText("可用:" + FileUtils.getAvailableExternalMemorySize());
        ((TextView) findViewById(R.id.fs_tv_total)).setText("总:" + FileUtils.getTotalExternalMemorySize());
        mBtnStart = (Button) findViewById(R.id.fs_btn_start);
        mBtnStart.setOnClickListener(this);
        mEtCount = (EditText) findViewById(R.id.fs_et_count);
        mTvNowCount = (TextView) findViewById(R.id.fs_tv_nowCount);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("提示");
        mProgressDialog.setMessage("正在终止填充...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        verifyStoragePermissions(this);
    }


    @Override
    protected void initDatas() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        mTvAvailable.setText("可用:" + FileUtils.getAvailableExternalMemorySize());
//                        Log.i("可用:" + FileUtils.getAvailableExternalMemorySize());
                        break;
                }
            }
        };
//        mThread.start();
    }

    private Thread mThread =  new Thread() {
        @Override
        public void run() {
            try {
                for (int i = 0; i < 1000000; i++) {
                    Thread.sleep(30000);
                    Message message = Message.obtain();
                    message.what = 1;
                    mHandler.sendMessage(message);
                    if (mIsTest){
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    public void onClick(View view) {
        if (view == mBtnStart) {
            if (mIsTest) {
                mThread.start();
                FileUtils.createDir(Constants.SDCARD_PATH);
                if (!checkConnt()) {return;}
                mIsTest = false;
                startFiles();
                mBtnStart.setText("停止填充");
            } else {
                if (mFileAsyncTask != null && !mFileAsyncTask.isCancelled()) {
                    mFileAsyncTask.cancel(true);
                    mProgressDialog.show();
                }
                Log.i("点击了取消");
                mIsTest = true;
                mBtnStart.setText("开始填充");
                mTvResult.setText("操作终止");
                mTvNowCount.setVisibility(View.INVISIBLE);
            }

        }
    }

    /**
     * 检查
     */
    private boolean checkConnt() {
        if (TextUtils.isEmpty(mEtCount.getText())) {
            Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT)
                 .show();
            return false;
        }
        int count = Integer.parseInt(mEtCount.getText()
                                             .toString());
        if (count <= 0) {
            Toast.makeText(this, "输入一个大于0的数字", Toast.LENGTH_SHORT)
                 .show();
            return false;
        }
        mCount = count;
        return true;
    }

    @Override
    protected String setAuther() {
        return "彭北林";
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("提示");
        dialog.setMessage("确定要退出当前操作?");
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mFileAsyncTask != null && !mFileAsyncTask.isCancelled()) {
                    mFileAsyncTask.cancel(true);
                }
                finish();
            }
        });
        dialog.setNegativeButton("取消", null);
        dialog.show();

    }

    private void startFiles() {
        mFileAsyncTask = new FileAsyncTask() {
            @Override
            protected void onPreExecute() {
                mTvNowCount.setVisibility(View.VISIBLE);
                mTvResult.setText("正在写入...");
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                if (values[0] == 0) {
                    mTvResult.setText("正在删除...");
                } else if (values[0] == -1) {
                    Toast.makeText(FsActivity.this, "存储空间过小...", Toast.LENGTH_SHORT)
                         .show();
                } else if (values[0] == -2) {
                    mTvResult.setText("正在写入...");
                } else {
                    mTvNowCount.setText("正在进行第" + values[0] + "次测试...");
                }
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mTvResult.setText("操作完成");
                mBtnStart.setText("开始填充");
                mTvNowCount.setVisibility(View.INVISIBLE);
                mIsTest = true;
            }

            @Override
            protected void onCancelled() {
                Log.i("已经取消了操作");
                mProgressDialog.dismiss();
            }
        };
        mFileAsyncTask.execute(mCount);
    }

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
