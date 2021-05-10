package com.kailaisi.library.log;

import androidx.annotation.NonNull;

/**
 * 描述：管理类
 * <p/>作者：wu
 * <br/>创建时间：2021-05-10:22:38
 */
public class HiLogManager {
    private HiLogConfig config;
    private static HiLogManager instance;

    private HiLogManager(HiLogConfig config){
        this.config=config;
    }

    public static HiLogManager getInstance(){
        return instance;
    }

    public static void init(@NonNull HiLogConfig config){
        instance=new HiLogManager(config);
    }
    public HiLogConfig getConfig(){
        return config;
    }
}
