package com.lihao.news.menupage;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.lihao.news.bean.NewsData;
import com.lihao.news.tools.LogUtil;

/**
 * Created by hbm on 2017/4/16.
 * 专题菜单页面
 */

public class MenuSpecialPage extends MenuBasePage {
    private NewsData.DataBean data;
    public MenuSpecialPage(Context context, NewsData.DataBean data) {
        super(context);
        this.data = data;
    }


    @Override
    public View initView() {
        TextView tv = new TextView(context);
        tv.setText("菜单--专题页面");
        tv.setTextColor(Color.RED);
        return tv;
    }

    public void initData(){
        LogUtil.e("专题页面初始化了");
    }

}
