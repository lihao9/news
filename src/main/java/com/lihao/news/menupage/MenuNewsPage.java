package com.lihao.news.menupage;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lihao.news.R;
import com.lihao.news.activity.MainActivity;
import com.lihao.news.bean.NewsData;
import com.lihao.news.menupage.newspagedetail.NewsDetail;
import com.lihao.news.tools.LogUtil;
import com.lihao.news.tools.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hbm on 2017/4/16.
 * 新闻页面
 */

public class MenuNewsPage extends MenuBasePage {
    private List<NewsData.DataBean.ChildrenBean> childers;
    private View view;
    private ViewPager mViewPager;
    //    private ScrollIndicatorView mIndicatorView;
    private TabLayout mTablayout;
    private ImageButton mImageButton;
    private int currentPosition;
    private List<NewsDetail> newsDetails;
    //    private IndicatorViewPager indicatorViewPager;
    public MenuNewsPage(Context context, NewsData.DataBean data) {
        super(context);
        childers = data.getChildren();
    }

    @Override
    public View initView() {
//        TextView tv = new TextView(context);
//        tv.setText("菜单--新闻页面");
//        tv.setTextColor(Color.RED);
        view = View.inflate(context, R.layout.menu_news_layout,null);
//        mIndicatorView = (ScrollIndicatorView) view.findViewById(R.id.indicatorView);
        //tablayout的设置
        mTablayout = (TabLayout) view.findViewById(R.id.tablelayout);
        //设置tablelayout 时可以滑动的
        mTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);

        mTablayout.setupWithViewPager(mViewPager,true);


        mImageButton = (ImageButton) view.findViewById(R.id.imagebtn);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                indicatorViewPager.setCurrentItem(indicatorViewPager.getCurrentItem()+1,false);
                mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
            }
        });
//        indicatorViewPager = new IndicatorViewPager(mIndicatorView,mViewPager);


        LogUtil.e("菜单--新闻页面view初始化完成");
        return view;
    }

    public void initData(){
        if (childers!=null&&childers.size()>0) {
            int i = Tools.px2dip(context, 24);
//            indicatorViewPager.setIndicatorOnTransitionListener(
//                    new OnTransitionTextListener().setColor(Color.RED,Color.BLACK).
//                            setSize(i,i));
//            indicatorViewPager.setIndicatorScrollBar(new ColorBar(context,Color.GRAY,Color.RED));

            LogUtil.e("菜单--新闻页面data初始化了");
            newsDetails = new ArrayList<>();
            for (int j = 0; j < childers.size(); j++) {
                newsDetails.add(new NewsDetail(context,childers.get(j)));
            }
//            indicatorViewPager.setAdapter(new MyIndicatorAdapter(context,childers,newsDetails));
            mViewPager.setAdapter(new MyTabPagerAdapter());

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


//            indicatorViewPager.setCurrentItem(currentPosition,true);
            mViewPager.setCurrentItem(currentPosition);
//            indicatorViewPager.setOnIndicatorPageChangeListener(new MyOnIndicatorPageChangeListener());
            mViewPager.addOnPageChangeListener(new MyTabOnPageChangeListener());
        }
    }

//    private class MyOnIndicatorPageChangeListener implements IndicatorViewPager.OnIndicatorPageChangeListener {
//        @Override
//        public void onIndicatorPageChange(int preItem, int currentItem) {
//            currentPosition = currentItem;
//            MainActivity activity = (MainActivity) context;
//            SlidingMenu slidingMune = activity.getSlidingMune();
//            LogUtil.e("position"+currentItem);
//            if (currentItem==0){
//                slidingMune.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//                return;
//            }
//            slidingMune.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
//        }
//
//    }

    private class MyTabPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return newsDetails.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            NewsDetail newsDetail = newsDetails.get(position);
            View view = newsDetail.viewRoot;
            if (position==0){
                newsDetail.initData();
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return childers.get(position).getTitle();
        }
    }

    private class MyTabOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            newsDetails.get(position).initData();
            currentPosition = position;
            MainActivity activity = (MainActivity) context;
            SlidingMenu slidingMune = activity.getSlidingMune();
            LogUtil.e("position"+position);
            if (position==0){
                slidingMune.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                return;
            }
            slidingMune.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
