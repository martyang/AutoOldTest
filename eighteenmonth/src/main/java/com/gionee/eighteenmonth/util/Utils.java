package com.gionee.eighteenmonth.util;

/*
 *  @项目名：  AutoAging18Month 
 *  @包名：    com.gionee.eighteenmonth.util
 *  @文件名:   Utils
 *  @创建者:   gionee
 *  @创建时间:  2017/7/31 17:10
 *  @描述：
 */


import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.Settings;

import com.gionee.gnutils.ContextUtil;
import com.gionee.gnutils.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utils {

    /**
     *获取应用的icon
     */
    public static Drawable getAppDrawable(String pkg,List<PackageInfo> packgeInfos){
        Drawable drawable = null;
        PackageManager pm = ContextUtil.getInstance().getPackageManager();
        for(PackageInfo packgeInfo : packgeInfos){
                        String   appName     = packgeInfo.applicationInfo.loadLabel(pm).toString();
            String   packageName = packgeInfo.packageName;
            drawable    = packgeInfo.applicationInfo.loadIcon(pm);
            if (pkg.equals(packageName)){
                Bitmap bmp =((BitmapDrawable)drawable).getBitmap();
                //先把Drawable转成Bitmap，如果是Bitmap，就不用这一步了
                FileOutputStream fop;
                try {
                    fop=new FileOutputStream("/sdcard/"+appName+".png");
                    //实例化FileOutputStream，参数是生成路径
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fop);
                    //压缩bitmap写进outputStream 参数：输出格式  输出质量  目标OutputStream
                    //格式可以为jpg,png,jpg不能存储透明
                    fop.close();
                    //关闭流
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return drawable;
            }
        }
        return drawable;
    }

    /**
     * 获取所有应用的icon
     */
    public static void getAppDrawable(){
        Drawable drawable;
        List<PackageInfo> list = new ArrayList<>();
        PackageManager pm = ContextUtil.getInstance().getPackageManager();
        List<PackageInfo>  packgeInfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
            for(PackageInfo packgeInfo : packgeInfos){
                String   appName     = packgeInfo.applicationInfo.loadLabel(pm).toString();
                String   packageName = packgeInfo.packageName;
                drawable    = packgeInfo.applicationInfo.loadIcon(pm);
                Bitmap bmp =((BitmapDrawable)drawable).getBitmap();
                //先把Drawable转成Bitmap，如果是Bitmap，就不用这一步了
                FileOutputStream fop;
                try {
                    fop=new FileOutputStream("/sdcard/"+appName+".png");
                    //实例化FileOutputStream，参数是生成路径
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fop);
                    //压缩bitmap写进outputStream 参数：输出格式  输出质量  目标OutputStream
                    //格式可以为jpg,png,jpg不能存储透明
                    fop.close();
                    //关闭流
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }

    }

    /**
     * 批量插入联系人
     * @param size 个数
     * @param ctx 上下文
     */
    public static void CopyAll2Phone(int size,Context ctx) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int                                 rawContactInsertIndex;
        for (int i = 0; i < size; i++) {
            rawContactInsertIndex = ops.size();// 这句好很重要，有了它才能给真正的实现批量添加。

            ops.add(ContentProviderOperation
                            .newInsert(ContactsContract.RawContacts.CONTENT_URI)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                            .withYieldAllowed(true).build());
            ops.add(ContentProviderOperation
                            .newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(
                                    ContactsContract.Data.RAW_CONTACT_ID,
                                    rawContactInsertIndex)
                            .withValue(
                                    ContactsContract.Data.MIMETYPE,
                                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                            .withValue(
                                    ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                    getTime()+i).withYieldAllowed(true).build());
            ops.add(ContentProviderOperation
                            .newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(
                                    ContactsContract.Data.RAW_CONTACT_ID,
                                    rawContactInsertIndex)
                            .withValue(
                                    ContactsContract.Data.MIMETYPE,
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                                       "10086"+i)
                            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                       ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).withYieldAllowed(true).build());

        }
        try {
            // 这里才调用的批量添加
            ctx.getContentResolver()
               .applyBatch(ContactsContract.AUTHORITY, ops);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }

    }
    /**
     * 获得日期时间
     */
    public static String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

    public static  void deleteFile(File file) {
        if(file.exists()) {
            if(file.isFile()) {
                file.delete();
            } else if(file.isDirectory()) {
                File[] files = file.listFiles();

                for(int i = 0; i < files.length; ++i) {
                    deleteFile(files[i]);
                }
            }

            file.delete();
        } else {
            Log.i("删除完成");
        }

    }

    /**
     * apk是否安装成功
     */
    public static boolean isApkInstalled(String packagename,Context context)
    {
        PackageManager localPackageManager = context.getPackageManager();
        try
        {
            PackageInfo localPackageInfo = localPackageManager.getPackageInfo(packagename, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
            return false;
        }

    }
    /**
     * 包名启动apk
     * @param pkgName apk包
     */
    public static void startAppByPkgName(String pkgName,Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(pkgName);

        if (intent != null) {
            context.startActivity(intent);
        }
    }

    /**
     * 设置灭屏时间
     *
     * @Author SONGYC
     * create at 2016-07-18 16:37
     */
    public static void setScreenTime(Context context, int time) {
        ContentResolver cr = context.getContentResolver();
        Settings.System.putInt(cr, Settings.System.SCREEN_OFF_TIMEOUT, time);
    }

    public static int getContactsCount(Context context) {
        Cursor cur = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        //        String uri = "content://icc/adn";
        //content://icc/pbr
        cur.moveToPosition(0);
        int count = cur.getCount();
        cur.close();
        Log.i( "联系人个数: " + count);
        return count;
    }

    public static int getAllSMS(Context context) {
        List<String> listSms = new ArrayList<String>();
        try {
            // Uri uriSMS = Uri.parse("content://sms/sent");
            Uri    uriSMS = Uri.parse("content://sms/inbox");
            Cursor cursor = context.getContentResolver().query(uriSMS, new String[] { "_id", "thread_id", "address" }, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    //					long threadId = cursor.getLong(1);
                    String address = cursor.getString(cursor.getColumnIndex("address"));
                    listSms.add(address);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        Log.i( "短信个数: " + listSms.size());
        return listSms.size();
    }
}
