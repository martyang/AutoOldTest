package com.gionee.autoaging18month.fillstorage;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gionee.autoaging18month.R;
import com.gionee.autoaging18month.Utils.Log;
import com.gionee.autoaging18month.Utils.Utils;
import com.gionee.autoaging18month.base.BaseActivity;


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

    private       TextView mTvAvailable;
    private       TextView mTvResult;
    private       TextView mTvNowCount;
    private       Button   mBtnStart;
    public static Handler  mHandler;
    private FileAsyncTask mFileAsyncTask;
    private EditText mEtCount;
    private boolean mIsTest = true;
    public int mCount;
    private ProgressDialog mProgressDialog;

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_fs);
        Utils.setScreenTime(this, Integer.MAX_VALUE);
        Condition.addToWhileList(this, "com.gionee.autoaging18month");
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

    }


    @Override
    protected void initDatas() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        mTvAvailable.setText("可用:" + FileUtils.getAvailableExternalMemorySize());
                        Log.i("可用:" + FileUtils.getAvailableExternalMemorySize());
                        break;
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 1000000; i++) {
                        Thread.sleep(5000);
                        Message message = Message.obtain();
                        message.what = 1;
                        mHandler.sendMessage(message);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onClick(View view) {
        if (view == mBtnStart) {
            if (mIsTest){
                if(!checkConnt()){return;}
                mIsTest = false;
                startFiles();
                mBtnStart.setText("停止填充");
            }else{
                if (mFileAsyncTask!=null&&!mFileAsyncTask.isCancelled()){
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
        if(TextUtils.isEmpty(mEtCount.getText())){
            Toast.makeText(this,"输入不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        int count = Integer.parseInt(mEtCount.getText()
                           .toString());
        if (count<=0){
            Toast.makeText(this,"输入一个大于0的数字",Toast.LENGTH_SHORT).show();
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
                if (mFileAsyncTask!=null&&!mFileAsyncTask.isCancelled()){
                    mFileAsyncTask.cancel(true);
                }
                Condition.removeFromWhileList(FsActivity.this, "com.gionee.autoaging18month");
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
                }else if (values[0] == -1){
                    Toast.makeText(FsActivity.this,"存储空间过小...",Toast.LENGTH_SHORT).show();
                }else if (values[0] == -2){
                    mTvResult.setText("正在写入...");
                }else{
                    mTvNowCount.setText("正在进行第"+values[0]+"次测试...");
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

   /* private void startFile() {
        FileUtils.writeFile(Constants.SDCARD_PATH + "4k");
        FileUtils.writeFile(Constants.SDCARD_PATH + "8k");
        FileUtils.writeFile(Constants.SDCARD_PATH + "12k");
        FileUtils.writeFile(Constants.SDCARD_PATH + "16k");
        FileUtils.writeFile(Constants.SDCARD_PATH + "20k");
        FileUtils.writeFile(Constants.SDCARD_PATH + "24k");
        FileUtils.writeFile(Constants.SDCARD_PATH + "128k");
        FileUtils.writeFile(Constants.SDCARD_PATH + "512k");
        long sizeK = FileUtils.getAvailableExternalMemorySizeK();
        long v4    = (long) (sizeK * 0.77f) / 8;//4k 77
        long v8    = (long) (sizeK * 0.06f);//8k 6
        long v12   = (long) (sizeK * 0.02f);//12k 2
        long v16   = (long) (sizeK * 0.02f);//16k 2
        long v20   = (long) (sizeK * 0.01f);//20k 1
        long v24   = (long) (sizeK * 0.01f);//24k 1
        long v128  = (long) (sizeK * 0.03f);//128k 3
        long v512  = (long) (sizeK * 0.01f);//512k 1
        ThreadPoolManager.getInstance()
                         .execute(new WriteFileThread(Constants.SDCARD_PATH + "8k", 8, v8));
        ThreadPoolManager.getInstance()
                         .execute(new WriteFileThread(Constants.SDCARD_PATH + "12k", 12, v12));
        ThreadPoolManager.getInstance()
                         .execute(new WriteFileThread(Constants.SDCARD_PATH + "16k", 16, v16));
        ThreadPoolManager.getInstance()
                         .execute(new WriteFileThread(Constants.SDCARD_PATH + "20k", 20, v20));
        ThreadPoolManager.getInstance()
                         .execute(new WriteFileThread(Constants.SDCARD_PATH + "24k", 24, v24));
        ThreadPoolManager.getInstance()
                         .execute(new WriteFileThread(Constants.SDCARD_PATH + "128k", 128, v128));
        ThreadPoolManager.getInstance()
                         .execute(new WriteFileThread(Constants.SDCARD_PATH + "512k", 512, v512));
        for (int i = 0; i < 8; i++) {
            FileUtils.writeFile(Constants.SDCARD_PATH + "4k" + i);
            ThreadPoolManager.getInstance()
                             .execute(new WriteFileThread(Constants.SDCARD_PATH + "4k" + i, 4, v4));
        }
    }*/

}
