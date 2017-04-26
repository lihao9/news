package com.lihao.news.menupage;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lihao.news.R;
import com.lihao.news.activity.MainActivity;
import com.lihao.news.activity.ShowPictureActivity;
import com.lihao.news.bean.ImageData;
import com.lihao.news.bean.NewsData;
import com.lihao.news.tools.Constant;
import com.lihao.news.tools.LogUtil;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hbm on 2017/4/16.
 * 图片页面
 */

public class MenuImagePage extends MenuBasePage{
    private NewsData.DataBean dataBean;
    private ListView mListView;
    private GridView mGridView;
    private String mImageUrl;
    private ImageData mImageData;
    private List<ImageData.DataBean.NewsBean> mImageNews;
    private MyListAndGridAdapter adapter;
    private OkHttpClient client;

    public MenuImagePage(Context context, NewsData.DataBean dataBean) {
        super(context);
        this.dataBean = dataBean;
    }


    @Override
    public View initView() {
//        TextView tv = new TextView(context);
//        tv.setText("菜单--图片页面");
//        tv.setTextColor(Color.RED);

        View view = View.inflate(context,R.layout.mene_image_detail ,null);
        mListView = (ListView) view.findViewById(R.id.image_listview);
        mGridView = (GridView) view.findViewById(R.id.image_gridview);
        return view;
    }
    //当前显示的模式是listView。
    private boolean isListView = true;
    public void swithImageShowDOme(ImageButton imageButton){
        if(mImageNews==null||mImageNews.size()<=0){
            return;
        }
        if (isListView){
            isListView = false;
            mListView.setVisibility(View.GONE);
            mGridView.setVisibility(View.VISIBLE);
            mGridView.setAdapter(new MyListAndGridAdapter());
            imageButton.setImageResource(R.drawable.icon_pic_list_type);
        }else {
            isListView = true;
            mListView.setVisibility(View.VISIBLE);
            mListView.setAdapter(new MyListAndGridAdapter());
            mGridView.setVisibility(View.GONE);
            imageButton.setImageResource(R.drawable.icon_pic_grid_type);
        }

    }

    public void initData(){
        LogUtil.e("图片页面初始化了");
        mImageUrl = Constant.BASE_URL + dataBean.getUrl();
        processData();
    }

    private void processData() {
        LogUtil.e("图组Url:"+mImageUrl);
        client = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.url(mImageUrl).
                build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e("失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtil.e("成功");
                String string = response.body().string();
                LogUtil.e("数据"+string);
                if(string!=null&&string!=""){
                    //有数据--解析到对应的bean中
                    mImageData = praseGson(string);

                    if (mImageData!=null){
                        //设置数据给视图
                        mImageNews =  mImageData.getData().getNews();
                        if (mImageNews!=null&&mImageNews.size()>0){
                            setData();
                        }
                    }
                }
            }
        });
    }


    private void setData() {
        MainActivity a = (MainActivity) context;
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(isListView){
                    mListView.setVisibility(View.VISIBLE);
                    mListView.setAdapter(new MyListAndGridAdapter());
                    mGridView.setVisibility(View.GONE);
                }else {
                    mGridView.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                    mGridView.setAdapter(new MyListAndGridAdapter());
                }
            }
        });
    }

    private ImageData praseGson(String string) {
        return  new Gson().fromJson(string , ImageData.class);
    }

    private class MyListAndGridAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mImageNews.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHoulder houlder;
            if (convertView==null){
                convertView = View.inflate(context,R.layout.image_item,null);
                houlder = new ViewHoulder();
                houlder.image = (ImageView) convertView.findViewById(R.id.icon_image);
                houlder.text = (TextView) convertView.findViewById(R.id.item_image_text);
                convertView.setTag(houlder);
            }else {
                houlder = (ViewHoulder) convertView.getTag();
            }
            houlder.text.setText(mImageNews.get(position).getTitle());
            String imageUrl = Constant.BASE_URL+mImageNews.get(position).getSmallimage();
            houlder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    LogUtil.e("条目被点击"+position);
                    //图片的点击事件，进入显示图片的activity
                    Intent intent = new Intent(context,ShowPictureActivity.class);
                    intent.putExtra("iamgeUrl",mImageNews.get(position).getSmallimage());
                    context.startActivity(intent);
                }
            });
            loadImage(houlder,imageUrl);
            return convertView;
        }
    }

    private void loadImage(ViewHoulder houlder, String imageUrl) {
        Picasso.with(context).
                load(imageUrl).
                error(R.drawable.news_pic_default).
                placeholder(R.drawable.news_pic_default).
                into(houlder.image);

    }

    class ViewHoulder{
        private ImageView image;
        private TextView text;
    }

}
