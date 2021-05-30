package com.kailaisi.library.log;

import android.util.Log;

import androidx.annotation.NonNull;

import static com.kailaisi.library.log.HiLogConfig.MAX_LEN;

/**
 * 描述：控制台打印器
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-05-11:22:17
 */
public class HiConsolePrinter implements HiLogPrinter {
    @Override
    public void print(@NonNull HiLogConfig config, int level, String tag, @NonNull String msg) {
        int len = msg.length();
        //打印行数
        int row = len / MAX_LEN;
        if (row > 0) {
            int index = 0;
            for (int i = 0; i < row; i++) {
                Log.println(level, tag, msg.substring(index, index + MAX_LEN));
                index += MAX_LEN;
            }
            //将剩余部分打印出来
            if (index != len) {
                Log.println(level, tag, msg.substring(index));
            }
        }else{
            Log.println(level,tag,msg);
        }
    }
}
