package com.gionee.eighteenmonth.util;

import android.content.Context;
import android.content.pm.PackageManager;

import com.gionee.eighteenmonth.R;
import com.gionee.gnutils.ResourceUtil;

import java.util.TimerTask;


/*
 *  @项目名：  AutoAging18Month 
 *  @包名：    com.gionee.eighteenmonth.util
 *  @文件名:   TestTask
 *  @创建者:   gionee
 *  @创建时间:  2017/8/3 16:19
 *  @描述：
 */


public class TestTask
        extends TimerTask
{
    private Context        mContext;
    private String[]       mPkgArr;
    private String[]       mDetailsArr;
    private PackageManager mPackageManager;
    public TestTask(Context context) {
        mContext = context;
        mPackageManager = context.getPackageManager();
        mPkgArr = ResourceUtil.getStringArrayResource(R.array.arr_pkg);
        mDetailsArr = ResourceUtil.getStringArrayResource(R.array.arr_details);
    }
    @Override
    public void run() {
        executeTest();
    }
    private void executeTest() {}


}
