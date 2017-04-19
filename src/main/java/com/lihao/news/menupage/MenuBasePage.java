package com.lihao.news.menupage;

import android.content.Context;
import android.view.View;

import com.lihao.news.tools.LogUtil;

/**
 * Created by hbm on 2017/4/16.
 * 各个菜单页面的父类
 */

public abstract class MenuBasePage {
    public Context context;
    public View rootView;
    public MenuBasePage(Context context){
        this.context = context;
        rootView = initView();
    }
    //子类继承父类分别实现自己的布局
    public abstract View initView();

    //子类重写改方法,实现对自己布局的数据的获取
    //并且将数据设置到自己的布局中显示出来。
    public void initData(){
        LogUtil.e("父类data初始化");

    }
}
