package com.kailaisi.library.log;

import android.util.Log;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 描述：
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-05-10:22:22
 */
public class HiLogType {
    @IntDef({V,D,E,I,A,W})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TYPE{}

    public static final int V= Log.VERBOSE;
    public static final int E=Log.ERROR;
    public static final int D=Log.DEBUG;
    public static final int I=Log.INFO;
    public static final int A=Log.ASSERT;
    public static final int W=Log.WARN;
}
