package com.kailaisi.library.log;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

/**
 * 描述：打印面板生成器
 * 作者：wujinxiang
 * 创建时间：2021/5/13 2:52 PM
 */
public class HiViewPrinterProvider {
    public static final String TAG_FLOATING_VIEW = "TAG_FLOATING_VIEW";
    public static final String TAG_LOG_VIEW = "TAG_LOG_VIEW";

    private FrameLayout rootView;
    private View floatingView;
    private boolean isOpen;
    private FrameLayout logView;
    private RecyclerView recyclerView;

    public HiViewPrinterProvider(FrameLayout rootView, RecyclerView recyclerView) {
        this.rootView = rootView;
        this.recyclerView = recyclerView;
    }

    /**
     *显示悬浮按钮
     */
    public void showFloatingView() {
        if (rootView.findViewWithTag(TAG_FLOATING_VIEW) != null) {
            return;
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.END;
        View floatingView = genFloatingView();
        floatingView.setTag(TAG_FLOATING_VIEW);
        floatingView.setBackgroundColor(Color.BLACK);
        floatingView.setAlpha(0.8f);
        //距离底部位置s
        params.bottomMargin=HiDisplayUtils.dip2px(recyclerView.getContext(),100);
        rootView.addView(genFloatingView(),params);
    }

    public void closeFloatingView(){
        rootView.removeView(genFloatingView());
    }

    private View genFloatingView() {
        if (floatingView != null) {
            return floatingView;
        }
        TextView view = new TextView(rootView.getContext());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpen) {
                    showLogView();
                }
            }
        });
        view.setText("HiLog");
        return floatingView = view;
    }

    /**
     * 显示LogVIew
     */
    private void showLogView() {
        if (rootView.findViewWithTag(TAG_LOG_VIEW) != null) {
            return;
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, HiDisplayUtils.dip2px(rootView.getContext(), 160));
        View logView = genLogView();
        params.gravity=Gravity.BOTTOM;
        logView.setTag(TAG_LOG_VIEW);
        rootView.addView(logView,params);
        isOpen=true;
    }

    private View genLogView() {
        if (logView != null) {
            return logView;
        }
        FrameLayout logView = new FrameLayout(rootView.getContext());
        logView.setBackgroundColor(Color.GRAY);
        logView.addView(recyclerView);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.END;
        //关闭按钮
        TextView textView = new TextView(rootView.getContext());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeLogView();
            }
        });
        textView.setText("Close");
        logView.addView(textView, params);
        this.logView = logView;
        return logView;
    }

    /**
     * 关闭
     */
    private void closeLogView() {
        isOpen = false;
        rootView.removeView(genLogView());
    }

}
