package com.gionee.eighteenmonth.adapter;

/*
 *  @项目名：  AutoChargeMap 
 *  @包名：    com.gionee.autochargemap.adapter
 *  @文件名:   ViewPagerAdapter
 *  @创建者:   gionee
 *  @创建时间:  2017/3/29 15:47
 *  @描述：    viewpager adapter
 */


import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ViewPagerAdapter
        extends PagerAdapter
{
    private ArrayList<View> viewList;

    public ViewPagerAdapter(ArrayList<View> viewList) {
        this.viewList = viewList;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }
}
