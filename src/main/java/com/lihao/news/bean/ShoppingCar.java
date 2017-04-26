package com.lihao.news.bean;

/**
 * Created by hbm on 2017/4/24.
 * 作者：李浩
 * 时间：2017/4/24
 * 类的作用：购物车中商品实体类
 */

public class ShoppingCar extends HotGoods.GoodsBean{
    /**
     * 购买的数量
     */
    private int count = 1;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    /**
     * 是否勾选
     */
    private boolean isCheck = true;


    @Override
    public String toString() {
        return "ShoppingCar{" +
                "count=" + count +
                ", isCheck=" + isCheck +
                '}';
    }
}
