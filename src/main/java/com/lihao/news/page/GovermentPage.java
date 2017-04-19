package com.lihao.news.page;

import android.content.Context;

import com.lihao.news.tools.LogUtil;

/**
 * Created by hbm on 2017/4/13.
 */

public class GovermentPage extends BasePage {

    public GovermentPage(Context context){
        //构造方法调用父类的
        super(context);
    }

    public void initData(){
        LogUtil.e("政要页面初始化了");
        mTvTitle.setText("政要页面");
    }
}
