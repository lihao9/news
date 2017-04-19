package com.lihao.news.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.lihao.news.page.BasePage;

import java.util.List;

/**
 * Created by hbm on 2017/4/14.
 */

public class MyPagerAdapter extends PagerAdapter {

    private List<BasePage> pages;
    public MyPagerAdapter(List<BasePage> pages){
        this.pages = pages;
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = pages.get(position).viewRoot;
//        pages.get(position).initData();
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
