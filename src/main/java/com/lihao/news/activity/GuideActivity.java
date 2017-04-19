package com.lihao.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lihao.news.R;
import com.lihao.news.tools.LogUtil;
import com.lihao.news.tools.Tools;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mRedDot;
    private float initLeftMargin;
    private LinearLayout mLl;
    private int[] imageid = {R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
    private List<ImageView> images;
    private ViewPager mVp;
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
    }

    private void initView() {
        mRedDot = (ImageView) findViewById(R.id.iv_red_dot);
        mRedDot.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());
        int dotWight = Tools.dip2px(GuideActivity.this,10);
        int dotheight = Tools.dip2px(GuideActivity.this,10);
        mLl = (LinearLayout) findViewById(R.id.guide_ll);
        mBtn = (Button) findViewById(R.id.guide_ib_enter_home);
        mBtn.setOnClickListener(this);
        images = new ArrayList<>();
        for (int i = 0; i < imageid.length ; i++) {
            //创建viewpager的图片
            ImageView image = new ImageView(this);
            image.setImageResource(imageid[i]);
            images.add(image);

            //创建点
            ImageView dot = new ImageView(this);
            dot.setBackgroundResource(R.drawable.dot);
            LinearLayout.LayoutParams paramas = new LinearLayout.LayoutParams(dotWight,dotheight);
            if (i!=0){
                paramas.leftMargin = dotWight;
            }
            dot.setLayoutParams(paramas);
            mLl.addView(dot);
        }
        mVp = (ViewPager) findViewById(R.id.splash_vp);
        mVp.setAdapter(new SplashPageradapter());
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                //红点移动的距离：两点间的距离=页面移动的距离：屏幕的距离=屏幕滑动的百分比
                int leftMargin = (int) (positionOffset*initLeftMargin);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRedDot.getLayoutParams();
                //红点位置 = 起始位置+移动位置
                params.leftMargin = (int) (position*initLeftMargin+leftMargin);
                mRedDot.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                if (position==images.size()-1){
                    mBtn.setVisibility(View.VISIBLE);
                }else {
                    mBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(GuideActivity.this,MainActivity.class);
        startActivity(intent);
        getSharedPreferences("config",MODE_PRIVATE)
                .edit()
                .putBoolean("isEnter",true)
                .commit();
        finish();
    }

    private class SplashPageradapter extends PagerAdapter {
        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(images.get(position));
            return images.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(images.get(position));
        }
    }

    private class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            mRedDot.getViewTreeObserver().removeOnGlobalLayoutListener(new MyOnGlobalLayoutListener());
            //间距
            initLeftMargin = mLl.getChildAt(1).getLeft()-mLl.getChildAt(0).getLeft();
            LogUtil.e("initLeftMargin"+initLeftMargin);
        }
    }
}
