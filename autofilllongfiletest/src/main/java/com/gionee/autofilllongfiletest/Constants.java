package com.gionee.autofilllongfiletest;

import android.os.Environment;

import java.io.File;

/**
 * 常量
 */
interface Constants {

    String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator;

    String IMG_PATH = SDCARD_PATH+"AutoFillImgTest";

    String FILE_PATH = SDCARD_PATH+"AutoFillFileTest";

    String ASSSETS = "test.png";

}
