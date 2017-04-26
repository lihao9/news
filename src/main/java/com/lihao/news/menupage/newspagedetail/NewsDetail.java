package com.lihao.news.menupage.newspagedetail;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lihao.news.R;
import com.lihao.news.bean.NewsData;
import com.lihao.news.bean.NewsDetailData;
import com.lihao.news.tools.Constant;
import com.lihao.news.tools.LogUtil;
import com.lihao.news.tools.Tools;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hbm on 2017/4/17.
 * 新闻页面中新闻菜单中的各个类别的显示页面
 * 一个轮播的图片--viewPager
 * 一个ListView--显示新闻内容
 * 每一个类别的新闻详情都是这样的界面--显示内容不同而已
 */

public class NewsDetail {
    //各个类别页面的详情信息的布局
    public NewsDetailData newsData;
    public View viewRoot;
    public TextView tv;
    public Context context;
    public ListView listview;
    public LinearLayout linearLayout;
    public ViewPager viewpager;
    //顶部新闻
    private ArrayList<NewsDetailData.DataBean.TopnewsBean> Topnews;
    public NewsData.DataBean.ChildrenBean chidrenBean;
    private List<NewsDetailData.DataBean.NewsBean> news;
    private Handler handler = new Handler();

    public NewsDetail(Context context, NewsData.DataBean.ChildrenBean childrenBean){
        this.context = context;
        this.chidrenBean = childrenBean;
        viewRoot = initView();
    }
    //初始化视图
    public View initView(){
        View TopNews = View.inflate(context, R.layout.top_news,null);
        viewpager = (ViewPager) TopNews.findViewById(R.id.image_viewpager);
        linearLayout = (LinearLayout) TopNews.findViewById(R.id.ll_image);
        tv = (TextView) TopNews.findViewById(R.id.image_title);

        View view = View.inflate(context,R.layout.menu_news_detail,null);
        listview = (ListView) view.findViewById(R.id.lv_content);
        listview.addHeaderView(TopNews);
        return view;
    }
    //请求数据并设置到view中
    public void initData(){
        //拿到各个类别中的数据--各个类别的url--类别北京的数据url
        String NewsUrl = Constant.BASE_URL+chidrenBean.getUrl();
        LogUtil.e("url："+NewsUrl);
//        String data = CacheUtil.getString(context, "newDetailDate", "");
//        if (!TextUtils.isEmpty(data)){
//            LogUtil.e("data:"+data);
//            processData(data);
//            return;
//        }
        RequestParams params = new RequestParams(NewsUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.i("成功");
                //存储数据
//                CacheUtil.putString(context,"newDetailDate",result);
                //解析和处理数据。
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("失败"+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("取消"+cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("完成");
            }
        });
    }


    class MyRunnable implements Runnable{
            @Override
            public void run() {
                viewpager.setCurrentItem((viewpager.getCurrentItem()+1)%Topnews.size());
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new MyRunnable(),2000);
        }
    }
    //解析和处理数据
    private void processData(String result) {
        newsData = parsedGson(result);

        Topnews = (ArrayList<NewsDetailData.DataBean.TopnewsBean>) newsData.getData().getTopnews();
//        LogUtil.i(newsData.getData().getNews().get(0).getTitle());

        //绑定数据到视图上。
        viewpager.setAdapter(new MyImageVPAdapter());
        viewpager.setOnPageChangeListener(new MyPageChangeListener());
        tv.setText(Topnews.get(0).getTitle());

        news = newsData.getData().getNews();
//        listview.addHeaderView(viewpager);
        listview.setAdapter(new MyLVAdapter());
        setDot();
        handler.postDelayed(new MyRunnable(),2000);
    }

    /**
     * 为轮播图设置红点
     */
    private void setDot() {
        linearLayout.removeAllViews();
        for (int i = 0; i < Topnews.size(); i++) {
            ImageView image = new ImageView(context);
            image.setBackgroundResource(R.drawable.dor_selector);
            int WandH = Tools.px2dip(context,12);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WandH,WandH);
            if (i!=0){
                image.setEnabled(false);
                params.leftMargin = WandH;
            }else {
                image.setEnabled(true);
            }
            image.setLayoutParams(params);
            linearLayout.addView(image);
        }
    }

    private NewsDetailData parsedGson(String result) {
        return new Gson().fromJson(result, NewsDetailData.class);
    }


    private class MyImageVPAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return Topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView image = new ImageView(context);
            String imageurl = Constant.BASE_URL+Topnews.get(position).getTopimage();
            LogUtil.i(imageurl);
            x.image().bind(image,imageurl);
            container.addView(image);
            image.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN://按下
                            LogUtil.e("按下");
                            //是把消息队列所有的消息和回调移除
                            handler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_UP://离开
                            LogUtil.e("离开");
                            //是把消息队列所有的消息和回调移除
                            handler.removeCallbacksAndMessages(null);
                            handler.postDelayed(new MyRunnable(), 2000);
                            break;
//                        case MotionEvent.ACTION_CANCEL://取消
//                            LogUtil.e("取消");
//                            //是把消息队列所有的消息和回调移除
//                            internalHandler.removeCallbacksAndMessages(null);
//                            internalHandler.postDelayed(new MyRunnable(), 4000);
//                            break;
                    }
                    return true;
                }
            });
            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //轮播图片时更改topNews的title;
            tv.setText(Topnews.get(position).getTitle());
            //设置红点的移动
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                ImageView image = (ImageView) linearLayout.getChildAt(i);
                if (i==position){
                    image.setEnabled(true);
                }else {
                    image.setEnabled(false);
                }

            }
        }
        private  boolean isDragging = false;
        @Override
        public void onPageScrollStateChanged(int state) {
            if(state ==ViewPager.SCROLL_STATE_DRAGGING){//拖拽
                isDragging = true;
                LogUtil.e("拖拽");
                //拖拽要移除消息
                handler.removeCallbacksAndMessages(null);
            }else if(state ==ViewPager.SCROLL_STATE_SETTLING&&isDragging){//惯性
                //发消息
                LogUtil.e("惯性");
                isDragging = false;
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new MyRunnable(),4000);

            }else if(state ==ViewPager.SCROLL_STATE_IDLE&&isDragging){//静止状态
                //发消息
                LogUtil.e("静止状态");
                isDragging = false;
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new MyRunnable(),4000);
            }
        }
    }

    private class MyLVAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NewsDetailData.DataBean.NewsBean newsBean = news.get(position);
            String imageUrl = Constant.BASE_URL+newsBean.getListimage();
            ViewHoulder houlder;
            if (convertView==null){
                convertView = View.inflate(context,R.layout.listview_news_item,null);
                houlder = new ViewHoulder();
                convertView.setTag(houlder);
                houlder.newsIv = (ImageView) convertView.findViewById(R.id.news_iv);
                houlder.newsTitle = (TextView) convertView.findViewById(R.id.news_title);
                houlder.newsDate = (TextView) convertView.findViewById(R.id.news_date);
            }else {
                houlder = (ViewHoulder) convertView.getTag();
            }
            x.image().bind(houlder.newsIv, imageUrl);
            houlder.newsTitle.setText(newsBean.getTitle());
            houlder.newsDate.setText(newsBean.getPubdate());
            return convertView;
        }
    }

    class ViewHoulder{
        private ImageView newsIv;
        private TextView newsTitle;
        private TextView newsDate;
    }

}
