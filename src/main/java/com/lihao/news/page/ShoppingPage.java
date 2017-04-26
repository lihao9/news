package com.lihao.news.page;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.lihao.news.R;
import com.lihao.news.bean.HotGoods;
import com.lihao.news.bean.ShoppingCar;
import com.lihao.news.tools.CacheUtil;
import com.lihao.news.tools.Constant;
import com.lihao.news.tools.GoodsProvider;
import com.lihao.news.tools.LogUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hbm on 2017/4/13.
 */

public class ShoppingPage extends BasePage {
    private static final int SUCCESS = 1;
    private RecyclerView recyclerview;
    private MaterialRefreshLayout refresh;
    private ProgressBar pb;
    private GoodsProvider goodsProvider;
    //总共有多少页
    private int totalPage;

    //一页显示多少条数据
    private int pageSize = 10;

    //当前请求的页面
    private int currentPage = 1;

    //当前请求页面的地址
    private String url;

    //商品的实体列表
    private List<HotGoods.GoodsBean> goodss;
    private boolean isLoadMore;
    private MyRecycleAdapter adapter;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    String data = (String) msg.obj;
                    processData(data);
                    break;
            }
        }
    };

    public ShoppingPage(Context context){
        //构造方法调用父类的
        super(context);

    }

    public void initData(){
        goodsProvider = new GoodsProvider(context);
        getDataUrl();
        LogUtil.e("购物页面初始化了");
        mTvTitle.setText("热卖商品");
//        btnCart.setVisibility(View.VISIBLE);
        View view = View.inflate(context, R.layout.shopping_page_layout,null);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        pb = (ProgressBar) view.findViewById(R.id.progress);
        refresh = (MaterialRefreshLayout) view.findViewById(R.id.refreshLayout);
        refresh.setMaterialRefreshListener(new MyMaterialRefreshListener());
        if (mFl!=null){
            mFl.removeAllViews();
            mFl.addView(view);
        }
        //1.获取热卖商品数据
        //2.处理热卖商品--解析，存储
        //3.绑定数据
//        String data = CacheUtil.getStringFromSp(context,"Hot","");
//        if (data!=null&&!data.equals("")){
//            processData(data);
//            LogUtil.e("从Sp中获取数据成功");
//            return;
//        }
        getDataFromNet();
    }

    /**
     * 获取热卖商品的地址
     */
    private void getDataUrl() {
        url = Constant.WARES_HOT_URL+pageSize+"&curPage="+currentPage;
        LogUtil.i("当前请求页面的地址"+url);
    }

    private void getDataFromNet() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().
                get().
                url(url).build();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e("获取热卖商品失败"+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取数据成功--
                LogUtil.e("获取热卖商品成功");
                String data = response.body().string();
                CacheUtil.putStringToSp(context,"Hot",data);
                Message msg = new Message();
                msg.what = SUCCESS;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        });

    }

    //解析处理数据
    private void processData(String data) {
        HotGoods hotGoods = parsedData(data);
        if (hotGoods!=null){
            if (isLoadMore){
                //处理加载更多
                isLoadMore = false;
                goodss.addAll(hotGoods.getList());
                adapter.notifyDataSetChanged();
                refresh.finishRefreshLoadMore();
            }else {
                goodss = hotGoods.getList();
                currentPage = hotGoods.getCurrentPage();
                totalPage = hotGoods.getTotalCount();
                bindData();
                LogUtil.e("currentPage:" + currentPage + "---" + "totalpage:" + totalPage);
            }
            //显示，绑定数据
            pb.setVisibility(View.GONE);

        }
    }

    /**
     * 绑定数据
     */
    private void bindData() {
        //设置适配器
        adapter = new MyRecycleAdapter();
        recyclerview.setAdapter(adapter);
        //设置模式
        recyclerview.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
    }

    private HotGoods parsedData(String data) {
        return new Gson().fromJson(data, HotGoods.class);
    }

    private class MyMaterialRefreshListener extends MaterialRefreshListener {
        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
            //下拉更新

        }

        @Override
        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
            //加载更多
            if(currentPage == totalPage){
                Toast.makeText(context, "已经没有数据了", Toast.LENGTH_SHORT).show();
                refresh.finishRefreshLoadMore();
            }else {
                isLoadMore = true;
                currentPage += 1;
                url = Constant.WARES_HOT_URL+pageSize+"&curPage="+currentPage;
                LogUtil.e("loadMore的地址"+url);
                getDataFromNet();
            }
        }
    }

    private class MyRecycleAdapter extends RecyclerView.Adapter<ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(context,R.layout.goods_item,null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final HotGoods.GoodsBean goods = goodss.get(position);
            holder.tv_describe.setText(goods.getName());
            holder.tv_price.setText("￥"+goods.getPrice());
            Glide.with(context)
                    .load(goods.getImgUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.news_pic_default)//真正加载的默认图片
                    .error(R.drawable.news_pic_default).//失败的默认图片
                    into(holder.image);
            LogUtil.e("图片地址"+goods.getImgUrl());
            holder.btn_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.e("buy点击了"+position);

                    //1.把商品转化为购物车中的商品类型
                    ShoppingCar good = goodsProvider.conversion(goods);
                    //2.添加到集合
                    goodsProvider.addData(good);
                    //3.保存到Sp中
                    goodsProvider.commit();
                }
            });
        }


        @Override
        public int getItemCount() {
            return goodss.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView tv_describe;
        private TextView tv_price;
        private Button btn_buy;


        public ViewHolder(View view){
            super(view);
            this.image = (ImageView) view.findViewById(R.id.goods_image);
            this.btn_buy = (Button) view.findViewById(R.id.btn_buy);
            this.tv_price = (TextView) view.findViewById(R.id.tv_price);
            this.tv_describe = (TextView) view.findViewById(R.id.tv_describe);
        }
    }
}
