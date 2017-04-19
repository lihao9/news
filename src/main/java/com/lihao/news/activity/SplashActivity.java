package com.lihao.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.lihao.news.R;

public class SplashActivity extends Activity {
    private SharedPreferences sp;
    private ImageView mIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        sp = getSharedPreferences("config",MODE_PRIVATE);

        mIv = (ImageView) findViewById(R.id.splash_iv);

        AlphaAnimation al = new AlphaAnimation(0.3f,1.0f);

        Animation sa =  AnimationUtils.loadAnimation(SplashActivity.this,R.anim.scale);

        Animation ra = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF ,0.5f);

        //插入器
        AnimationSet set = new AnimationSet(false);
        set.addAnimation(al);
        set.addAnimation(sa);
        set.addAnimation(ra);
        set.setDuration(2000);
        mIv.startAnimation(set);
        set.setAnimationListener(new MyAnimationListenert());
    }


    private class MyAnimationListenert implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
//            mVp.setAdapter(new SplashPageradapter());
            boolean isEnter = sp.getBoolean("isEnter",false);
            Intent intent = new Intent();
            if (isEnter){
                //已经进入过主界面了
                intent.setClass(SplashActivity.this,MainActivity.class);
            }else {
                intent.setClass(SplashActivity.this,GuideActivity.class);
            }
            startActivity(intent);
            finish();
        }

        /**
         * 重复播放动画时调用
         * @param animation
         */
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }


}
