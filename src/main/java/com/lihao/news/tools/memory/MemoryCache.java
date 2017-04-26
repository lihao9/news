package com.lihao.news.tools.memory;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.lihao.news.tools.LogUtil;

/**
 * Created by hbm on 2017/4/23.
 * 作者：李浩
 * 时间：2017/4/23
 * 类的作用：应用缓存的处理类
 */




class MemoryCache {
    private LruCache<String,Bitmap> lruCache;
    public MemoryCache(){
        //缓存的大小
        int l = (int) (Runtime.getRuntime().maxMemory()/1024/8);
        lruCache = new LruCache<String,Bitmap>(l){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //api12以上时
//                value.getByteCount() = value.getRowBytes()*value.getHeight()
                return value.getRowBytes()*value.getHeight()/1024;
            }
        };
    }

//获取Bitmap
    public  Bitmap getBitMapFromMomery(String Url) {
        LogUtil.e("内存获取图片成功");
        return lruCache.get(Url);
    }
    //存储Bitmap
    public void putBitmapToMomery(String Url,Bitmap b){
        lruCache.put(Url,b);
        LogUtil.e("内存存储成功");
    }
}
