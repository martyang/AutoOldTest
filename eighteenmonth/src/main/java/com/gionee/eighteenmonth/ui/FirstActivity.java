package com.gionee.eighteenmonth.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.gionee.eighteenmonth.R;
import com.gionee.eighteenmonth.progress.Log;
import com.gionee.eighteenmonth.util.Constants;
import com.gionee.gnutils.Preference;
import com.gionee.gnutils.ResourceUtil;



/*
 *  @项目名：  AutoAging18Month 
 *  @包名：    com.gionee.eighteenmonth.ui
 *  @文件名:   FirstActivity
 *  @创建者:   gionee
 *  @创建时间:  2017/8/21 14:33
 *  @描述：   第一个应用
 */


public class FirstActivity
        extends AppCompatActivity
{
    private static final int ANIMATION_DURATION = 2000;
    private static final float SCALE_END = 1.13F;

    private ImageView mImageView;
    private TextView  mTv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.acitvity_first);
        mImageView = (ImageView) findViewById(R.id.iv_splash);
        mTv = (TextView) findViewById(R.id.splash_version_name);
        try {
            mTv.setText(getPackageManager().getPackageInfo(getPackageName(),0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            Log.i(e.toString());
            mTv.setText(ResourceUtil.getStringResource(R.string.splash_version));
        }
        animateImage();

    }
   private void animateImage() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mImageView, "scaleX", 1f, SCALE_END);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mImageView, "scaleY", 1f, SCALE_END);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIMATION_DURATION).play(animatorX).with(animatorY);
        set.start();

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent;
                if(Preference.getBoolean(FirstActivity.this, Constants.FIRST_TAG_YINDAO)){
                    intent = new Intent(FirstActivity.this,MainActivity.class);
                }else{
                    intent = new Intent(FirstActivity.this,SplashActivity.class);
                }
                FirstActivity.this.startActivity(intent);
                FirstActivity.this.finish();
            }
        });
    }
}
