package com.gionee.eighteenmonth.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.gionee.eighteenmonth.R;



/*
 *  @项目名：  AutoGetAppinfo 
 *  @包名：    com.gionee.autogetappinfo.util
 *  @文件名:   ProAsyncTask
 *  @创建者:   gionee
 *  @创建时间:  2017/8/16 10:35
 *  @描述：    ProAsyncTask
 */


public abstract class ProAsyncTask
        extends AsyncTask
{
    private Context        mContext;
    private String         mProgressMsg;
    private String         mProgressTitle;
    private ProgressDialog mProgressDialog;

    public ProAsyncTask(Context context) {
        mContext = context;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIcon(R.mipmap.ic_launcher);
        mProgressDialog.setTitle("提示");
        mProgressDialog.setMessage("请稍后...");
    }

    public ProAsyncTask(Context context, String progressMsg, String progressTitle) {
        this(context);
        mProgressMsg = progressMsg;
        mProgressTitle = progressTitle;
        mProgressDialog.setTitle(progressTitle);
        mProgressDialog.setMessage(progressMsg);
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog.show();
    }

    @Override
    protected void onPostExecute(Object o) {
        mProgressDialog.dismiss();
    }
}
