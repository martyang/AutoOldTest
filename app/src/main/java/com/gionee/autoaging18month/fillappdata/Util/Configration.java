package com.gionee.autoaging18month.fillappdata.Util;


import android.os.Environment;

import java.io.File;

public class Configration {
    public static boolean isTest = false;
    public static boolean isRunVideo = false;
    public static String path = Environment.getExternalStorageDirectory().getPath() + File.separator +"PlayVideo";
}
