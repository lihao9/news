package com.lihao.news.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.lihao.news.R;
import com.lihao.news.tools.Constant;
import com.lihao.news.tools.LogUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ShowPictureActivity extends AppCompatActivity {

    private PhotoView photoView;
    private String imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);
        photoView = (PhotoView) findViewById(R.id.photoView);
        initData();
    }

    private void initData() {
        imageUrl = Constant.BASE_URL+getIntent().getStringExtra("iamgeUrl");
        LogUtil.e("图片地址"+imageUrl);
        final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
        Picasso.with(this).
                load(imageUrl).
                into(photoView, new Callback() {
                    @Override
                    public void onSuccess() {
                        attacher.update();
                    }

                    @Override
                    public void onError() {

                    }
                });
    }
}
