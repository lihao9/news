package com.lihao.news.menupage.newspagedetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lihao.news.R;
import com.lihao.news.activity.ShowNewsDetailActivity;
import com.lihao.news.bean.NewsData;
import com.lihao.news.bean.NewsDetailData;
import com.lihao.news.tools.CacheUtil;
import com.lihao.news.tools.Constant;
import com.lihao.news.tools.LogUtil;
import com.lihao.news.tools.Tools;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hbm on 2017/4/17.
 * 新闻页面中新闻菜单中的各个类别的显示页面
 * 一个轮播的图片--viewPager
 * 一个ListView--显示新闻内容
 * 每一个类别的新闻详情都是这样的界面--显示内容不同而已
 */

public class SpecailDetail {
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
    private PullToRefreshListView mPullRefreshListView;
    //加载更多数据的url
    private String loadMoreUrl;
    private String NewsUrl;
    private boolean isFirst = true;

    public SpecailDetail(Context context, NewsData.DataBean.ChildrenBean childrenBean){
        this.context = context;
        this.chidrenBean = childrenBean;
        viewRoot = initView();
    }
    //初始化视图
    public View initView(){
        View TopNews = View.inflate(context, R.layout.top_news,null);
        viewpager = (ViewPager) TopNews.findViewById(R.id.image_viewpager);
        viewpager.setOnPageChangeListener(new MyPageChangeListener());
        linearLayout = (LinearLayout) TopNews.findViewById(R.id.ll_image);
        tv = (TextView) TopNews.findViewById(R.id.image_title);

        adapter = new MyLVAdapter();
        View view = View.inflate(context,R.layout.listview_pull_to_refresh_item,null);
        mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);

        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                LogUtil.e("下拉刷新");
                getDataFromNet(NewsUrl);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (TextUtils.isEmpty(loadMoreUrl)){
                    Toast.makeText(context,"没有更多数据了",Toast.LENGTH_SHORT).show();
                    return;
                }
                LogUtil.e("上拉加载");
                isLoadMore = true;
                LogUtil.e("上拉加载地址"+loadMoreUrl);
                getDataFromNet(loadMoreUrl);
            }
        });
        listview = mPullRefreshListView.getRefreshableView();
        listview.setOnItemClickListener(new MyLVOnItemClickListener());
        listview.addHeaderView(TopNews);
        return view;
    }
    //请求数据并设置到view中
    public void initData(){
        //拿到各个类别中的数据--各个类别的url--类别北京的数据url
        NewsUrl = Constant.BASE_URL+chidrenBean.getUrl();
        LogUtil.e("url："+NewsUrl);
//        String data = CacheUtil.getString(context, "newDetailDate", "");
//        if (!TextUtils.isEmpty(data)){
//            LogUtil.e("data:"+data);
//            processData(data);
//            return;
//        }
        getDataFromNet(NewsUrl);
    }

    private boolean isLoadMore;
    private void getDataFromNet(String url) {
        RequestParams params = new RequestParams(url);
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
                mPullRefreshListView.onRefreshComplete();
            }
        });
    }

    private MyLVAdapter adapter;
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
        if(isLoadMore){
            //加载更多数据
            news.addAll(newsData.getData().getNews());
            adapter.notifyDataSetChanged();
            mPullRefreshListView.onRefreshComplete();
            isLoadMore = false;
            loadMoreUrl = Constant.BASE_URL+newsData.getData().getMore();
            LogUtil.e("loadMoreUrl"+loadMoreUrl);
        }else{
            //初次添加数据
            //顶部新闻集合
            if (isFirst) {
                isFirst = false;
                news = newsData.getData().getNews();
                Topnews = (ArrayList<NewsDetailData.DataBean.TopnewsBean>) newsData.getData().getTopnews();
                viewpager.setAdapter(new MyImageVPAdapter());
                tv.setText(Topnews.get(0).getTitle());
                setDot();
                loadMoreUrl = Constant.BASE_URL+newsData.getData().getMore();
                LogUtil.e("loadMoreUrl"+loadMoreUrl);
            }else {
                List<NewsDetailData.DataBean.NewsBean> newNews = newsData.getData().getNews();
                Iterator it = newNews.iterator();
                while (it.hasNext()){
                    NewsDetailData.DataBean.NewsBean next = (NewsDetailData.DataBean.NewsBean) it.next();
                    if (!news.contains(next))
                        news.add(next);
                }
            }
            //LogUtil.i(newsData.getData().getNews().get(0).getTitle());
            //绑定数据到视图上。
            //listview.addHeaderView(viewpager);
            listview.setAdapter(adapter);
            mPullRefreshListView.onRefreshComplete();
            handler.postDelayed(new MyRunnable(),2000);
        }
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
            String isClickId = CacheUtil.getStringFromSp(context, "isClickId", "");
            if (isClickId.contains(news.get(position).getId()+"")){
                houlder.newsTitle.setTextColor(Color.GRAY);
            }else {
                houlder.newsTitle.setTextColor(Color.BLACK);
            }
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

    private class MyLVOnItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int cachePosition = position-2;
            NewsDetailData.DataBean.NewsBean newsBean = news.get(cachePosition);
            String isClickId = CacheUtil.getStringFromSp(context, "isClickId", "");
            LogUtil.e("position"+position+"----"+"cachePosition"+cachePosition);
            if(!isClickId.contains(newsBean.getId()+"")){ //3512
                LogUtil.e("条目被点击"+position);
                CacheUtil.putStringToSp(context,"isClickId",isClickId+ newsBean.getId()+",");
//                CacheUtils.putString(context,READ_ARRAY_ID,idArray+newsData.getId()+",");//"3511,3512,"

                //刷新适配器
                adapter.notifyDataSetChanged();//getCount-->getView
            }
            Intent intent = new Intent(context,ShowNewsDetailActivity.class);
            intent.putExtra("title",newsBean.getTitle());
            intent.putExtra("url",newsBean.getUrl());
            context.startActivity(intent);
        }
    }
}
