package com.lihao.news.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lihao.news.page.BasePage;

/**
 * Created by hbm on 2017/4/13.
 */

public class MyFragment extends Fragment {
    private BasePage page;
    public MyFragment(){
    }
    public MyFragment(BasePage page){
        this.page = page;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return page.viewRoot;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
