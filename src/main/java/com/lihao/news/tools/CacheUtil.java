package com.lihao.news.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hbm on 2017/4/17.
 */

public class CacheUtil {
    public static void putStringToSp(Context context,String key,String values){
        SharedPreferences sp = context.getSharedPreferences("cache", Context.MODE_PRIVATE);
        sp.edit().putString(key,values).commit();
    }

    public static String getStringFromSp(Context context,String key,String values){
        SharedPreferences sp = context.getSharedPreferences("cache", Context.MODE_PRIVATE);
        return sp.getString(key,values);
    }




}
