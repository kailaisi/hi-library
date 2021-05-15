package com.kailaisi.hiapp.logic;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentManager;

import com.kailaisi.common.tab.HiFragmentTabView;
import com.kailaisi.common.tab.HiTabViewAdapter;
import com.kailaisi.hi_ui.tab.bottom.HiBottomLayout;
import com.kailaisi.hi_ui.tab.bottom.HiTabBottomInfo;
import com.kailaisi.hi_ui.tab.common.IHiTabLayout;
import com.kailaisi.hiapp.R;
import com.kailaisi.hiapp.fragment.CategoryFragment;
import com.kailaisi.hiapp.fragment.FavoriteFragment;
import com.kailaisi.hiapp.fragment.HomePageFragment;
import com.kailaisi.hiapp.fragment.ProfileFragment;
import com.kailaisi.hiapp.fragment.RecommandFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：抽取MainActivity的逻辑
 * <p/>作者：wu
 * <br/>创建时间：2021-05-15:20:28
 */
public class MainActivityLogic {

    private static final String SAVE_INSTANCE_STATE = "SAVE_INSTANCE_STATE";
    private HiFragmentTabView fragmentTabView;
    private HiBottomLayout hiBottomLayout;
    private List<HiTabBottomInfo<?>> infoList;
    private ActivityProvider activityProvider;
    private int currentItemIndex = 0;

    public MainActivityLogic(ActivityProvider activityProvider, Bundle savedInstanceState) {
        this.activityProvider = activityProvider;
        //fix 不保留活动导致的Fragment重叠问题
        if (savedInstanceState != null) {
            currentItemIndex = savedInstanceState.getInt(SAVE_INSTANCE_STATE);
        }
        initBottom();
    }

    public void initBottom() {
        hiBottomLayout = activityProvider.findViewById(R.id.tab_bottom_layout);

        int defaultColor = activityProvider.getResources().getColor(R.color.tabBottomDefaultColor);
        int tintColor = activityProvider.getResources().getColor(R.color.tabBottomTintColor);
        HiTabBottomInfo home = new HiTabBottomInfo<Integer>("首页", "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_home), null, defaultColor, tintColor
        );
        home.fragment = HomePageFragment.class;
        HiTabBottomInfo favorite = new HiTabBottomInfo<Integer>("收藏", "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_favorite), null, defaultColor, tintColor
        );
        favorite.fragment = FavoriteFragment.class;
        HiTabBottomInfo category = new HiTabBottomInfo<Integer>("分类", "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_category), null, defaultColor, tintColor
        );
        category.fragment = CategoryFragment.class;
        HiTabBottomInfo recommand = new HiTabBottomInfo<Integer>("推荐", "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_recommend), null, defaultColor, tintColor
        );
        recommand.fragment = RecommandFragment.class;
        HiTabBottomInfo profile = new HiTabBottomInfo<Integer>("我的", "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_profile), null, defaultColor, tintColor
        );
        profile.fragment = ProfileFragment.class;
        infoList = new ArrayList<>();
        infoList.add(home);
        infoList.add(favorite);
        infoList.add(category);
        infoList.add(recommand);
        infoList.add(profile);
        //设置底部的tab
        hiBottomLayout.inflateInfo(infoList);
        //将底部的tab和fragment进行了一层绑定
        initFragmentTabView();
        hiBottomLayout.addTabSelectedChangedListener(new IHiTabLayout.OnTabSelectedListener<HiTabBottomInfo<?>>() {
            @Override
            public void onTabSelectedListener(int index, @Nullable HiTabBottomInfo<?> preInfo, @NonNull HiTabBottomInfo<?> nextInfo) {
                //tab的切换，引起fragment的切换。其实二者是完全解耦合的
                fragmentTabView.setCurrentItem(index);
                currentItemIndex = index;
            }
        });
        hiBottomLayout.defaultSelected(infoList.get(currentItemIndex));
    }

    private void initFragmentTabView() {
        HiTabViewAdapter adapter = new HiTabViewAdapter(activityProvider.getSupportFragmentManager(), infoList);
        fragmentTabView = activityProvider.findViewById(R.id.fragment_tab_view);
        fragmentTabView.setAdapter(adapter);
    }

    public void onSaveInstanceState(@NotNull Bundle outState) {
        outState.putInt(SAVE_INSTANCE_STATE, currentItemIndex);
    }

    public interface ActivityProvider {
        <T extends View> T findViewById(@IdRes int id);

        Resources getResources();

        FragmentManager getSupportFragmentManager();

        String getString(@StringRes int resId);
    }
}
