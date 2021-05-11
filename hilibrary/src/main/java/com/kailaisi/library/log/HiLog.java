package com.kailaisi.library.log;

import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * 描述：
 * 1.打印堆栈信息
 * 2.打印File文件
 * 3.模拟控制台输出
 * <p/>作者：kailaisii
 * <br/>创建时间：2021-05-10:22:21
 */
public class HiLog {
    public static void v(Object... contents) {
        log(HiLogType.V, contents);
    }

    public static void vt(String tag, Object... contents) {
        log(HiLogType.V, tag, contents);
    }

    public static void d(Object... contents) {
        log(HiLogType.D, contents);
    }

    public static void dt(String tag, Object... contents) {
        log(HiLogType.D, tag, contents);
    }

    public static void e(Object... contents) {
        log(HiLogType.E, contents);
    }

    public static void et(String tag, Object... contents) {
        log(HiLogType.E, tag, contents);
    }

    public static void w(Object... contents) {
        log(HiLogType.W, contents);
    }

    public static void wt(String tag, Object... contents) {
        log(HiLogType.W, tag, contents);
    }

    public static void i(Object... contents) {
        log(HiLogType.I, contents);
    }

    public static void it(String tag, Object... contents) {
        log(HiLogType.I, tag, contents);
    }

    public static void a(Object... contents) {
        log(HiLogType.A, contents);
    }

    public static void at(String tag, Object... contents) {
        log(HiLogType.A, tag, contents);
    }


    public static void log(@HiLogType.TYPE int type, Object... contents) {
        log(type,HiLogManager.getInstance().getConfig().getGlobalTag(),contents);
    }

    public static void log(@HiLogType.TYPE int type,@NonNull String tag,Object... contents){
        log(HiLogManager.getInstance().getConfig(),type,tag,contents);
    }

    public static void log(@NonNull HiLogConfig config, @HiLogType.TYPE int type, @NotNull String tag, Object... contents) {
        if (!config.enable()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (config.includeThread()){
            String threadInfo = HiLogConfig.hiThreadFormatter.format(Thread.currentThread());
            sb.append(threadInfo).append("\n");
        }
        if (config.stackTraceDepth()>0){
            String stackTrace = HiLogConfig.hiStackTraceFormatter.format(new Throwable().getStackTrace());
            sb.append(stackTrace).append("\n");
        }
        String body=parseBody(contents,config);
        sb.append(body);
        List<HiLogPrinter> printers = config.printers() != null ? Arrays.asList(config.printers()) : HiLogManager.getInstance().getPrinters();
        if (printers==null){
            return;
        }
        //用各个打印器去打印log
        for (HiLogPrinter printer : printers) {
            printer.print(config,type,tag,sb.toString());
        }
    }

    private static String parseBody(@NonNull Object[] contents, HiLogConfig config) {
        if (config.injectParser()!=null){
            return config.injectParser().toJson(contents);
        }
        StringBuilder sb = new StringBuilder();
        for (Object o : contents) {
            sb.append(o.toString()).append(";");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
