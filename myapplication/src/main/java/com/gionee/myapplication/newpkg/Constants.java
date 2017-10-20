package com.gionee.myapplication.newpkg;

import android.os.Environment;

import java.io.File;

/**
 * 常量
 */
interface Constants {

    String AUTOAGING18MONTH_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"AutoAging18Month";

    String LOG_PATH = AUTOAGING18MONTH_PATH+File.separator+"log"+File.separator;

    String EXCEL_PATH = AUTOAGING18MONTH_PATH+File.separator+"excel"+File.separator;

    String[] EXCEL_HEAD = {"时间","应用名","包名","启动消耗内部存储空间大小","安装消耗内部存储空间大小"};
}
