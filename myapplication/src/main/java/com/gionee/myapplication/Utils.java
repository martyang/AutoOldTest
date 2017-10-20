package com.gionee.myapplication;

/*
 *  @项目名：  AutoMonitorWifi 
 *  @包名：    com.gionee.automonitorwifi.util
 *  @文件名:   Utils
 *  @创建者:   gionee
 *  @创建时间:  2017/1/3 11:36
 *  @描述：    工具
 */


import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    /**
     * 获得日期时间
     */
    public static String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

}
