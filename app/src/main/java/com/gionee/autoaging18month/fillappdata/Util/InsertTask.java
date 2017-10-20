package com.gionee.autoaging18month.fillappdata.Util;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import com.gionee.autoaging18month.Utils.Command;
import com.gionee.autoaging18month.Utils.Log;
import com.gionee.autoaging18month.fillappdata.AddDataActivity;

import static com.gionee.autoaging18month.Utils.Instrument.swipe;
import static com.gionee.autoaging18month.fillappdata.Util.Configration.isRunVideo;


public class InsertTask extends AsyncTask<Void, Integer, Void> {
    private Context mContext;
    private OnDel onDel;
    private CallLogModel mCall;

    public InsertTask(Context context,OnDel onDel) {
        mContext = context;
        this.onDel = onDel;
    }


    @Override
    protected Void doInBackground(Void... params) {
        try {
            CallLogDataSet.installCallLog(mContext, mCall);
            insertContacts();
            insertMms();
            refresh();
            cameraTakePicture();
            FrontCameraTakePicture();
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mCall = getCallLogData();
        Log.i("通话记录准备工作完成");
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.i("onPostExecute");
        Command.execCommand("pm clear com.android.camera", false);
        isRunVideo=true;
        backMainActivity();
        super.onPostExecute(aVoid);
    }

    private void cameraTakePicture() {
        if (Configration.isTest) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            openApp("com.android.camera");
            int cameraNum = Preference.getInt(mContext, "cameraNum", 0);
            takePicture(onDel, cameraNum);
        }
    }

    private void FrontCameraTakePicture() {
        if (Configration.isTest) {
            Log.i("打开前置摄像头");
//            Intent intent = new Intent("gn.intent.action.OpenFrontCamera");
            Intent intent = new Intent("gn.com.android.mmitest.item.FrontCameraTest");
            mContext.startActivity(intent);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int frontCamera = Preference.getInt(mContext, "cameraFront", 0);
            takePicture(onDel, frontCamera);
        }
    }

    private void backMainActivity() {
        Intent mainActivity = new Intent(mContext, AddDataActivity.class);
        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(mainActivity);
    }

    private void insertMms() {
        int mmsIRead = Preference.getInt(mContext, "MMSIRead", 0);
        int mmsType = Preference.getInt(mContext, "MMSType", 0);
        int mmsNum = Preference.getInt(mContext, "MMSNum", 0);
        for (int i = 0; i < mmsNum; i++) {
            if (!Configration.isTest) {
                throw new IllegalStateException();
            }
            SmsUtil.insert(mContext, new ContentValues(), mmsType, ("10086" + i), mmsIRead, ("Text" + i), 1);
        }
    }

    private void takePicture(OnDel onDel,int cameraNum) {

        try {
            Thread.sleep(3000);
            Log.i("输入的循环次数为：" + (cameraNum*2));
            for (int t = 0; t < (cameraNum * 2); t++) {
                if (Configration.isTest) {
                    for (int i = 0; i < 3; i++) {
                        Command.execCommand("input keyevent 27", false);
                        Thread.sleep(2000);
                        Log.i("第" + (i + 1) + "次拍照");
                    }
                    Log.i("删除最新的一张照片");
                    onDel.delPicture();
                    Log.i("第" + (t + 1) + "次循环拍照");
                }
            }
            Log.i("拍照完成");
            Command.execCommand("am force-stop com.android.camera", false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void insertContacts() {
        int contactNum = Preference.getInt(mContext, "contactNum", 0);
        Log.i("界面输入联系人个数为：" + contactNum);
        PhoneContactsDataSet.installPhoneContacts(mContext, contactNum, false, "18124012017");
        Log.i("增加联系人完成");
    }

    private CallLogModel getCallLogData() {
        CallLogModel call_result = new CallLogModel();
        int callLogNum = Preference.getInt(mContext, "callLogNum", 0);
        call_result.setSim_id(-1);
        call_result.setNumber("10086");
        call_result.setAddTime(callLogNum);
        call_result.setType(Preference.getInt(mContext, "callType", 0));
        call_result.setIsClear(1);
        Log.i("call数据分别为：" + Preference.getInt(mContext, "callType", 0) + 1 + callLogNum);
        return call_result;
    }

    private void openApp(String PKG) {
        Log.i("开启应用");
        PackageManager pm = mContext.getPackageManager();
        Intent getInt = pm.getLaunchIntentForPackage(PKG);
        if (getInt != null) {
            mContext.startActivity(getInt);
        } else {
            Log.i("开启活动失败");
        }
    }

    private void refresh() {
        openApp("com.android.email");
        Log.i("开始刷新服务");
        int num1 = Preference.getInt(mContext, "emailNum", 0);
        Log.i("刷新次数为：" + num1);
        for (int i = 0; i < num1; i++) {
            if (Configration.isTest) {
                int width = Preference.getInt(mContext, "screenWidth", 0);
                int height = Preference.getInt(mContext, "screenHeigh", 0);
                Log.i("高为：" + width);
                Log.i("宽为：" + height);
                try {
                    Thread.sleep(3 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                swipe(width / 2, height / 6, width / 2, height * 5 / 7, 15);
                Log.i("完成第" + i + "次滑动");
            }
        }
        Log.i("完成滑动");
        Command.execCommand("am force-stop com.android.email", false);
//        Command.execCommand("pm clear com.android.email", false);
        backMainActivity();
    }

    public interface OnDel {
        void delPicture();
    }

}

