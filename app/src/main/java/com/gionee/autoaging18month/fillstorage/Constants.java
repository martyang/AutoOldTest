package com.gionee.autoaging18month.fillstorage;

import android.os.Environment;

import java.io.File;

/**
 * 常量
 */
public interface Constants {
    /**
     *压力测试的时间
     */
    float TEST_TIME = 60*60*2f;

    /**
     *停止压力测试的action
     */
    String COM_GIONEE_AUTOTEST_STOPRECEIVER ="com.gionee.autouserstressfileoperate.StopReceiver";

    /**
     * sd卡目录
     */
//    String SDCARD_PATH         = "/data/AutoAging18Month_1G";
    /**
     * 复制文件目录
     */
    String SDCARD_PATH        = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"AutoAging18Month"+File.separator;
    /**
     * 每次操作的间隔时间 ms
     */

    String LOG_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"AutoAging18Month_log";

}
