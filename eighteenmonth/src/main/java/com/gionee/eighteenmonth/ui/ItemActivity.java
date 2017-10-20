package com.gionee.eighteenmonth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.gionee.eighteenmonth.R;
import com.gionee.eighteenmonth.bean.ItemBean;
import com.gionee.eighteenmonth.easytransition.EasyTransition;


/*
 *  @项目名：  AutoAging18Month 
 *  @包名：    com.gionee.eighteenmonth.ui
 *  @文件名:   ItemActivity
 *  @创建者:   gionee
 *  @创建时间:  2017/8/1 10:15
 *  @描述：
 */


public class ItemActivity
        extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(mToolbar);
        initView();
        EasyTransition.enter(this);
    }

    private TextView  mTextViewTitle;
    private TextView  mTextViewD;
    private ImageView mImageView;
    private void initView() {
        mImageView = (ImageView) findViewById(R.id.home_item_img);
        mTextViewTitle = (TextView) findViewById(R.id.home_item_title_tv);
        mTextViewD = (TextView) findViewById(R.id.home_item_details_tv);
        Intent   intent = getIntent();
        ItemBean bean   = intent.getParcelableExtra("name");
        mImageView.setImageResource(bean.getIcon());
        mTextViewTitle.setText(bean.getDescribe());
        mTextViewD.setText(bean.getDetails());
    }

    @Override
    public void onBackPressed() {
        EasyTransition.exit(this);
    }


}
