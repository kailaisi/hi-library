package com.kailaisi.common.ui.component;

import android.os.Bundle;
import android.widget.BaseAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kailaisi.common.HiBaseActionInterface;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 描述：
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-05-15:15:58
 */
public class HiBaseActivity<T> extends AppCompatActivity implements HiBaseActionInterface {
    private T data;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取到父类的类型
        Type superclass = this.getClass().getGenericSuperclass();
        //获取到类是否是范型参数
        if (superclass instanceof ParameterizedType){
            Type[] types = ((ParameterizedType) superclass).getActualTypeArguments();
            if (types!=null && types[0] instanceof BaseAdapter){
                try {
                    data = (T) types[0].getClass().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
