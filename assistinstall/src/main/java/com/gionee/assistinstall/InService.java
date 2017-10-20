package com.gionee.assistinstall;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


/*
 *  @项目名：  AutoAging18Month 
 *  @包名：    com.gionee.assistinstall
 *  @文件名:   InService
 *  @创建者:   gionee
 *  @创建时间:  2017/8/22 17:40
 *  @描述：    TODO
 */


public class InService
        extends Service
{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags,
                              int startId)
    {
        return super.onStartCommand(intent, flags, startId);
    }
}
