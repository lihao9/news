package com.lihao.news.tools.memory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import com.lihao.news.tools.LogUtil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hbm on 2017/4/23.
 * 作者：李浩
 * 时间：2017/4/23
 * 类的作用：网络获取图片
 */

class NetCache {
    private static final int SUCCESS = 1;
    private static final int FAILD = 2;
    private MemoryCache memoryCache;
    private LocalCache localCache;
    private Handler handler;
    public NetCache(MemoryCache memoryCache,LocalCache localCache,Handler handler){
        this.memoryCache = memoryCache;
        this.localCache = localCache;
        this.handler = handler;
    }
    public void getBitMapFromNet(String url) {

        //需要在子线程中获取数据
        new Thread(new MyRunnable(url)).start();
    }


    private class MyRunnable implements Runnable  {
        private String url;
        public MyRunnable(String url){
            this.url = url;
        }
        @Override
        public void run() {
            try {
                URL Url = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.connect();
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    //连接成功
                    InputStream is = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    if (bitmap!=null){
                        //分别在内存和Sdcard中存一份
                        memoryCache.putBitmapToMomery(url,bitmap);
                        localCache.putBitmapToSD(url,bitmap);
                        //将图片加载到控件上
                        Message msg = new Message();
                        msg.what = SUCCESS;
                        msg.obj = bitmap;
                        handler.sendMessage(msg);
                        LogUtil.e("网络加载图片成功");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Message msg = new Message();
                msg.what = FAILD;
                handler.sendMessage(msg);
            }
        }
    }
}
