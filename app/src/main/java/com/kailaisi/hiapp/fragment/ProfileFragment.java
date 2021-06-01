package com.kailaisi.hiapp.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kailaisi.common.ui.component.HiBaseFragment;
import com.kailaisi.hiapp.R;

/**
 * 描述：
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-05-15:17:50
 */
public class ProfileFragment extends HiBaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
