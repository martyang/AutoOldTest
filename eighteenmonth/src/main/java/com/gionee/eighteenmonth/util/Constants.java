package com.gionee.eighteenmonth.util;

/*
 *  @项目名：  AutoAging18Month 
 *  @包名：    com.gionee.eighteenmonth.util
 *  @文件名:   Constants
 *  @创建者:   gionee
 *  @创建时间:  2017/7/31 17:38
 *  @描述：
 */


import android.os.Environment;

import com.gionee.eighteenmonth.R;

import java.io.File;

public interface Constants {
    //Sd卡路径
    String SDCARD_PATH            = Environment.getExternalStorageDirectory()
                                               .getAbsolutePath() + File.separator;
    //预制apk的路径
    String YUZHI_INSTALL_APK_PATH = SDCARD_PATH + "apkpath" + File.separator + "defaultapk" + File.separator;

    //安装apk的路径
    String INSTALL_APK_PATH = SDCARD_PATH + "apkpath" + File.separator + "installapk" + File.separator;

    //卸载apk的路径
    String UNINSTALL_APK_PATH = SDCARD_PATH + "apkpath" + File.separator + "uninstallapk" + File.separator;

    //虎牙缓存路径
    String HUYA_CACHE_PATH = SDCARD_PATH + "Android" + File.separator + "data" + File.separator + "com.duowan.kiwi" + File.separator + "cache";

    //爱奇艺缓存路径
    String QIYI_CACHE_PATH = SDCARD_PATH + "Android" + File.separator + "data" + File.separator + "com.qiyi.video" + File.separator + "cache";

    //网易云音乐的缓存路径
    String NETEASE_CACHE_PATH = SDCARD_PATH + "netease" + File.separator + "cloudmusic" + File.separator + "Cache";

    //酷我听书
    String KUWO_CACHE_PATH = SDCARD_PATH + "KwTingShu" + File.separator + "playcache";

    //qq
    String QQ_CACHE_PATH = SDCARD_PATH + "Android" + File.separator + "data" + File.separator + "com.tencent.mobileqq" + File.separator + "cache";

    //微信
    String WECHAT_CACHE_PATH = SDCARD_PATH + "Android" + File.separator + "data" + File.separator + "com.tencent.mm" + File.separator + "cache";

    //相机
    String CANERA_CACHE_PATH = SDCARD_PATH + "DCIM" + File.separator + "Camera";


    //总路径
    String RESULT_PATH = SDCARD_PATH + "18month" + File.separator;
    //Log
    String LOG_PATH    = RESULT_PATH + "log" + File.separator;

    //excel
    String EXCEL_PATH = RESULT_PATH + "excel" + File.separator;

    int[] ICON_LIST = {R.mipmap.img01,
                       R.mipmap.img02,
                       R.mipmap.img03,
                       R.mipmap.img04,
                       R.mipmap.img05,
                       R.mipmap.img06,
                       R.mipmap.img07,
                       R.mipmap.img08,
                       R.mipmap.img09,
                       R.mipmap.img10,
                       R.mipmap.img11,
                       R.mipmap.img12,
                       R.mipmap.img13};

    String KEYEVENT_MEDIA_PLAY = "85";
    String KEYEVENT_MEDIA_STOP = "86";
    String KEYEVENT_MEDIA_NEXT = "87";

    String START_DATE = "flag_time";

    String ACTION_INSTALL = "com.gionee.assistinstall_action_install";

    String FIRST_TAG_YINDAO = "first_tag_yindao";
}
