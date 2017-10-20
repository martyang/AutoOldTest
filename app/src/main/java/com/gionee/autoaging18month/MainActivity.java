package com.gionee.autoaging18month;

import android.content.Intent;
import android.view.View;

import com.gionee.autoaging18month.base.BaseActivity;
import com.gionee.autoaging18month.fillappdata.AddDataActivity;
import com.gionee.autoaging18month.fillstorage.FsActivity;
import com.gionee.autoaging18month.slidetest.SlideActivity;

public class MainActivity
        extends BaseActivity implements View.OnClickListener
{
    @Override
    protected void initViews() {
        setContentView(R.layout.activity_main);
        findViewById(R.id.main_btn_gofad).setOnClickListener(this);
        findViewById(R.id.main_btn_gofs).setOnClickListener(this);
        findViewById(R.id.main_btn_gost).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_btn_gofad:
                goActivity(AddDataActivity.class);
                break;
            case R.id.main_btn_gofs:
                goActivity(FsActivity.class);
                break;
            case R.id.main_btn_gost:
                goActivity(SlideActivity.class);
                break;
        }
    }

    private void goActivity(Class c) {
        startActivity(new Intent(this, c));
    }

    @Override
    protected String setAuther() {
        return "彭北林 宋研聪 刘思琦";
    }

}
