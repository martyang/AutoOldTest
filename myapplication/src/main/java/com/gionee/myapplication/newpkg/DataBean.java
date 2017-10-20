package com.gionee.myapplication.newpkg;

/*
 *  @项目名：  AutoAging18Month 
 *  @包名：    com.gionee.myapplication.newpkg
 *  @文件名:   DataBean
 *  @创建者:   gionee
 *  @创建时间:  2017/7/12 11:07
 *  @描述：    数据
 */


public class DataBean {
    private String pkgName;
    private String appName;
    private String installUseSize;
    private String startUseSize;

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getInstallUseSize() {
        return installUseSize;
    }

    public void setInstallUseSize(String installUseSize) {
        this.installUseSize = installUseSize;
    }

    public String getStartUseSize() {
        return startUseSize;
    }

    public void setStartUseSize(String startUseSize) {
        this.startUseSize = startUseSize;
    }
}
