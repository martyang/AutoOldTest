package com.gionee.autofilllongfiletest;

/*
 *  @项目名：  AutoUserStressFileOperate 
 *  @包名：    com.gionee.autouserstressfileoperate.Util
 *  @文件名:   FileUtils
 *  @创建者:   gionee
 *  @创建时间:  2016/12/21 17:33
 *  @描述：    操作文件
 */


import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import com.gionee.gnutils.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
            if (file.mkdirs()) {Log.i("create succeed" + file.getAbsolutePath());}
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
     */
    static void writeFiles(String path, int count)
            throws IOException
    {
        File file2 = new File(path, getUUID());
        if (!file2.exists()) {
            if (!file2.exists()) {
                if (file2.mkdir()) {Log.i("create succeed");}
            }
        }
        String           b_64    = "00000000";
        byte[]           srtbyte = b_64.getBytes();
        FileOutputStream outputStream;
        for (int i = 0; i < count; i++) {
            outputStream = new FileOutputStream(file2 + File.separator + getUUIDs() + ".bin");
            outputStream.write(srtbyte, 0, srtbyte.length);
            outputStream.flush();
            outputStream.close();
            //            Log.i(file2.getName() + "write succeed "+i);
        }
    }

    static void copyImg(String path, int count)
            throws IOException
    {
        File file2 = new File(path, getUUID());
        if (!file2.exists()) {
            if (!file2.exists()) {
                if (file2.mkdir()) {Log.i("create succeed");}
            }
        }
        byte[]           b      = new byte[1024 * 8];
        for (int i = 0; i < count; i++) {
            FileInputStream  input  = new FileInputStream(Constants.SDCARD_PATH + Constants.ASSSETS);
            FileOutputStream output = new FileOutputStream(file2 + File.separator + getUUIDs() + ".png");
            int              len;
            while ((len = input.read(b)) != -1) {
                output.write(b, 0, len);
            }
            output.flush();
            output.close();
//            Log.i(file2.getName() + "write succeed " + i);
            input.close();
        }


    }

    /**
     * 获得路径下文件的个数
     */
    static int getPathFileSize(String path) {
        File file = new File(path);
        int  size = 0;
        if (file.exists()) {
            if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (File file1 : files) {
                    if (file1.isDirectory()) {
                        size += file1.listFiles().length;
                    }
                }
            }
        }
        return size;
    }

    /**
     * 删除文件夹下所有的文件
     * @param file 文件夹
     */
    static void deleteDirFile(File file) {
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

    /**
     * 文件命名 uuid
     */
    public static String getUUID() {

        return UUID.randomUUID()
                   .toString();
    }

    /**
     * 文件命名 uuid
     */
    public static String getUUIDs() {

        return UUID.randomUUID()
                   .toString() + getTime() + "Whatever_happens_tomorrow_we_had_today";
    }

    public static String getTime() {
        // yyyyMMddHHmmSS
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-SS");
        Date             curDate   = new Date(System.currentTimeMillis());
        String           time      = formatter.format(curDate);
        return time;
    }

    public static boolean createFiledir(String filePath) {
        File file = new File(filePath);
        return file.isDirectory()
               ? createFile(filePath)
               : createDir(filePath);
    }

    public static boolean createFile(String destFileName) {
        File file = new File(destFileName);
        if (file.exists()) {
            Log.i("创建单个文件" + destFileName + "失败，目标文件已存在！");
            return false;
        } else {
            if (!file.getParentFile()
                     .exists())
            {
                Log.i("创建文件夹 " + destFileName);
                createDir(file.getParent());
            }

            if (destFileName.endsWith(File.separator)) {
                Log.i("创建单个文件" + destFileName + "失败，目标文件不能为目录！");
                return false;
            } else {
                if (!file.getParentFile()
                         .exists())
                {
                    if (!file.getParentFile()
                             .mkdirs())
                    {
                        Log.i("创建目标文件所在目录失败！");
                        return false;
                    }
                }

                try {
                    if (file.createNewFile()) {
                        Log.i("创建单个文件" + destFileName + "成功！");
                        return true;
                    } else {
                        Log.i("创建单个文件" + destFileName + "失败！");
                        return false;
                    }
                } catch (IOException var3) {
                    var3.printStackTrace();
                    Log.i("创建单个文件" + destFileName + "失败！" + var3.getMessage());
                    return false;
                }
            }
        }
    }

    static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            Log.i("创建目录" + destDirName + "失败，目标目录已经存在");
            return false;
        } else {
            if (!destDirName.endsWith(File.separator)) {
                destDirName = destDirName + File.separator;
            }

            if (dir.mkdirs()) {
                Log.i("创建目录" + destDirName + "成功！");
                return true;
            } else {
                Log.i("创建目录" + destDirName + "失败！");
                return false;
            }
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
        DecimalFormat df = isInteger
                           ? fileIntegerFormat
                           : fileDecimalFormat;
        String fileSizeString = "0M";
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
     * 把资源文件移动到sd卡上
     * @param context 上线文
     * @param assetsFileName 需要移动的资源文件的文件名
     * @param sdDir sd卡路径
     */
    public static void copyFileToSdcard(Context context, String assetsFileName, String sdDir) {
        try {
            File file = new File(sdDir);
            if (file.exists()) {
                file.delete();

            }
            file.createNewFile();
            InputStream is = context.getResources()
                                    .getAssets()
                                    .open(assetsFileName);
            FileOutputStream fos    = new FileOutputStream(file);
            byte[]           buffer = new byte[1024];
            int              count  = 0;
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            fos.flush();
            fos.close();
            is.close();
        } catch (Exception e) {
            Log.i(e.toString());
        }
    }
}
