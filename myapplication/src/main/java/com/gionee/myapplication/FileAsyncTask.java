package com.gionee.myapplication;

import android.os.AsyncTask;

import java.io.File;


/*
 *  @项目名：  AutoUserStressFileOperate 
 *  @包名：    com.gionee.autouserstressfileoperate.Util
 *  @文件名:   FileAsyncTask
 *  @创建者:   gionee
 *  @创建时间:  2016/12/24 17:22
 *  @描述：    异步任务
 */


class FileAsyncTask
        extends AsyncTask<Integer, Integer, Void>
{
    private FileHelper fileHelper;
    static String fileName = Utils.getTime() + ".txt";
    static int mCount;
    @Override
    protected Void doInBackground(Integer... count) {
        fileHelper = new FileHelper();
        publishProgress(0);
        deletePaths();
        mCount = count[0];
        FileUtils.writeFile(Constants.SDCARD_PATH);

        for (int j = 0; j < mCount; j++) {
            publishProgress(j+1);
            publishProgress(-2);
//            fileHelper.write(fileName, "开始填充", true);
                String path = Constants.SDCARD_PATH + FileUtils.getUUID() + File.separator;
                FileUtils.createFiledir(path);
                FileUtils.writeFile(path);
                startFile(path);
                writeFile(path + "1M", 1024, 7);
                if (isCancelled()) {return null;}
                writeFile(path + "512k", 512, 1);
                if (isCancelled()) {return null;}
                writeFile(path + "128k", 128, 3);
                if (isCancelled()) {return null;}
                writeFile(path + "24k", 24,1);
                if (isCancelled()) {return null;}
                writeFile(path + "20k", 20, 1);
                if (isCancelled()) {return null;}
                writeFile(path + "16k", 16, 2);
                if (isCancelled()) {return null;}
                writeFile(path + "12k", 12, 2);
                if (isCancelled()) {return null;}
                writeFile(path + "8k", 8,6);
                if (isCancelled()) {return null;}
                writeFile(path + "4k", 4,77);
                if (isCancelled()) {return null;}
        }
        publishProgress(0);
//        deletePaths();
        return null;
    }

    private void deletePaths(){
        FileUtils.writeFile(Constants.SDCARD_PATH);
        deleteDirFile(new File(Constants.SDCARD_PATH));
    }
    //写1G文件
    private void startFile(String path) {
        FileUtils.writeFile(path + "4k");
        FileUtils.writeFile(path + "8k");
        FileUtils.writeFile(path + "12k");
        FileUtils.writeFile(path + "16k");
        FileUtils.writeFile(path + "20k");
        FileUtils.writeFile(path + "24k");
        FileUtils.writeFile(path + "128k");
        FileUtils.writeFile(path + "512k");
        FileUtils.writeFile(path + "1M");
    }

    private void writeFile(String path, int size, int count) {
        for (int i = 0; i < count; i++) {
            if (isCancelled()) { return;}
            FileUtils.writeFiles(path, 1, size / 4);
        }
    }

    private void deleteDirFile(File file) {
        if (isCancelled()) { return;}
        if (file.exists()) { //判断文件是否存在
            if (file.isFile()) { //判断是否是文件
                file.delete(); //delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { //否则如果它是一个目录
                File files[] = file.listFiles(); //声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { //遍历目录下所有的文件
                    deleteDirFile(files[i]); //把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        } else {
            Log.i("删除完成");
        }
    }

    public void cancelTest(){

    }
}
