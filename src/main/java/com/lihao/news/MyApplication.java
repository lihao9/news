package com.lihao.news;

import android.app.Application;


import org.xutils.x;

/**
 * Created by hbm on 2017/4/14.
 */

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }

}
