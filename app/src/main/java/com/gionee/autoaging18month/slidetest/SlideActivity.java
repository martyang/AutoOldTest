package com.gionee.autoaging18month.slidetest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gionee.autoaging18month.R;
import com.gionee.autoaging18month.Utils.Preference;
import com.gionee.autoaging18month.base.BaseActivity;

import java.util.ArrayList;

import gionee.autotest.AccessUtil;


/*
 *  @项目名：  AutoAging18Month 
 *  @包名：    com.gionee.autoaging18month.fillstorage
 *  @文件名:   SlideActivity
 *  @创建者:   gionee
 *  @创建时间:  2017/2/24 14:09
 *  @描述：    app滑动
 */


public class SlideActivity extends BaseActivity implements View.OnClickListener {
    private static EditText mTime;
    private static Button mStart;
    private TextView testFinish;
    private SlideReceiver slideReceiver;
    private boolean noApp = false;
    private TextView mTestAppText;

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_slide);
        mTestAppText = (TextView) findViewById(R.id.testAppText);
        new RefreshTask().execute();
        mTime = (EditText) findViewById(R.id.Time);
        mTime.setText(Preference.getLong("isTest", 20) + "");
        mTime.setSelection(mTime.getText().length());
        mStart = (Button) findViewById(R.id.start);
        mStart.setOnClickListener(this);
        testFinish = (TextView) findViewById(R.id.testFinish);
        slideReceiver = new SlideReceiver();
        registerReceiver(slideReceiver, new IntentFilter("testFinish.Slide"));
    }

    @Override
    public void onClick(View view) {
        boolean isTest = Configurator.isTest;
        if (!isTest) {
            if (!checkInputTime()) return;
            Configurator.isTest = true;
            testFinish.setVisibility(View.GONE);
            startService(new Intent(this, SlideService.class));
        } else {
            Configurator.isTest = false;
            stopService(new Intent(this, SlideService.class));
        }
        refreshView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(slideReceiver);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private boolean checkInputTime() {
        String time = mTime.getText().toString();
        if (!"".equals(time) || Integer.parseInt(time) == 0) {
            if (noApp) {
                Toast.makeText(this, "不存在需要测试的应用", Toast.LENGTH_LONG).show();
                return false;
            }
            long t = Long.parseLong(time);
            Preference.putLong("isTest", t);
            Configurator.time = t;
            return true;
        } else {
            Toast.makeText(this, "请输入正确的次数!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @NonNull
    private String getTestAppNames() {
        ArrayList<String> testAppName = Configurator.getTestAppName(this);
        String text = "本次测试应用:\n";
        if (testAppName.size() == 0) {
            text += "无";
            noApp = true;
        } else {
            for (int i = 0; i < testAppName.size(); i++) {
                text += testAppName.get(i) + (i == testAppName.size() - 1 ? "" : "、");
            }
        }
        return text;
    }

    public static void refreshView() {
        boolean isTest = Configurator.isTest;
        if (mTime != null)
            mTime.setEnabled(!isTest);
        if (mStart != null)
            mStart.setText(isTest ? "停止测试" : "开始测试");
    }

    @Override
    protected String setAuther() {
        return "宋研聪";
    }

    @Override
    protected void onStart() {
        super.onStart();
        new AccessUtil(this).setServiceEnable(false);
        refreshView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }

    private class RefreshTask extends AsyncTask<Void, Object, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mTestAppText != null)
                mTestAppText.setText("");
        }

        @Override
        protected String doInBackground(Void... params) {
            return getTestAppNames();
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            if (mTestAppText != null)
                mTestAppText.setText(str);
        }
    }


    class SlideReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "testFinish.Slide":
                    testFinish.setVisibility(View.VISIBLE);
                    break;

                default:
                    break;
            }
        }
    }

}
