package com.lihao.news.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lihao.news.R;

/**
 * Created by hbm on 2017/4/25.
 * 作者：李浩
 * 时间：2017/4/25
 * 类的作用：xxxxxx
 */

public class MyCountView extends LinearLayout implements View.OnClickListener {
    private Button btnSub,btnAdd;
    private TextView tvCount;

    private int value = 1;
    private int minValue = 1;
    private int maxValue = 10;

    public int getValue() {
        String valueStr =  tvCount.getText().toString().trim();//文本的内容
        if(!TextUtils.isEmpty(valueStr)){
            value = Integer.valueOf(valueStr);
        }
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        tvCount.setText(value+"");
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public MyCountView(Context context) {
        this(context,null);
    }

    public MyCountView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyCountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = View.inflate(context, R.layout.view_layout,this);
        btnSub = (Button) view.findViewById(R.id.btn_bus);
        btnAdd = (Button) view.findViewById(R.id.btn_add);
        tvCount = (TextView) view.findViewById(R.id.tv_count);
        btnSub.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        getValue();
    }

    /**
     * 增加或者减少按钮被点击时调用
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                addNumber();
                if (listener!=null){
                    listener.btnAddClick(v,value);
                }
                break;
            case R.id.btn_bus:
                busNumber();
                if (listener!=null){
                    listener.btnBusClick(v,value);
                }
                break;
        }
    }

    private void busNumber() {
        if (value>minValue){
            value -= 1;
        }
        setValue(value);
    }

    private void addNumber() {
        if(value < maxValue){
            value += 1;
        }
        setValue(value);
    }

    private OnBtnClickListener listener;

    /**
     * 用于控制
     * @param listener
     */
    public void setOnBtnClickListener(OnBtnClickListener listener){
        this.listener = listener;
    }

    public interface OnBtnClickListener{
        public void btnAddClick(View v,int count);
        public void btnBusClick(View v,int count);
    }
}
