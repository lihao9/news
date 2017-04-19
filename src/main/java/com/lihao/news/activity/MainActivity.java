package com.lihao.news.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lihao.news.R;
import com.lihao.news.adapter.MyPagerAdapter;
import com.lihao.news.customview.NoScrollViewPager;
import com.lihao.news.page.BasePage;
import com.lihao.news.page.GovermentPage;
import com.lihao.news.page.HomePage;
import com.lihao.news.page.NewsPage;
import com.lihao.news.page.SettingPage;
import com.lihao.news.page.ShoppingPage;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private int prePosition;
    private List<BasePage> pages;
//    private String[] menus = {"新闻","新闻","新闻","新闻","新闻"};
    private String[] menus;
    @ViewInject(R.id.main_vp)
    private NoScrollViewPager mVp;

    @ViewInject(R.id.main_rg)
    private RadioGroup mRg;

    private SlidingMenu menu;

    private MyLVAdapter adapter;

//    private int mNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x.view().inject(MainActivity.this);
        initView();
    }

    private void initView() {
        pages = new ArrayList<>();
        pages.add(new HomePage(this));
        pages.add(new NewsPage(this));
        pages.add(new ShoppingPage(this));
        pages.add(new GovermentPage(this));
        pages.add(new SettingPage(this));

        setLeftMune();
        mVp.setAdapter(new MyPagerAdapter(pages));
        mVp.addOnPageChangeListener(new MyOnPagerChangeListener());
        mRg.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        mRg.check(R.id.home);
    }

    private void setLeftMune() {
        // configure the SlidingMenu
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
//        menu.setShadowWidthRes(R.dimen.shadow_width);
//        menu.setShadowDrawable(R.drawable.shadow);

        // 设置滑动菜单视图的宽度
//        int slidingMuneWight = Tools.dip2px(MainActivity.this, 200);
//        R.dimen.slidingmenu_offset
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        /**
         * SLIDING_WINDOW will include the Title/ActionBar in the content
         * section of the SlidingMenu, while SLIDING_CONTENT does not.
         */
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
//        menu.setMenu(R.layout.leftmenu);

//        setMenuData();

//        View view = View.inflate(MainActivity.this,R.layout.leftmenu,null);
//        ListView listview = (ListView) view.findViewById(R.id.main_menu_lv);
//        adapter = new MyLVAdapter();
//        listview.setAdapter(adapter);
//        menu.setMenu(view);
//        listview.setOnItemClickListener(new MyOnItemClickListener());
    }

    private class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId){
                case R.id.news:
                    mVp.setCurrentItem(1,false);
                    menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    break;
                case R.id.home:
                    mVp.setCurrentItem(0,false);
                    menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.shopping:
                    mVp.setCurrentItem(2,false);
                    menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.government:
                    mVp.setCurrentItem(3,false);
                    menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.setting:
                    mVp.setCurrentItem(4,false);
                    menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                    break;
            }
        }
    }

    private class MyOnPagerChangeListener implements ViewPager.OnPageChangeListener {
        /**
         *
         * @param position
         * @param positionOffset  页面滑动的百分比
         * @param positionOffsetPixels  页面滑动的像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         *
         * @param position 已经被选中的页面
         */
        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0:
                    mRg.check(R.id.home);
                    pages.get(position).initData();
                    break;
                case 1:
                    mRg.check(R.id.news);
                    pages.get(position).initData();
//                    List<NewsData.DataBean> datas = NewsPage.newsData.getData();
//                    menus = new String[datas.size()];
//                    for (int i = 0;i<datas.size();i++){
//                        menus[i] = datas.get(i).getTitle();
//                    }
//
//                    View view = View.inflate(MainActivity.this,R.layout.leftmenu,null);
//                    ListView listview = (ListView) view.findViewById(R.id.main_menu_lv);
//                    adapter = new MyLVAdapter();
//                    listview.setAdapter(adapter);
//                    menu.setMenu(view);
//                    listview.setOnItemClickListener(new MyOnItemClickListener());

//                    setLeftMune();
                    break;
                case 2:
                    mRg.check(R.id.shopping);
                    pages.get(position).initData();
                    break;
                case 3:
                    mRg.check(R.id.government);
                    pages.get(position).initData();
                    break;
                case 4:
                    mRg.check(R.id.setting);
                    pages.get(position).initData();
                    break;
            }

        }

        /**
         * viewpager页面滑动状态改变时调用
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

//    private void setFragment() {
//        FragmentManager fManager = getSupportFragmentManager();
//        FragmentTransaction fTransaction = fManager.beginTransaction();
//        fTransaction.replace(R.id.main_page,new MyFragment(getPage()));
//        fTransaction.commit();
//    }
//
//    private BasePage getPage() {
//        BasePage basePage = pages.get(mNo);
//        if (basePage!=null&&!basePage.isInitData){
//            basePage.initData();
//            basePage.isInitData = true;
//        }
//        return basePage;
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVp.removeOnPageChangeListener(new MyOnPagerChangeListener());
    }

    private class MyLVAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 5;
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
            TextView textview = (TextView) View.inflate(MainActivity.this,R.layout.text,null);
            textview.setText(menus[position]);
            textview.setEnabled(position==prePosition);
            return textview;
        }
    }

//    private class MyOnItemClickListener implements android.widget.AdapterView.OnItemClickListener {
//
//
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            prePosition = position;
//            adapter.notifyDataSetChanged();
//            view.setEnabled(true);
//            menu.toggle();
//        }
//    }

    public SlidingMenu getSlidingMune(){
        return menu;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (menu.isMenuShowing()){
                menu.toggle();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}

