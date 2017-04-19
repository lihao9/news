package com.lihao.news.menupage;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.lihao.news.bean.NewsData;
import com.lihao.news.tools.LogUtil;

/**
 * Created by hbm on 2017/4/16.
 * 图片页面
 */

public class MenuImagePage extends MenuBasePage {
    private NewsData.DataBean dataBean;
    public MenuImagePage(Context context, NewsData.DataBean dataBean) {
        super(context);
        this.dataBean = dataBean;
    }


    @Override
    public View initView() {
        TextView tv = new TextView(context);
        tv.setText("菜单--图片页面");
        tv.setTextColor(Color.RED);
        return tv;
    }

    public void initData(){
        LogUtil.e("图片页面初始化了");
    }

}
