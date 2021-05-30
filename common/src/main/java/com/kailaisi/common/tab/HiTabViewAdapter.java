package com.kailaisi.common.tab;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kailaisi.hi_ui.tab.bottom.HiTabBottomInfo;

import java.util.List;

/**
 * 描述：管理Fragment
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-05-15:16:32
 */
public class HiTabViewAdapter {
    private FragmentManager mFragmentManager;
    private Fragment mCurrentFragment;
    private List<HiTabBottomInfo<?>> infos;

    public HiTabViewAdapter(FragmentManager fragmentManager, List<HiTabBottomInfo<?>> infos) {
        this.mFragmentManager = fragmentManager;
        this.infos = infos;
    }


    public void instantiateItem(View container, int position) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mCurrentFragment != null) {
            transaction.hide(mCurrentFragment);
        }
        String name = container.getId() + ":" + position;
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            fragment = getItem(position);
            if (!fragment.isAdded()) {
                transaction.add(container.getId(), fragment, name);
            }
        }
        mCurrentFragment = fragment;
        transaction.commitAllowingStateLoss();
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    private Fragment getItem(int position) {
        try {
            return infos.get(position).fragment.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getCount() {
        return infos.size();
    }
}
