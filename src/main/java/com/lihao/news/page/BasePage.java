package com.lihao.news.page;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lihao.news.R;

/**
 * Created by hbm on 2017/4/13.
 */

public abstract class BasePage {

    public ImageButton ibBack;
    public ImageButton ibShare;
    public ImageButton ibSwichListGrid;
    public Button btnCart;

    public TextView mTvTitle;
    public ImageButton mIbMune;
    public Context context;
    public View viewRoot;

    /**
     *用于显示数据
     */
    public FrameLayout mFl;

    public BasePage(Context context){
        this.context = context;
        viewRoot = initeView();
    }

    //初始化page页面的大体布局
    public View initeView(){
        viewRoot =  View.inflate(context, R.layout.layout_basepage,null);

        ibBack = (ImageButton)viewRoot.findViewById( R.id.ib_back );
        ibShare = (ImageButton)viewRoot.findViewById( R.id.ib_share );
        ibSwichListGrid = (ImageButton)viewRoot.findViewById( R.id.ib_swich_list_grid );
        btnCart = (Button)viewRoot.findViewById( R.id.btn_cart );

        mTvTitle = (TextView) viewRoot.findViewById(R.id.tv_title);
        mIbMune = (ImageButton) viewRoot.findViewById(R.id.ib_menu);
        mFl = (FrameLayout) viewRoot.findViewById(R.id.fl_content);
        return viewRoot;
    }

    //根据不同的page加载不同的数据，
    // 并且把不同的内容放到公共page中实现多个page的变化。
    public void initData(){

    }
}
