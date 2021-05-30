package com.kailaisi.library.log;

import androidx.annotation.NonNull;

/**
 * 描述：打印器
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-05-11:21:54
 */
public interface HiLogPrinter {
    void print(@NonNull HiLogConfig config,int level,String tag,@NonNull String msg);
}
