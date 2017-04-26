package com.lihao.news.tools;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lihao.news.bean.HotGoods;
import com.lihao.news.bean.ShoppingCar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hbm on 2017/4/24.
 * 作者：李浩
 * 时间：2017/4/24
 * 类的作用：存储购物车中的数据操作类
 */

public class GoodsProvider {
    private Context context;
    //用于替换Hasmap来操作购物车中的数据
    private SparseArray<ShoppingCar> datas;
    public GoodsProvider(Context context){
        this.context = context;
        datas = new SparseArray<>();
        listToSparsw();
    }

    /**
     * 把购物车中的数据放到集合中
     * @return
     */
    public void listToSparsw() {
        List<ShoppingCar> goods = getGoods();
        for (int i = 0; i < goods.size(); i++) {
            ShoppingCar cart = goods.get(i);
            datas.put(cart.getId(),cart);
        }
    }

    /**
     * 获取goods
     */
    public List<ShoppingCar> getGoods(){
        //1.从Sp中获取购物车的goods
        return getAllData();
    }

    public List<ShoppingCar> getAllData() {
        List<ShoppingCar> shoppingGoods = new ArrayList<>();
        String localData = CacheUtil.getStringFromSp(context,"goods","");
        if (!TextUtils.isEmpty(localData)){
            //把json数据转化为list数据
            shoppingGoods = new Gson().fromJson(localData,new TypeToken<List<ShoppingCar>>() {
            }.getType());
        }
        return shoppingGoods;
    }

    public void addData(ShoppingCar cart){
        ShoppingCar cart1 = datas.get(cart.getId());
        //判断集合中是否有要添加的数据
        if (cart1!=null){
            cart1.setCount(cart1.getCount()+1);
        }else {
            //集合中没有这个商品
            cart1 = cart;
            cart1.setCount(1);
        }
        datas.put(cart.getId(),cart1);
        //添加到集合中后保存到Spzhong
        commit();
    }


    public void commit() {
        //1.把SparseArray转换成List
        List<ShoppingCar> carts =  SparseToList();

        //2.用Gson把List转换成String
        String json = new Gson().toJson(carts);

        //3.保存数据
        CacheUtil.putStringToSp(context,"goods",json);
    }

    /**
     * 把集合中的数据转化为list用于存储到Sp中。
     * @return
     */
    public List<ShoppingCar> SparseToList(){
        List<ShoppingCar> carList = new ArrayList<>();
        if (datas!=null&&datas.size()>0){
            for (int i = 0; i <datas.size() ; i++) {
                ShoppingCar shoppingCar = datas.valueAt(i);
                carList.add(shoppingCar);
            }
        }
        return carList;
    }

    /**
     * 删除数据
     *
     * @param cart
     */
    public void deleteData(ShoppingCar cart) {

        //1.删除数据
        datas.delete(cart.getId());

        //2.保存数据
        commit();
    }

    /**
     * 修改数据
     * @param cart
     */
    public void updataData(ShoppingCar cart) {

        //1.修改-count
        datas.put(cart.getId(), cart);

        //2.保存数据
        commit();
    }

    /**
     *  把商品Wares转换成ShoppingCart
     */

    public ShoppingCar conversion(HotGoods.GoodsBean goods) {
        ShoppingCar cart = new ShoppingCar();
        cart.setImgUrl(goods.getImgUrl());
//        cart.setDescription(goods.getDescription());
        cart.setName(goods.getName());
        cart.setId(goods.getId());
        cart.setSale(goods.getSale());
        cart.setPrice(goods.getPrice());
        return cart;
    }


}
