package com.kailaisi.hiapp.demo.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.kailaisi.hi_ui.refresh.HiOverView;
import com.kailaisi.hiapp.R;

/**
 * 描述：
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-05-23:22:39
 */
class HiLottieOverView extends HiOverView {

    private LottieAnimationView view;

    public HiLottieOverView(@NonNull Context context) {
        super(context);
    }

    public HiLottieOverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HiLottieOverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.hi_refresh_lottie_overview, this, true);
        view = findViewById(R.id.animation);
        view.setAnimation("loading_wave.json");
    }

    @Override
    protected void onScroll(int scrollY, int pullRefreshHeight) {

    }

    @Override
    protected void onVisible() {

    }

    @Override
    public void onOver() {

    }

    @Override
    public void onRefresh() {
        view.setSpeed(2);
        view.playAnimation();
    }

    @Override
    public void onFinish() {
        view.setProgress(0f);
        view.cancelAnimation();
    }
}
