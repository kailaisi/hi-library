package com.kailaisi.library.log;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 描述：打印的log信息
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-05-12:20:49
 */
public class HiLogMo {
    private static SimpleDateFormat sdf=new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.CHINA);

    public long time;
    public int level;
    public String tag;
    public String log;

    public HiLogMo(long time, int level, String tag, String log) {
        this.time = time;
        this.level = level;
        this.tag = tag;
        this.log = log;
    }

    public String flattenedLog(){
        return getFlattened()+"\n"+log;
    }

    @NotNull
    public String getFlattened(){
        return format(time) + "|"+level+"|"+tag+"|:";
    }

    private String format(long time) {
        return sdf.format(time);
    }
}
