package com.gionee.autoaging18month.view;

/*
 *  @文件名:   AboutView
 *  @创建者:   gionee
 *  @创建时间:  2017/1/3 11:43
 *  @描述：    关于
 */


import android.content.Context;
import android.content.pm.PackageManager;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class AboutView {
    private  static String mVersion = null;
    private  TextView     mTvVersion;
    private  TextView     mTvDepartment;
    private  TextView     mTvAuther;
    private  LinearLayout mLinearLayout;
    private Context mContext;
    private String mAuther;

    public AboutView(Context context,String auther) {
        mContext = context;
        mAuther = auther;
    }

    private void initViews(Context context) {
        mLinearLayout = new LinearLayout(context);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mTvVersion = new TextView(context);
        mTvVersion.setText(mVersion);
        mTvDepartment = new TextView(context);
        mTvDepartment.setText("金立OS中心 自动化组");
        mTvAuther = new TextView(context);
        mTvVersion.setTextSize(15);
        mTvDepartment.setTextSize(15);
        mTvAuther.setTextSize(15);
    }

    /**
     * 设置Margin值
     * @param view 需要设置的控件
     * @param f Marg值大小
     */
    private static void setWidth(View view, Context context, int f) {
        LinearLayout.LayoutParams labelParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        labelParams.setMargins((int) dp2px(f, context), 10, 0, 0);
        view.setLayoutParams(labelParams);
    }

    /**
     * 返回about布局
     * @return 返回about布局
     */
    public View getAboutView() {
        try {
            mVersion = mContext.getPackageManager()
                              .getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mVersion = "Version error";
        }
        initViews(mContext);
        mTvAuther.setText(mAuther);
        mLinearLayout.addView(mTvVersion);
        mLinearLayout.addView(mTvDepartment);
        mLinearLayout.addView(mTvAuther);
        setWidth(mTvVersion,mContext, 200);
        setWidth(mTvDepartment, mContext,200);
        setWidth(mTvAuther,mContext, 200);
        return mLinearLayout;
    }


    /**
     * dp --> px
     */

    private static float dp2px(float dp,Context context) {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,

                                         dp, context.getResources().getDisplayMetrics());


    }

    /**
     * px --> dp
     */

    private static float px2dp(float px,Context context) {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px,

                                         context.getResources().getDisplayMetrics());

    }
}
