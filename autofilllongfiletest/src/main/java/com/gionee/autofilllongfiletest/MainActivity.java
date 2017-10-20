package com.gionee.autofilllongfiletest;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gionee.gnutils.ShellUtil;
import com.gionee.gnutils.base.HomeBaseActivity;

public class MainActivity
        extends HomeBaseActivity
{

    @Override
    protected int bindLayout() {
        verifyStoragePermissions(this);
        return R.layout.activity_main;
    }

    private EditText mEtFileSize,mEtImgSize;
    private Button mBtnStart,mBtnStop;
    private TextView mTvShow,mTvTestState;
    private int      mFileSize,mImgSize;

    @Override
    protected void initViews() {
        mEtFileSize = (EditText) findViewById(R.id.main_et_filesize);
        mEtImgSize = (EditText) findViewById(R.id.main_et_imgsize);
        mBtnStart = (Button) findViewById(R.id.main_btn_start);
        mBtnStop = (Button) findViewById(R.id.main_btn_stop);
        mBtnStart.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);
        mBtnStop.setClickable(false);
        mTvShow = (TextView) findViewById(R.id.main_tv_describe);
        mTvTestState = (TextView) findViewById(R.id.main_tv_teststate);
    }
    public static Handler       mHandler;
    private FileAsyncTask mAsyncTask;
    private boolean mIsTest;
    @Override
    protected void initDatas() {
        mIsTest = true;
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        mTvShow.setText((String) msg.obj);
                        break;
                }
            }
        };
        FileUtils.copyFileToSdcard(MainActivity.this, Constants.ASSSETS, Constants.SDCARD_PATH+Constants.ASSSETS);
    }

    private Thread mThread =  new Thread() {
        @Override
        public void run() {
            try {
                for (int i = 0; i < 1000000; i++) {
                    String fileSize = "长名文件个数:" + FileUtils.getPathFileSize(Constants.FILE_PATH)
                            +"\n图片文件个数:" + FileUtils.getPathFileSize(Constants.IMG_PATH);
                    Thread.sleep(5000);
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj =fileSize;
                    mHandler.sendMessage(message);
//                    if (mIsTest){
//                        break;
//                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    protected void viewClick(View v) {
        if (v == mBtnStart){
            startTest();
        }else if (v == mBtnStop){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle(R.string.action_explain);
            builder.setMessage("确定要强制终止测试？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    stopTest();
                    ShellUtil.execCommand(" pm clear "+getPackageName(),false);
                }
            });
            builder.setNegativeButton("取消",null);
            builder.show();

        }
    }

    private  void startTest(){
        if (!checkConnt()){
            return;
        }
        mAsyncTask = new FileAsyncTask(){
            @Override
            protected void onPreExecute() {
                mTvTestState.setText("正在填充...");
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                stopTest();
            }

            @Override
            protected void onProgressUpdate(Integer... i) {
                switch (i[0]){
                    case -1:
                        mTvTestState.setText("填充过程出现错误...");
                        break;
                    case 1:
                        mTvTestState.setText("填充完成...");;
                        break;
                }
            }
        };
        mAsyncTask.execute(mFileSize,mImgSize);
        mBtnStart.setClickable(false);
        mBtnStop.setClickable(true);
        mEtFileSize.setEnabled(false);
        mEtImgSize.setEnabled(false);
        mIsTest = true;
        if(mThread.isAlive()){
            mThread.interrupt();
        }
        mThread.start();
    }

    private  void stopTest(){
        mAsyncTask.cancel(true);
        mBtnStop.setClickable(false);
        mBtnStart.setClickable(true);
        mEtFileSize.setEnabled(true);
        mEtImgSize.setEnabled(true);
        mIsTest = false;
    }

    /**
     * 检查
     */
    private boolean checkConnt() {
        if (TextUtils.isEmpty(mEtFileSize.getText())||TextUtils.isEmpty(mEtImgSize.getText())) {
            Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT)
                 .show();
            return false;
        }
        int fileCount = Integer.parseInt(mEtFileSize.getText()
                                             .toString());
        int imgCount = Integer.parseInt(mEtImgSize.getText()
                                             .toString());
        if (fileCount <= 0||imgCount <= 0) {
            Toast.makeText(this, "输入一个大于0的数字", Toast.LENGTH_SHORT)
                 .show();
            return false;
        }
        mFileSize = fileCount;
        mImgSize = imgCount;
        return true;
    }
    private static final int      REQUEST_EXTERNAL_STORAGE = 1;
    private static       String[] PERMISSIONS_STORAGE      = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    /**
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to
     * grant permissions
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater()
            .inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_clear:
                clear();
                return true;
            case R.id.action_explain:
                explain();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clear() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("提示");
        builder.setMessage("请自行到文件管理中删除AutoFillImgTest和AutoFillFileTest文件夹即可(如果文件较多,可能会需要一段时间,请耐心等待)...");
        builder.setPositiveButton("跳转文件管理", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                ComponentName cn = new ComponentName("com.gionee.filemanager",
                                                     "com.gionee.filemanager.FileExplorerTabActivity");
                intent.setComponent(cn);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }

    private void explain(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.action_explain);
        builder.setMessage(R.string.action_explain_content);
        builder.setPositiveButton("确定", null);
        builder.show();
    }
    @Override
    protected String setAuther() {
        return getResources().getString(R.string.auther);
    }

    @Override
    protected void onEnd() {
        ShellUtil.execCommand(" pm clear "+getPackageName(),false);
    }
}
