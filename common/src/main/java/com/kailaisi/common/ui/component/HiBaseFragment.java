package com.kailaisi.common.ui.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 描述：
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-05-15:15:58
 */
public abstract class HiBaseFragment extends Fragment {
    protected View layoutView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(getLayoutId(), container, false);
        return layoutView;
    }

    protected abstract int getLayoutId();

    public boolean isAlive(){
        if (isRemoving() || isDetached()||getActivity()==null){
            return false;
        }
        return true;
    }
}
