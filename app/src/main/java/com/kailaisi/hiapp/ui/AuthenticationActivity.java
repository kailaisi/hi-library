package com.kailaisi.hiapp.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kailaisi.hiapp.R;
import com.kailaisi.hiapp.route.RouterFlag;

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2021-06-06:13:42
 */
@Route(path = "/profile/authentication", extras = RouterFlag.FLAG_AUTHENTICATION)
public class AuthenticationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
    }
}