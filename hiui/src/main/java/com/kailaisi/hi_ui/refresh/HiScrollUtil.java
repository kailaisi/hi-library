package com.kailaisi.hi_ui.refresh;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2021-05-18:22:20
 */
public class HiScrollUtil {
    /**
     * 获取可滚动的列表
     *
     * @param group
     * @return
     */
    public static View findScrollableChild(@NotNull ViewGroup group) {
        //第0个元素一般是添加的刷新头
        View child = group.getChildAt(1);
        if (child instanceof RecyclerView || child instanceof AdapterView) {
            return child;
        }
        if (child instanceof ViewGroup) {
            View temp = ((ViewGroup) child).getChildAt(0);
            if (temp instanceof RecyclerView || temp instanceof AdapterView) {
                child = temp;
            }
        }
        return child;
    }

    /**
     * 判断是否发生了滚动
     *
     * @param child
     * @return
     */
    public static boolean childScrolled(@NotNull View child) {
        if (child instanceof AdapterView) {
            if (((AdapterView) child).getFirstVisiblePosition() != 0
                    || ((AdapterView) child).getFirstVisiblePosition() == 0 && ((AdapterView) child).getChildAt(0) != null
                    && ((AdapterView) child).getChildAt(0).getTop() < 0) {
                return true;
            }
        } else if (child.getScaleY() > 0) {
            return true;
        }
        if (child instanceof RecyclerView) {
            View view = ((RecyclerView) child).getChildAt(0);
            int position = ((RecyclerView) child).getChildAdapterPosition(view);
            return position != 0 || view.getTop() != 0;
        }
        return false;
    }
}
