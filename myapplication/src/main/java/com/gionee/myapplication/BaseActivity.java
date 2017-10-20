package com.gionee.myapplication;

/*
 *  @文件名:   BaseActivity
 *  @创建者:   gionee
 *  @创建时间:  2017/1/3 16:49
 *  @描述：    base activity
 */


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public abstract class BaseActivity
        extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initDatas();
    }

    /**
     * view init
     */
    protected abstract void initViews();

    /**
     * data init
     */
    protected void initDatas() {}

    /**
     * Auther init
     */
    protected String setAuther(){
        return "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                about(setAuther());
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * menu - about
     */
    private void about(String auther) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.app_name);
        builder.setView(new AboutView(this, auther).getAboutView());
        builder.setPositiveButton("确定", null);
        builder.show();
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("提示");
        dialog.setMessage("确定要退出应用?");
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.setNegativeButton("取消", null);
        dialog.show();
    }

}
