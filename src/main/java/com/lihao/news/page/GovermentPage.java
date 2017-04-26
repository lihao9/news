package com.lihao.news.page;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lihao.news.R;
import com.lihao.news.bean.ShoppingCar;
import com.lihao.news.customview.MyCountView;
import com.lihao.news.tools.GoodsProvider;
import com.lihao.news.tools.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static com.lihao.news.R.id.btn_delete;

/**
 * Created by hbm on 2017/4/13.
 */

public class GovermentPage extends BasePage {
    //编辑完成
    private static final int SUCCESS = 1;
    //正在编辑
    private static final int EDIT = 2;
    private RecyclerView recyclerView;
    private TextView tvNOGoods;
    private CheckBox checkBox;
    private TextView tvShowPrice;
    private Button btnBuy,btnDelet;
    private MyrecyclerViewAdapter adapter;
    private GoodsProvider goodsProvider;
    private List<ShoppingCar> allData;
    private List<ShoppingCar> isCheckedData;
    private boolean isCheckAll;

    public GovermentPage(Context context){
        //构造方法调用父类的
        super(context);

    }

    public void initData(){
        goodsProvider = new GoodsProvider(context);
        btnCart.setVisibility(View.VISIBLE);
        btnCart.setText("编辑");
        btnCart.setTag(SUCCESS);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((int)v.getTag()==EDIT){
                    v.setTag(SUCCESS);
                    btnCart.setText("编辑");
                    btnBuy.setVisibility(View.VISIBLE);
                    btnDelet.setVisibility(View.GONE);
                    checkBox.setVisibility(View.VISIBLE);
                    tvShowPrice.setVisibility(View.VISIBLE);
                    //完成
                }else if ((int)v.getTag()==SUCCESS){
                    v.setTag(EDIT);
                    //编辑
                    btnCart.setText("完成");
                    btnBuy.setVisibility(View.GONE);
                    btnDelet.setVisibility(View.VISIBLE);
                    checkBox.setVisibility(View.GONE);
                    tvShowPrice.setVisibility(View.GONE);

                }
                chechData();
            }
        });
        //1.mFl中的页面初始化
        View view = View.inflate(context, R.layout.shopping_cart_layout,null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        tvNOGoods = (TextView) view.findViewById(R.id.tv_no_goods);
        tvShowPrice = (TextView) view.findViewById(R.id.tv_total_price);
        btnBuy = (Button) view.findViewById(R.id.btn_order);
        checkBox = (CheckBox) view.findViewById(R.id.checkbox_all);
        btnDelet = (Button) view.findViewById(btn_delete);

        btnDelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //刪除已经被选中的
                List<ShoppingCar> deletData = new ArrayList<ShoppingCar>();
                if (allData!=null&allData.size()>0) {
                    for (int i = 0; i < allData.size(); i++) {
//把allData中选中的数据删除并刷新数据适配器
                        ShoppingCar car = allData.get(i);
                        if (car.isCheck()) {
                            goodsProvider.deleteData(car);
                            deletData.add(car);
                        }
                    }
                    allData.removeAll(deletData);
                    adapter.notifyDataSetChanged();
                    showTotalPrice();
                    chechData();
                }else {
                    return;
                }
            }
        });
        //2.mFl加载自己的页面
        mFl.removeAllViews();
        mFl.addView(view);
        //3.获取数据-->解析数据
        setData();
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAll();
            }
        });
    }

    private void setData() {
        allData = goodsProvider.getAllData();
        chechData();
        if (allData!=null&&allData.size()>0){
            //绑定数据
            checkedAll();
            tvNOGoods.setVisibility(View.GONE);
            adapter = new MyrecyclerViewAdapter();
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL,false));
            showTotalPrice();
            return;
        }
        tvShowPrice.setText("总价格0");
        tvNOGoods.setVisibility(View.VISIBLE);
        LogUtil.e("政要页面初始化了");
        mTvTitle.setText("政要页面");
    }

    private void chechData() {
        if (allData==null||allData.size()==0){
            //数据为空
            checkBox.setVisibility(View.GONE);
            tvShowPrice.setVisibility(View.GONE);
            tvNOGoods.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 点击勾选全部的按钮
     */
    private void checkAll() {
        if (isCheckAll) {
            isCheckAll = false;
            checkBox.setChecked(false);
            for (int i = 0; i < allData.size(); i++) {
                ShoppingCar shoppingCar = allData.get(i);
                shoppingCar.setCheck(false);
                goodsProvider.updataData(shoppingCar);
            }
        } else {
            isCheckAll = true;
            checkBox.setChecked(true);
            isCheckedData = new ArrayList<>();
            for (int i = 0; i < allData.size(); i++) {
                ShoppingCar shoppingCar = allData.get(i);
//                if (shoppingCar.isCheck()){
//                    isCheckedData.add(shoppingCar);
//                }
                shoppingCar.setCheck(true);
                goodsProvider.updataData(shoppingCar);
//                setData();
            }
        }
        adapter.notifyDataSetChanged();
        showTotalPrice();
    }

    private void hideDelete() {
        //1.文本设置-编辑
        btnCart.setText("编辑");
        //2.状态设置编辑
//        btnCart.setTag(ACTION_EDIT);
        //3.数据设置非全选
//            adapter.checkAll_none(true);
//            adapter.checkAll();
        //4.隐藏按钮显示，显示结算按钮
        btnDelet.setVisibility(View.GONE);
        btnDelet.setVisibility(View.VISIBLE);
        //5.价格重新计算
//        adapter.showTotalPrice();
    }

    private class MyrecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(context,R.layout.shopping_car_goods_item,null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final ShoppingCar car = allData.get(position);
            holder.tv_price.setText("￥"+car.getPrice()*car.getCount());
            holder.tv_describe.setText(car.getName());
            holder.cb.setChecked(car.isCheck());
            holder.mcv.setValue(car.getCount());
            holder.cb.setChecked(car.isCheck());
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //item 的点击事件
                    LogUtil.e("item被点击了"+position);
                    car.setCheck(!car.isCheck());
                    goodsProvider.updataData(car);
                    holder.cb.setChecked(car.isCheck());
                    checkedAll();
                    showTotalPrice();
                }
            });
            Glide.with(context)
                    .load(car.getImgUrl())
                    .into(holder.iv);
            holder.mcv.setOnBtnClickListener(new MyCountView.OnBtnClickListener() {
                @Override
                public void btnAddClick(View v, int count) {
                    //改变总的价格
//                    double price = getPrice();
                    holder.tv_price.setText("￥"+count*car.getPrice());
                    car.setCount(count);
                    goodsProvider.updataData(car);
                    showTotalPrice();
                }

                @Override
                public void btnBusClick(View v, int count) {
//                    double price = getPrice();
                    holder.tv_price.setText("￥"+count*car.getPrice());
                    car.setCount(count);
                    goodsProvider.updataData(car);
                    //更改总价格
                    showTotalPrice();
                }
            });
        }

        @Override
        public int getItemCount() {
            return allData.size();
        }
    }

    private void showTotalPrice() {
        double totalPrice = getPrice();
        tvShowPrice.setText("总共"+totalPrice);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private CheckBox cb;
        private ImageView iv;
        private TextView tv_describe,tv_price;
        private MyCountView mcv;
        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.cb = (CheckBox) itemView.findViewById(R.id.checkbox);
            this.iv = (ImageView) itemView.findViewById(R.id.iv_icon);
            this.mcv = (MyCountView) itemView.findViewById(R.id.numberAddSeubView);
            this.tv_describe = (TextView) itemView.findViewById(R.id.tv_name);
            this.tv_price = (TextView) itemView.findViewById(R.id.tv_price);

        }
    }

    private double getPrice() {
        double price = 0;
        List<ShoppingCar> allDatas = goodsProvider.getAllData();
        for (int i = 0; i < allDatas.size(); i++) {
            ShoppingCar shoppingCar = allDatas.get(i);
            if (shoppingCar.isCheck()){
                //当前项被选中了
                int count = shoppingCar.getCount();
                price += count*shoppingCar.getPrice();
            }
        }
        return price;
    }

    /**
     * 检测是否是全部都被勾选了
     */
    private void checkedAll(){
        if (allData!=null&&allData.size()>0){
            int count = 0;
            for (int i = 0; i < allData.size(); i++) {
                ShoppingCar shoppingCar = allData.get(i);
                if (!shoppingCar.isCheck()){
                    checkBox.setChecked(false);
                    isCheckAll = false;
                }else {
                    count ++;
                }
            }
            if (count==allData.size()){
                checkBox.setChecked(true);
                isCheckAll = true;
            }
        }else {
            checkBox.setChecked(false);
        }
    }

}
