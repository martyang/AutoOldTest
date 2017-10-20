package com.gionee.autoaging18month.fillappdata;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gionee.autoaging18month.R;
import com.gionee.autoaging18month.Utils.Log;
import com.gionee.autoaging18month.Utils.Utils;
import com.gionee.autoaging18month.base.BaseActivity;
import com.gionee.autoaging18month.fillappdata.Util.Configration;
import com.gionee.autoaging18month.fillappdata.Util.InsertTask;
import com.gionee.autoaging18month.fillappdata.Util.Preference;
import com.gionee.autoaging18month.fillappdata.Util.SmsUtil;

import java.io.File;

import static android.provider.MediaStore.Images.ImageColumns.DATE_TAKEN;
import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
import static android.provider.Telephony.TextBasedSmsColumns.MESSAGE_TYPE_INBOX;
import static android.provider.Telephony.TextBasedSmsColumns.MESSAGE_TYPE_SENT;
import static com.gionee.autoaging18month.fillappdata.Util.Configration.isRunVideo;
import static com.gionee.autoaging18month.fillappdata.Util.Configration.isTest;


public class AddDataActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private EditText mContactsNum, mMMsNum, mCallLogNum, mEmailNum, mCameraNum, mCameraFrontNum, mVideoNum;
    private RadioGroup mGroup1, mGroup2;
    private Button mStart;
    private int type;
    private int iRead;//是否已读0未读，1已读
    private int sTypeChoice = 0;
    private AsyncTask<Void, Integer, Void> mTask;
    private String contacts, mms, callLog, email, camera, cameraFront, videoNum;
    private int intContacts, intMMS, intCallLog, intEmail, intCamera, intCameraFront, intVideoNum;
    File mfile;
    @Override
    public void onClick(View v) {
        Log.i("----start------");
        if (v == mStart) {
                if (!Configration.isTest) {
                    if (checkInputView()) {
                    Configration.isTest = true;
                    mTask = new InsertTask(this, new InsertTask.OnDel() {
                        @Override
                        public void delPicture() {
                            deleteLatestPhoto();
                        }
                    }).execute();
                }} else {
                    stopTest();
                }
                updateView();

        }


    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_adddata);
        SmsUtil.setDefaultSms(this);//设置应用为默认的短信应用
        updateView();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Preference.putInt(this, "screenWidth", dm.widthPixels);
        Preference.putInt(this, "screenHeigh", dm.heightPixels);
        mMMsNum = (EditText) findViewById(R.id.MMsNum);
        mContactsNum = (EditText) findViewById(R.id.contactsNum);
        mCallLogNum = (EditText) findViewById(R.id.callLogNum);
        mCameraNum = (EditText) findViewById(R.id.cameraNum);
        mCameraFrontNum = (EditText) findViewById(R.id.cameraFrontNum);
        mEmailNum = (EditText) findViewById(R.id.eMailNum);
        mVideoNum = (EditText) findViewById(R.id.videoNum);
        mGroup1 = (RadioGroup) findViewById(R.id.group1);
        mGroup2 = (RadioGroup) findViewById(R.id.group2);
        mGroup1.setOnCheckedChangeListener(this);
        mGroup2.setOnCheckedChangeListener(this);
        mStart = (Button) findViewById(R.id.start);
        mStart.setOnClickListener(this);
        mMMsNum.setText(Preference.getInt(this, "MMSNum", 30) + "");
        mContactsNum.setText(Preference.getInt(this, "contactNum", 30) + "");
        mCallLogNum.setText(Preference.getInt(this, "callLogNum", 30) + "");
        mEmailNum.setText(Preference.getInt(this, "emailNum", 50) + "");
        mCameraNum.setText(Preference.getInt(this, "cameraNum", 30) + "");
        mCameraFrontNum.setText(Preference.getInt(this, "cameraFront", 30) + "");
        mVideoNum.setText(Preference.getInt(this, "video", 3) + "");
        RelativeLayout addDataRoot = (RelativeLayout) findViewById(R.id.addDataRoot);
        Utils.setUpAnimation(Utils.getAllChild(addDataRoot));
        onHelp();
        mfile = new File(Configration.path+File.separator);
        Log.i("------------new file------------");
        if (!mfile.exists()) {
            Log.i("路径不存在进行新建");
            mfile.mkdir();
        }
    }

    public boolean checkInputView() {
        if (!Utils.isLoginEmail(this)) {
//            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            Snackbar.make(getWindow().getDecorView(),"登录电子邮箱",Snackbar.LENGTH_LONG).show();
            return false;
        }
        if(!isPrepareVideo()){
            Log.i("-------check--------");
            Toast.makeText(this, "~请将视频文件放到指定目录下~", Toast.LENGTH_SHORT).show();
            return false;
        }
        viewToStr();
        if (contacts.equals("") || mms.equals("") || callLog.equals("") || email.equals("") || camera.equals("") || cameraFront.equals("") || videoNum.equals("")) {
            Toast.makeText(this, "请检查参数设置，填写内容是否为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        strToInt();
        if (intContacts < 0 || intMMS < 0 || intCallLog < 0 || intEmail < 0 || intCamera < 0 || intCameraFront < 0 || intVideoNum <= 0) {
            Toast.makeText(this, "请输入正确数值的数据", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }

    private boolean isPrepareVideo() {
        mfile = new File(Configration.path+File.separator);
        Log.i("------------new file------------");
        if (!mfile.exists()) {
            Log.i("路径不存在进行新建");
            mfile.mkdir();
        }
        File[] filelist =mfile.listFiles();
        Log.i(filelist.length+"");
        if(!(filelist.length == 0)){
           return true;
        }
        return  false;
    }

    private void strToInt() {
        intContacts = Integer.parseInt(contacts);
        intMMS = Integer.parseInt(mms);
        intCallLog = Integer.parseInt(callLog);
        intEmail = Integer.parseInt(email);
        intCamera = Integer.parseInt(camera);
        intCameraFront = Integer.parseInt(cameraFront);
        intVideoNum = Integer.parseInt(videoNum);
        Preference.putInt(this, "callLogNum", intCallLog);
        Preference.putInt(this, "contactNum", intContacts);
        Preference.putInt(this, "MMSNum", intMMS);
        Preference.putInt(this, "emailNum", intEmail);
        Preference.putInt(this, "cameraNum", intCamera);
        Preference.putInt(this, "cameraFront", intCameraFront);
        Preference.putInt(this, "video", intVideoNum);
        Log.i("存入的视频循环次数为：" + intVideoNum);
    }

    private void viewToStr() {
        contacts = mContactsNum.getText().toString().trim();
        mms = mMMsNum.getText().toString().trim();
        callLog = mCallLogNum.getText().toString().trim();
        email = mEmailNum.getText().toString().trim();
        camera = mCameraNum.getText().toString().trim();
        cameraFront = mCameraFrontNum.getText().toString().trim();
        videoNum = mVideoNum.getText().toString().trim();
    }

    public void updateView() {
        if (mStart != null) {
            mStart.setText(isTest ? "停止添加" : "开始添加");
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == mGroup1) {
            switch (checkedId) {
                case R.id.hasRead:
                    type = MESSAGE_TYPE_INBOX;
                    iRead = 1;
                    break;
                case R.id.hasSend:
                    type = MESSAGE_TYPE_SENT;
                    iRead = 1;
                    break;
                case R.id.notRead:
                    type = MESSAGE_TYPE_INBOX;
                    iRead = 0;
                    break;
            }
            Preference.putInt(this, "MMSIRead", iRead);
            Preference.putInt(this, "MMSType", type);
        } else if (group == mGroup2) {
            switch (checkedId) {
                case R.id.in:
                    sTypeChoice = 1;
                    break;
                case R.id.to:
                    sTypeChoice = 2;
                    break;
                case R.id.miss:
                    sTypeChoice = 3;
                    break;
            }
            Preference.putInt(this, "callType", sTypeChoice);
        }

    }

    public void stopTest() {
        isTest = false;
        isRunVideo = false;
        mTask.cancel(true);
    }

    public void deleteLatestPhoto() {

        String[] projection = new String[]{MediaStore.Images.ImageColumns._ID, DATE_TAKEN};
//        CursorLoader cursorLoader = new CursorLoader(mContext,EXTERNAL_CONTENT_URI, projection, null, null, DATE_TAKEN + " DESC");
//        Cursor cursor = cursorLoader.loadInBackground();

        Cursor cursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                null, null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        if (cursor != null) {
            cursor.moveToFirst();
            ContentResolver cr = AddDataActivity.this.getContentResolver();
            cr.delete(EXTERNAL_CONTENT_URI,
                    BaseColumns._ID + "=" + cursor.getString(0), null);

        }
    }

    @Override
    protected void onRestart() {
        Log.i("------mainActivity  onRestart-------");
        updateView();

        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.i("-------mainActivity onResume--------");
        updateView();
        if (isRunVideo) {
            startActivity(new Intent(AddDataActivity.this, playVideoActivity.class));
        }
        super.onResume();
    }

    @Override
    protected String setAuther() {
        return "刘思琦";
    }

    @Override
    public void onBackPressed() {
        if(isTest){
            isStopTest();
        }else{
            finish();
        }
    }

    private void isStopTest() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("提示");
        dialog.setMessage("确定要停止添加?");
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                stopTest();
                finish();
            }
        });
        dialog.setNegativeButton("取消", null);
        dialog.show();
    }

    public  void onHelp() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("帮助");
        dialog.setMessage("1.使用前请完成邮箱登录并将相机摄像头调为后置；\n2.在电子邮箱的设置中关闭‘通知栏显示新邮件’这一选项；\n3.使用前进入相机等相关应用去除首次进入的弹框\n4.需要循环的视频要统一放在/sdcard/PlayVideo这一目录下");
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setPositiveButton("了解",null);
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_help:
                menuHelp();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public  void menuHelp() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("帮助");
        dialog.setMessage("使用填充手机资料时：\n1.使用前请完成邮箱登录并将相机摄像头调为后置；\n2.在电子邮箱的设置中关闭‘通知栏显示新邮件’这一选项；\n3.使用前进入相机等相关应用去除首次进入的弹框\n4.需要循环的视频要统一放在/sdcard/PlayVideo这一目录下");
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setPositiveButton("了解",null);
        dialog.show();
    }
}

