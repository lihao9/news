package com.lihao.news.tools.memory;

import android.graphics.Bitmap;
import android.os.Handler;

import com.lihao.news.tools.LogUtil;

/**
 * Created by hbm on 2017/4/23.
 * 数据缓存类--用于存储和获取数据
 */

public class BitMapMemory {

    /**
     * 内存工具类
     */
    private MemoryCache memoryCache;

    private LocalCache localCache;
    private NetCache netCache;


    public BitMapMemory(Handler handler){
        this.memoryCache = new MemoryCache();
        this.localCache = new LocalCache(memoryCache);
        this.netCache = new NetCache(memoryCache,localCache,handler);
    }

    /**
     * 三级缓存设计步骤：
     *   * 从内存中取图片
     *   * 从本地文件中取图片
     *        向内存中保持一份
     *   * 请求网络图片，获取图片，显示到控件上,Hanlder,postion
     *      * 向内存存一份
     *      * 向本地文件中存一份
     */
    public Bitmap getBitMap(String Url){
        //先从缓存中获取
        if (memoryCache!=null){
            Bitmap b = memoryCache.getBitMapFromMomery(Url);
            if (b!=null){
                LogUtil.e("从缓存中加载图片成功");
                return b;
            }
        }
        //从SDcard中获取图片
        if (localCache!=null){
            Bitmap b = localCache.getBitmapFromSD(Url);
            if (b!=null){
                LogUtil.e("从SdCard中加载图片成功");
                return b;
            }
        }
        //从网络中获取图片
        netCache.getBitMapFromNet(Url);
        return null;

    }
}
