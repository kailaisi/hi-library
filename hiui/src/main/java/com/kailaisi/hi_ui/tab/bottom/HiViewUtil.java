package com.kailaisi.hi_ui.tab.bottom;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import java.util.ArrayDeque;

/**
 * 描述：从ViewGroup中找到指定类型的View
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-05-15:15:03
 */
public class HiViewUtil {
    public static <T> T findTypeView(@Nullable ViewGroup group, Class<T> cls) {
        if (group == null) {
            return null;
        }
        ArrayDeque<View> deque = new ArrayDeque<>();
        deque.add(group);
        while (!deque.isEmpty()) {
            View view = deque.removeFirst();
            if (cls.isInstance(view)) {
                return cls.cast(view);
            } else if (view instanceof ViewGroup) {
                Integer count = ((ViewGroup) view).getChildCount();
                for (int i = 0; i < count; i++) {
                    deque.add(((ViewGroup) view).getChildAt(i));
                }
            }
        }
        return null;
    }
}
