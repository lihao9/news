package com.lihao.news.page;

import android.content.Context;
import android.view.View;

/**
 * Created by hbm on 2017/4/20.
 */

public abstract class MenuDetailBasePage {
    public View viewRoot;
    public Context context;
    public MenuDetailBasePage(Context context) {
        viewRoot = initView();
        this.context = context;
    }
    public abstract View initView();

    public void initData(){

    }
}
