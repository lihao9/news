package com.lihao.news.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lihao.news.R;
import com.lihao.news.bean.NewsData;
import com.lihao.news.menupage.newspagedetail.SpecailDetail;
import com.lihao.news.tools.LogUtil;
import com.lihao.news.tools.Tools;
import com.shizhefei.view.indicator.IndicatorViewPager;

import java.util.List;

/**
 * Created by hbm on 2017/4/17.
 */

public class SpecailMyIndicatorAdapter extends IndicatorViewPager.IndicatorViewPagerAdapter {
    private List<NewsData.DataBean.ChildrenBean> children;
    private Context context;
    private List<SpecailDetail> newsdetail;
    public SpecailMyIndicatorAdapter(Context context, List<NewsData.DataBean.ChildrenBean> children, List<SpecailDetail> newsDetail){
        this.context = context;
        this.children = children;
        this.newsdetail = newsDetail;
    }
    @Override
    public int getCount() {
        return newsdetail.size();
    }

    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView==null){
            convertView = View.inflate(context, R.layout.tab_text,null);
        }
        TextView tv = (TextView) convertView;
        tv.setText(children.get(position).getTitle());
        int witdh = getTextWidth(tv);
        int padding = Tools.px2dip(context, 8);
        //因为wrap的布局 字体大小变化会导致textView大小变化产生抖动，这里通过设置textView宽度就避免抖动现象
        //1.3f是根据上面字体大小变化的倍数1.3f设置
        tv.setWidth((int) (witdh * 1.3f) + padding);
//        container.addView(convertView);
        return convertView;
    }

    @Override
    public View getViewForPage(int position, View convertView, ViewGroup container) {
        LogUtil.e("此处需要改-----返回新闻页面下，新闻菜单中的各个类别的详情内容");
//        View view = View.inflate(context,R.layout.menu_news_detail,null);
        //各个类别的数据
//        NewsData.DataBean.ChildrenBean childrenBean = children.get(position);
        SpecailDetail newsDetail = newsdetail.get(position);
        View view = newsDetail.viewRoot;
        newsDetail.initData();

//        container.addView(convertView);

//        TextView tv = new TextView(context);
//        tv.setTextColor(Color.RED);
//        tv.setGravity(Gravity.CENTER);
//        tv.setText(children.get(position).getTitle());
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_UNCHANGED;
    }

    private int getTextWidth(TextView textView) {
        if (textView == null) {
            return 0;
        }
        Rect bounds = new Rect();
        String text = textView.getText().toString();
        Paint paint = textView.getPaint();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int width = bounds.left + bounds.width();
        return width;
    }
}
