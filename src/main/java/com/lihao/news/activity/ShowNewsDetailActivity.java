package com.lihao.news.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lihao.news.R;
import com.lihao.news.tools.Constant;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class ShowNewsDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar mPb;
    private String mUrl;
    private WebView mWebView;
    private String[] textSize = {"超大字体","大字体","中等字体","小字体","超小字体"};
//    private PullToRefreshWebView mPullRefreshWebView;

    private TextView tvTitle;
    private ImageButton ibMenu;
    private ImageButton ibBack;
    private ImageButton ibTextsize;
    private ImageButton ibShare;
    private ImageButton ibSwichListGrid;
    private Button btnCart;

    private WebViewClient client;
    private  String title;
    private WebSettings settings;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-04-21 10:37:40 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        mPb = (ProgressBar) findViewById(R.id.progressbar);
        tvTitle = (TextView)findViewById( R.id.tv_title );
        ibMenu = (ImageButton)findViewById( R.id.ib_menu );
        ibBack = (ImageButton)findViewById( R.id.ib_back );
        ibBack.setVisibility(View.VISIBLE);
        ibTextsize = (ImageButton)findViewById( R.id.ib_textsize );
        ibTextsize.setVisibility(View.VISIBLE);
        ibShare = (ImageButton)findViewById( R.id.ib_share );
        ibShare.setVisibility(View.VISIBLE);
//        ibSwichListGrid = (ImageButton)findViewById( R.id.ib_swich_list_grid );
//        btnCart = (Button)findViewById( R.id.btn_cart );

//        mPullRefreshWebView = (PullToRefreshWebView) findViewById(R.id.pull_refresh_webview);
//        mWebView = mPullRefreshWebView.getRefreshableView();
        mWebView = (WebView) findViewById(R.id.webview);
        //控制是否在自己的应用中打开网页
        client = new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mPb.setVisibility(View.GONE);
            }
        };

        settings = mWebView.getSettings();
        mPb = (ProgressBar) findViewById(R.id.progressbar);

        ibMenu.setOnClickListener( this );
        ibBack.setOnClickListener( this );
        ibTextsize.setOnClickListener( this );
        ibShare.setOnClickListener( this );
//        ibSwichListGrid.setOnClickListener( this );
//        btnCart.setOnClickListener( this );
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-04-21 10:37:40 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
         if ( v == ibBack ) {
            // Handle clicks for ibBack
             finish();
        } else if ( v == ibTextsize ) {
            // Handle clicks for ibTextsize--选择字体大小
             showTextSizeChangeDialog();
        } else if ( v == ibShare ) {
            // Handle clicks for ibShare--分享
             showShare();
        }
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

    private int currenTextSize = 200;
    private int currentLocation = 2;
    private void showTextSizeChangeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowNewsDetailActivity.this);
        builder.setTitle("请选择文字大小");
        builder.setSingleChoiceItems(textSize, currentLocation, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentLocation = which;
                switch (which){
                case 0:
                    currenTextSize = 280;
                    break;
                case 1:
                    currenTextSize = 240;
                    break;
                case 2:
                    currenTextSize = 200;
                    break;
                case 3:
                    currenTextSize = 150;
                    break;
                case 4:
                    currenTextSize = 100;
                    break;
            }
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                settings.setTextZoom(currenTextSize);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news_detail);
        getData();
        findViews();
        initData();
    }

    private void initData() {
        //设置是否支持javaScript--开启则可应答网页的事件
        settings.setJavaScriptEnabled(true);
        //开启缓存
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.loadUrl(mUrl);
        mWebView.setWebViewClient(client);
        //设置双击变大变小
        settings.setUseWideViewPort(true);
        //增加缩放按钮
        settings.setBuiltInZoomControls(true);
        settings.setTextZoom(200);
        tvTitle.setText(title);
    }
    private void getData(){
        title = getIntent().getStringExtra("title");
        mUrl = Constant.BASE_URL+getIntent().getStringExtra("url");
    }

}
