package com.lihao.news.tools.memory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.lihao.news.tools.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by hbm on 2017/4/23.
 * 作者：李浩
 * 时间：2017/4/23
 * 类的作用：本地缓存工具类
 */

class LocalCache {
    private MemoryCache memoryCache;
    public LocalCache(MemoryCache memoryCache){
        this.memoryCache = memoryCache;
    }

    public Bitmap getBitmapFromSD(String url) {
        //再从SdCard中取出图片后将改图片在放入到Momery中去
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //挂载了SDCard
            //保存图片在/mnt/sdcard/beijingnews/http://192.168.21.165:8080/xsxxxx.png
            //保存图片在/mnt/sdcard/beijingnews/llkskljskljklsjklsllsl
            String fileName = url;
            File file = new File(Environment.getExternalStorageDirectory(),"/xinwen"+fileName);
            if (file.exists()){
                //文件存在
                try {
                    FileInputStream fis = new FileInputStream(file);
                    Bitmap b = BitmapFactory.decodeStream(fis);
                    if (b!=null) {
                        LogUtil.e("从SdCard中获取图片成功");
                        return b;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    LogUtil.e("从文件中读取Bitmap失败");
                }
            }
        }
        return null;
    }

    public void putBitmapToSD(String url,Bitmap bitmap) {

        //再从SdCard中取出图片后将改图片在放入到Momery中去
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //挂载了SDCard
            //保存图片在/mnt/sdcard/beijingnews/http://192.168.21.165:8080/xsxxxx.png
            //保存图片在/mnt/sdcard/beijingnews/llkskljskljklsjklsllsl
            String fileName = url;
            File file = new File(Environment.getExternalStorageDirectory(), "/xinwen" + fileName);
            File parant = file.getParentFile();
            if (!parant.exists()) {
                parant.mkdirs();
            }
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(file));
                    LogUtil.e("存储图片到SdCard中成功");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        LogUtil.e("没有内存卡");
    }
}
