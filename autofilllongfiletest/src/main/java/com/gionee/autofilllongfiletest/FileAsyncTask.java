package com.gionee.autofilllongfiletest;

import android.os.AsyncTask;

import com.gionee.gnutils.FileUtil;
import com.gionee.gnutils.Log;

import java.io.IOException;


/*
 *  @文件名:   FileAsyncTask
 *  @创建者:   gionee
 *  @创建时间:  2016/12/24 17:22
 *  @描述：    异步任务
 */


class FileAsyncTask
        extends AsyncTask<Integer, Integer, Void>
{
    @Override
    protected Void doInBackground(Integer... count) {
        FileUtil.createFiledir(Constants.FILE_PATH);
        FileUtil.createFiledir(Constants.IMG_PATH);
        int fileSize = count[0];
        int imgSize  = count[1];
        Log.i("需要写文件个数:"+fileSize);
        Log.i("需要写图片个数:"+imgSize);

        try {
            //文件
            int fileCyc = fileSize / 10000;
            int fileRemainder= fileSize % 10000;
            for (int i = 0; i < fileCyc; i++) {
                FileUtils.writeFiles(Constants.FILE_PATH, 10000);
            }
            FileUtils.writeFiles(Constants.FILE_PATH, fileRemainder);
            //图片
            int imgCyc = imgSize / 10000;
            int imgRemainder= imgSize % 10000;
            for (int i = 0; i < imgCyc; i++) {
                FileUtils.copyImg(Constants.IMG_PATH, 10000);
            }
            FileUtils.copyImg(Constants.IMG_PATH, imgRemainder);

        } catch (IOException e) {
            e.printStackTrace();
            Log.i(e.toString());
            publishProgress(-1);
            return null;
        }
        publishProgress(1);
        return null;
    }


}
