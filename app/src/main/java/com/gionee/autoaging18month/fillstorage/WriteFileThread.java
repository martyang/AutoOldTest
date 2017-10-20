package com.gionee.autoaging18month.fillstorage;

/*
 *  @项目名：  AutoAging18Month 
 *  @包名：    com.gionee.autoaging18month.fillstorage
 *  @文件名:   WriteFileThread
 *  @创建者:   pbl
 *  @创建时间:  2017/3/6 14:53
 *  @描述：   写文件
 */


public class WriteFileThread
        implements Runnable
{
    private String path;
    private int size;//文件大小
    private long storage;//要写多大内存

    public WriteFileThread(String path, int size, long storage) {
        this.path = path;
        this.size = size/4;
        this.storage = storage;
    }

    @Override
    public void run() {
        int sum = (int) (storage / size);//文件总数
        int count = sum/10000; //一万个一个文件夹
        int remain = sum%10000;
        for (int i = 0; i < count; i++) {
            FileUtils.writeFiles(path, 10000,size);
        }
        FileUtils.writeFiles(path, remain,size);
    }
}
