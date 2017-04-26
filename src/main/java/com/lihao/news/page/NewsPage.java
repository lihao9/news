package com.lihao.news.page;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lihao.news.R;
import com.lihao.news.activity.MainActivity;
import com.lihao.news.bean.NewsData;
import com.lihao.news.menupage.MenuBasePage;
import com.lihao.news.menupage.MenuImagePage;
import com.lihao.news.menupage.MenuInteractionPage;
import com.lihao.news.menupage.MenuNewsPage;
import com.lihao.news.menupage.MenuSpecialPage;
import com.lihao.news.tools.CacheUtil;
import com.lihao.news.tools.Constant;
import com.lihao.news.tools.LogUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hbm on 2017/4/13.
 */

public class NewsPage extends BasePage implements View.OnClickListener {
    private List<MenuBasePage> mMenuBasePages;
//    private int preMenuPosition;
    private MainActivity activity;
    private MyLVAdapter adapter;
    public static NewsData newsData;
    private int prePosition;
    private List<NewsData.DataBean> data;

    public NewsPage(Context context){
        //构造方法调用父类的
        super(context);
        activity = (MainActivity) context;
        mMenuBasePages = new ArrayList<>();
    }

    public void initData(){
        //新闻页面，显示进入侧滑菜单
        mIbMune.setVisibility(View.VISIBLE);
        mIbMune.setOnClickListener(this);
        mTvTitle.setText("新闻中心");
        //为各个子页面获取数据并设置数据
        LogUtil.e("新闻页面初始化了");

        //xUtils联网请求数据
        String cacheData = CacheUtil.getStringFromSp(context,"newsData","");
        if (!TextUtils.isEmpty(cacheData)){
            processData(cacheData);
        }else {
            RequestParams paramas = new RequestParams(Constant.NEWS_URL);
            x.http().get(paramas, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    LogUtil.i("请求数据成功"+result);
                    if (result!=null&&!"".equals(result)){
                        CacheUtil.putStringToSp(context,"newsData",result);
                        //处理json数据
                        processData(result);
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    LogUtil.i("请求数据失败"+ex.getMessage());
                }

                //网络请求停止时调用
                @Override
                public void onCancelled(CancelledException cex) {

                    LogUtil.e("onCancelled"+cex.getMessage());
                }

                //请求完成时调用
                @Override
                public void onFinished() {
                    LogUtil.e("onFinished");
                }

            });
        }
//        mTvTitle.setText("新闻页面");
//        TextView tv = new TextView(context);
//        tv.setText("新闻");
//        tv.setTextColor(Color.RED);

        //向mFl中添加Fragment--初始化为菜单--新闻的页面

    }

    private void swichMenuPage(int position) {
        if (position<mMenuBasePages.size()) {
            mFl.removeAllViews();
            mTvTitle.setText(data.get(position).getTitle());
            mMenuBasePages.get(position).initData();
            mFl.addView(mMenuBasePages.get(position).rootView);
            if (position==2){
                ibSwichListGrid.setVisibility(View.VISIBLE);
                final MenuImagePage imagepager = (MenuImagePage) mMenuBasePages.get(position);
                ibSwichListGrid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imagepager.swithImageShowDOme(ibSwichListGrid);
                    }
                });
            }else {
                ibSwichListGrid.setVisibility(View.GONE);
            }
        }
    }

    //处理gson数据
    private void processData(String result) {
        //解析gson数据
        newsData = parsedGson(result);
        //测试用
        String title = newsData.getData().get(0).getTitle();
        LogUtil.e("title"+title);

        //设置gson数据
        //整个新闻数据中的分类信息（5个类别）
        data = newsData.getData();
        if (data.size()>0&&data!=null){
            View view = View.inflate(activity,R.layout.leftmenu,null);
            ListView listview = (ListView) view.findViewById(R.id.main_menu_lv);
            adapter = new MyLVAdapter();
            listview.setAdapter(adapter);
            activity.getSlidingMune().setMenu(view);
            listview.setOnItemClickListener(new MyOnItemClickListener());

            //当数据获取完毕后向集合中添加各菜单项页面的实例
            mMenuBasePages.add(new MenuNewsPage(context, data.get(0)));
            mMenuBasePages.add(new MenuSpecialPage(context,data.get(0)));
            mMenuBasePages.add(new MenuImagePage(context,data.get(2)));
            mMenuBasePages.add(new MenuInteractionPage(context,data.get(3)));
            swichMenuPage(prePosition);
        }else {

            //data数据为空

            //菜单栏无法拖出来，内容页面显示无网络
        }


    }

    private NewsData parsedGson(String result) {
        Gson gson = new Gson();
        NewsData newsData = gson.fromJson(result, NewsData.class);
        return newsData;
    }


    @Override
    public void onClick(View v) {
        MainActivity activity = (MainActivity) context;
        SlidingMenu slidingMune = activity.getSlidingMune();
        slidingMune.toggle();
    }

    private class MyLVAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return data.size();
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
            TextView textview = (TextView) View.inflate(activity,R.layout.text,null);
            textview.setText(data.get(position).getTitle());
            textview.setEnabled(position==prePosition);
            return textview;
        }
    }

    private class MyOnItemClickListener implements android.widget.AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            prePosition = position;
            adapter.notifyDataSetChanged();
            view.setEnabled(true);
            activity.getSlidingMune().toggle();
            swichMenuPage(position);
        }
    }
}
