package com.kailaisi.hi_ui.tab.bottom;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.kailaisi.library.util.AppGlobals;

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


    public static boolean isActivityDestroyed(Context context) {
        Activity activity = findActivity(context);
        if (activity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return activity.isDestroyed() || activity.isFinishing();
            }

            return activity.isFinishing();
        }

        return true;
    }

    public static Activity findActivity(Context context) {
        //怎么判断context 是不是activity 类型的
        if (context instanceof Activity) return (Activity) context;
        else if (context instanceof ContextWrapper) {
            return findActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    public static boolean lightMode() {
        int mode = AppGlobals.INSTANCE.get().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return mode == Configuration.UI_MODE_NIGHT_NO;
    }

}
