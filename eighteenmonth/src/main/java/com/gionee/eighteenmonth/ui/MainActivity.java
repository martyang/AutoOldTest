package com.gionee.eighteenmonth.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gionee.eighteenmonth.R;
import com.gionee.eighteenmonth.adapter.ItemAdapter;
import com.gionee.eighteenmonth.bean.ItemBean;
import com.gionee.eighteenmonth.progress.Log;
import com.gionee.eighteenmonth.progress.LogReaderAsyncTask;
import com.gionee.eighteenmonth.progress.LogService;
import com.gionee.eighteenmonth.util.Constants;
import com.gionee.eighteenmonth.util.ExeclUtil;
import com.gionee.eighteenmonth.util.ExecuteTask;
import com.gionee.eighteenmonth.util.SmsUtil;
import com.gionee.eighteenmonth.util.Utils;
import com.gionee.gnutils.Condition;
import com.gionee.gnutils.FileUtil;
import com.gionee.gnutils.Preference;
import com.gionee.gnutils.ResourceUtil;
import com.gionee.gnutils.ui.AboutView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity
        extends AppCompatActivity
        implements View.OnClickListener
{
    private RecyclerView mRecyclerView;
    private Button       mBtnStart, mBtnSetCount, mBtnFixtrue;
    private TextView mTvShowLog;
    private static boolean     sTag;
    private        ExecuteTask mExecuteTask;
    public static  int         width;
    public static  int         heigth;
    private        int         mTestCount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i("onCreate");
        setContentView(R.layout.activity_main);
        initViews();
        initDatas();

    }

    protected void initViews() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                          .getMetrics(dm);
        width = dm.widthPixels;
        heigth = dm.heightPixels;
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(mToolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.home_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,
                                                                         LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        mBtnStart = (Button) findViewById(R.id.main_btn_starttest);
        mBtnStart.setOnClickListener(this);
        mBtnSetCount = (Button) findViewById(R.id.main_btn_setCount);
        mBtnSetCount.setOnClickListener(this);
        mBtnFixtrue = (Button) findViewById(R.id.main_btn_fixture);
        mBtnFixtrue.setOnClickListener(this);
        mTvShowLog = (TextView) findViewById(R.id.main_tv_showlog);
    }

    protected void initDatas() {
        mTestCount = 1;
        Utils.setScreenTime(this, 30 * 60 * 1000);
        FileUtil.createFiledir(Constants.LOG_PATH);
        FileUtil.createFiledir(Constants.EXCEL_PATH);
        Condition.addToWhileList(this, getPackageName());
        Condition.addToWhileList(this, "com.gionee.assistinstall");
        SmsUtil.setDefaultSms(this);
        loadRecycler();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_btn_starttest:
                test();
                break;
            case R.id.main_btn_setCount:
                setTestCount();
                break;
            case R.id.main_btn_fixture:
                setTestfixture();
                break;
            default:
                break;
        }
    }

    private void setTestfixture() {
        AlertDialog.Builder dialog    = new AlertDialog.Builder(this);
        dialog.setTitle("预设条件")
              .setMessage(getResources().getString(R.string.fixture))
              .setIcon(com.gionee.gnutils.R.mipmap.ic_launcher)
              .setPositiveButton("确定", null)
              .show();
    }

    private void setTestCount() {
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        final EditText      editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setTitle("请输入测试次数")
               .setIcon(R.mipmap.ic_launcher)
               .setView(editText)
               .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       String s = editText.getText()
                                          .toString()
                                          .trim();
                       if (!s.isEmpty() && Integer.parseInt(s) > 0 && Integer.parseInt(s) < Integer.MAX_VALUE) {
                           mTestCount = Integer.parseInt(s);
                           mBtnSetCount.setText("设置测试次数:" + mTestCount);
                           Log.i(mTestCount + "");
                       } else {
                           Toast.makeText(MainActivity.this, "输入有误...", Toast.LENGTH_SHORT)
                                .show();
                       }
                   }
               })
               .setNegativeButton("取消", null)
               .show();
    }

    private void test() {
        if (!sTag) {
            if (!Utils.isApkInstalled("com.gionee.assistinstall",this)){
                Toast.makeText(this,"您还没有安装辅助apk...",Toast.LENGTH_SHORT).show();
                return;
            }
            sTag = true;
            mTvShowLog.setVisibility(View.VISIBLE);
            mBtnStart.setText(getResources().getString(R.string.test_stop));
            String time = Utils.getTime();
            Preference.putString(this, Constants.START_DATE, time);
            ExeclUtil.writeCpuloadData(new File(Constants.EXCEL_PATH + time + ".xls"),
                                       ResourceUtil.getStringArrayResource(R.array.arr_app));
            mExecuteTask = new ExecuteTask(this, mTestCount) {
                @Override
                protected void onPreExecute() {
                    FileUtil.createFiledir(Constants.WECHAT_CACHE_PATH);
                    FileUtil.createFiledir(Constants.QQ_CACHE_PATH);
                }

                @Override
                protected void onProgressUpdate(String... values) {
                    for (String value : values) {
                        mTvShowLog.setText(value);
                    }
                }
                @Override
                protected void onPostExecute(Void aVoid) {
                    sTag = false;
                    mBtnStart.setText(getResources().getString(R.string.test_start));
                    stopService(new Intent(MainActivity.this, LogReaderAsyncTask.class));
                }
                @Override
                protected void onCancelled() {
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                }
            };
            mExecuteTask.execute();
            startService(new Intent(this, LogService.class));
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle("提示");
            builder.setMessage("确定要退出测试?");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    sTag = false;
                    mBtnStart.setText(getResources().getString(R.string.test_start));
                    if (mExecuteTask != null && !mExecuteTask.isCancelled()) {
                        mExecuteTask.cancel(true);
                    }
                    stopService(new Intent(MainActivity.this, LogReaderAsyncTask.class));
                    System.exit(0);
                }
            });
            builder.setNegativeButton("取消",null);
            builder.show();

        }
    }


    private void loadRecycler() {
        final ArrayList<ItemBean> itemBeen   = new ArrayList<>();
        String[]                  arrTitle   = ResourceUtil.getStringArrayResource(R.array.arr_title);
        String[]                  arrDetails = ResourceUtil.getStringArrayResource(R.array.arr_details);
        for (int i = 0; i < 13; i++) {
            ItemBean itemBean = new ItemBean();
            itemBean.setDescribe(arrTitle[i]);
            itemBean.setDetails(arrDetails[i]);
            itemBean.setIcon(Constants.ICON_LIST[i]);

            itemBeen.add(itemBean);
        }
        ItemAdapter itemAdapter = new ItemAdapter(MainActivity.this, itemBeen);
        mRecyclerView.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();
                /*itemAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
                        Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                        intent.putExtra("name", itemBeen.get(position));

                        // ready for transition options
                        EasyTransitionOptions options =
                                EasyTransitionOptions.makeTransitionOptions(
                                        MainActivity.this,
                                        view.findViewById(R.id.home_item_img),
                                        view.findViewById(R.id.home_item_title_tv),
                                        view.findViewById(R.id.home_item_details_tv),
                                        findViewById(R.id.v_top_card));

                        // start transition
                        EasyTransition.startActivity(intent, options);
                    }
                });*/
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Condition.removeFromWhileList(this, getPackageName());
        Condition.removeFromWhileList(this, "com.gionee.assistinstall");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater()
            .inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.menu_about) {
            this.about(getResources().getString(R.string.author_name));
        }else if (i == R.id.menu_result) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(com.gionee.gnutils.R.mipmap.ic_launcher);
            builder.setTitle(getResources().getString(R.string.menu_result));
            builder.setMessage(getResources().getString(R.string.result_message));
            builder.setPositiveButton("确定", null);
            builder.show();
        }else if (i == R.id.menu_yindao){
            startActivity(new Intent(this,SplashActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void about(String auther) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(com.gionee.gnutils.R.mipmap.ic_launcher);
        builder.setTitle(com.gionee.gnutils.R.string.app_name);
        builder.setView((new AboutView(this, auther)).getAboutView());
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("提示");
        dialog.setMessage("确定要退出应用?");
        dialog.setIcon(com.gionee.gnutils.R.mipmap.ic_launcher);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                System.exit(0);
            }
        });
        dialog.setNegativeButton("取消", null);
        dialog.show();
    }


}
