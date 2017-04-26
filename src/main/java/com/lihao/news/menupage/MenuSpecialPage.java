package com.lihao.news.menupage;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lihao.news.R;
import com.lihao.news.activity.MainActivity;
import com.lihao.news.adapter.SpecailMyIndicatorAdapter;
import com.lihao.news.bean.NewsData;
import com.lihao.news.menupage.newspagedetail.SpecailDetail;
import com.lihao.news.tools.LogUtil;
import com.lihao.news.tools.Tools;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hbm on 2017/4/16.
 * 专题菜单页面
 */

public class MenuSpecialPage extends MenuBasePage {

    private List<NewsData.DataBean.ChildrenBean> childers;
    private View view;
    private ViewPager mViewPager;
    private ScrollIndicatorView mIndicatorView;
    private ImageButton mImageButton;
    private int currentPosition;
    private List<SpecailDetail> newsDetails;
    private IndicatorViewPager indicatorViewPager;

    public MenuSpecialPage(Context context, NewsData.DataBean data) {
        super(context);
        childers = data.getChildren();
    }

    @Override
    public View initView() {
//        TextView tv = new TextView(context);
//        tv.setText("菜单--新闻页面");
//        tv.setTextColor(Color.RED);
        view = View.inflate(context, R.layout.menu_special_layout,null);

        mIndicatorView = (ScrollIndicatorView) view.findViewById(R.id.indicatorView);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mImageButton = (ImageButton) view.findViewById(R.id.imagebtn);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indicatorViewPager.setCurrentItem(indicatorViewPager.getCurrentItem()+1,false);
            }
        });
        indicatorViewPager = new IndicatorViewPager(mIndicatorView,mViewPager);
//        mIndicatorView.set
        LogUtil.e("菜单--新闻页面view初始化完成");
        return view;
    }

    public void initData(){
        if (childers!=null&&childers.size()>0) {
            int i = Tools.px2dip(context, 24);
            indicatorViewPager.setIndicatorOnTransitionListener(
                    new OnTransitionTextListener().setColor(Color.RED,Color.BLACK).
                            setSize(i,i));
            indicatorViewPager.setIndicatorScrollBar(new ColorBar(context,Color.GRAY,Color.RED));
            LogUtil.e("菜单--新闻页面data初始化了");
            newsDetails = new ArrayList<>();
            for (int j = 0; j < childers.size(); j++) {
                newsDetails.add(new SpecailDetail(context,childers.get(j)));
            }

            indicatorViewPager.setAdapter(new SpecailMyIndicatorAdapter(context,childers,newsDetails));
            //通过viewpager实现侧滑菜单的控制
//            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                @Override
//                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                    MainActivity activity = (MainActivity) context;
//                    SlidingMenu slidingMune = activity.getSlidingMune();
//                    LogUtil.e("position"+position);
//                    if (position==0){
//                        slidingMune.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//                        return;
//                    }
//                    slidingMune.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
//                }
//
//                @Override
//                public void onPageSelected(int position) {
//                }
//
//                @Override
//                public void onPageScrollStateChanged(int state) {
//
//                }
//            });
            indicatorViewPager.setCurrentItem(currentPosition,true);
            indicatorViewPager.setOnIndicatorPageChangeListener(new MyOnIndicatorPageChangeListener());
        }
    }

    private class MyOnIndicatorPageChangeListener implements IndicatorViewPager.OnIndicatorPageChangeListener {
        @Override
        public void onIndicatorPageChange(int preItem, int currentItem) {
            currentPosition = currentItem;
            MainActivity activity = (MainActivity) context;
            SlidingMenu slidingMune = activity.getSlidingMune();
            LogUtil.e("position"+currentItem);
            if (currentItem==0){
                slidingMune.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                return;
            }
            slidingMune.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

    }
}
