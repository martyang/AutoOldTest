package com.gionee.autoaging18month.fillstorage;

/*
 *  @项目名：  AutoUserStressFileOperate 
 *  @包名：    com.gionee.autouserstressfileoperate.Util
 *  @文件名:   FileUtils
 *  @创建者:   gionee
 *  @创建时间:  2016/12/21 17:33
 *  @描述：    操作文件
 */


import android.os.Environment;
import android.os.StatFs;

import com.gionee.autoaging18month.Utils.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.UUID;

class FileUtils {
    /**
     * 写一个txt文件
     * size=63 为4kb
     */
    static void writeFile(String filePath)
    {
        File file = new File(filePath);
        if (!file.exists()) {
                if (file.mkdirs()) {Log.i("create succeed"+file.getAbsolutePath());}
        }
    }

    /**
     * size=1 为4k
     */
    private static String getContent(int size)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size * 64; i++) {
            builder.append("0000000000000000000000000000000000000000000000000000000000000000");
        }
        return builder.toString();
    }

    /**
     * 字节流复制文件
     * @param path 目标路径
     * @param count 文件个数
     * @param size 文件大小 1 size = 4k
     */
    static void writeFiles(String path, int count,int size) {
        FileOutputStream outputStream = null;
        FileHelper fileHelper = new FileHelper();
        try {
            File file2 = new File(path,getUUID());
            if (!file2.exists()) {
                if (!file2.exists()) {
                    if (file2.mkdir()) {Log.i("create succeed");}
                }
            }
            byte[] srtbyte = getContent(size).getBytes();
            for (int i = 0; i < count; i++) {
                outputStream = new FileOutputStream(file2 + File.separator + getUUID() + ".txt");
                outputStream.write(srtbyte, 0, srtbyte.length);
                outputStream.flush();
                outputStream.close();
//                Log.i(file2.getName() + "write succeed "+i);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(e.getMessage() + "------"+path);
            fileHelper.write(FileAsyncTask.fileName,e.getMessage() + "------"+path,true);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    /**
     * 删除文件夹下所有的文件
     * @param file 文件夹
     */
     static void deleteDirFile(File file){
        if(file.exists()){ //判断文件是否存在
            if(file.isFile()){ //判断是否是文件
                file.delete(); //delete()方法 你应该知道 是删除的意思;
            }else if(file.isDirectory()){ //否则如果它是一个目录
                File files[] = file.listFiles(); //声明目录下所有的文件 files[];
                for(int i=0;i<files.length;i++){ //遍历目录下所有的文件
                    deleteDirFile(files[i]); //把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        }else{
            Log.i("删除完成");
        }
    }

    /**
     * 获取SDCARD剩余存储空间
     */
    static String getAvailableExternalMemorySize() {
        File   path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        return formatFileSize(stat.getAvailableBlocksLong() * stat.getBlockSizeLong());
    }

    /**
     * 获取SDCARD剩余存储空间(大小为K)
     */
    public static long getAvailableExternalMemorySizeK() {
        File   path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        return stat.getAvailableBlocksLong() * stat.getBlockSizeLong() / 1024;
    }

    /**
     * 获取SDCARD总的存储空间
     */
    static String getTotalExternalMemorySize() {
        File   path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        return formatFileSize(stat.getBlockSizeLong() * stat.getBlockCountLong());
    }


   private static DecimalFormat fileIntegerFormat = new DecimalFormat("#0");
    private static DecimalFormat fileDecimalFormat = new DecimalFormat("#0.00#");
    /**
     * 单位换算
     *
     * @param size 单位为B
     * @param isInteger 是否返回取整的单位
     * @return 转换后的单位
     */
    private static String formatFileSize(long size, boolean isInteger) {
        DecimalFormat df             = isInteger
                                       ? fileIntegerFormat
                                       : fileDecimalFormat;
        String        fileSizeString = "0M";
        if (size < 1024 && size > 0) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1024 * 1024) {
            fileSizeString = df.format((double) size / 1024) + "K";
        } else if (size < 1024 * 1024 * 1024) {
            fileSizeString = df.format((double) size / (1024 * 1024)) + "M";
        } else {
            fileSizeString = df.format((double) size / (1024 * 1024 * 1024)) + "G";
        }
        return fileSizeString;
    }

    /**
     * 单位换算
     *
     * @param size 单位为B
     * @return 转换后的单位
     */
    private static String formatFileSize(long size) {

        String fileSizeString = "";
        if (size >= 1024 * 1024 * 1024) {
            fileSizeString += size / (1024 * 1024 * 1024) + "G_";
            size = size % (1024 * 1024 * 1024);
        }
        if (size >= 1024 * 1024) {
            fileSizeString += size / (1024 * 1024) + "M_";
            size = size % (1024 * 1024);
        }
        if (size >= 1024) {
            fileSizeString += size / (1024) + "K";
        }
        return fileSizeString;
    }

    /**
     * 文件命名 uuid
     */
    public static String getUUID() {
        return UUID.randomUUID()
                   .toString();
    }
}
