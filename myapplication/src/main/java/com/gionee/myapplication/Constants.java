package com.gionee.myapplication;

import android.os.Environment;

import java.io.File;

/**
 * 常量
 */
interface Constants {
    /**
     * 复制文件目录
     */
    String SDCARD_PATH        = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"AutoAging18Month"+File.separator;

    String LOG_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"AutoAging18Month_log";

}
