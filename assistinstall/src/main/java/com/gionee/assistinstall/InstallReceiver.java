package com.gionee.assistinstall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gionee.gnutils.Log;
import com.gionee.gnutils.ShellUtil;


/*
 *  @项目名：  AutoAging18Month 
 *  @包名：    com.gionee.assistinstall
 *  @文件名:   InstallReceiver
 *  @创建者:   gionee
 *  @创建时间:  2017/8/8 15:50
 *  @描述：    安装apk
 */


public class InstallReceiver
        extends BroadcastReceiver
{
    private static final String ACTION_INSTALL = "com.gionee.assistinstall_action_install";
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
       final String cmd    = intent.getStringExtra("key");
        new Thread(){
            @Override
            public void run() {
                Log.i("cmd:"+cmd);
                if (action.equals(ACTION_INSTALL)){
                    ShellUtil.execCommand(cmd,false);
                }
            }
        }.start();

    }


}
