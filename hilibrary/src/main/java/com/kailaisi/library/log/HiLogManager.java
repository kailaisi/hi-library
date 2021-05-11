package com.kailaisi.library.log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 描述：管理类
 * <p/>作者：wu
 * <br/>创建时间：2021-05-10:22:38
 */
public class HiLogManager {
    private HiLogConfig config;
    private static HiLogManager instance;

    private List<HiLogPrinter> printers = new ArrayList<>();


    public HiLogManager(HiLogConfig config, HiLogPrinter[] printers) {
        this.config = config;
        this.printers.addAll(Arrays.asList(printers));
    }

    public static HiLogManager getInstance() {
        return instance;
    }

    //可变参数，更加灵活，用户可以设置，也可以不设置
    public static void init(@NonNull HiLogConfig config, HiLogPrinter... printers) {
        instance = new HiLogManager(config, printers);
    }

    public HiLogConfig getConfig() {
        return config;
    }

    public List<HiLogPrinter> getPrinters() {
        return printers;
    }

    public void addPrinter(HiLogPrinter printer){
        printers.add(printer);
    }

    public void removePrinter(HiLogPrinter printer){
        printers.remove(printer);
    }
}

