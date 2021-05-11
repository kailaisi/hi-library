package com.kailaisi.library.log;


/**
 * 描述：配置类
 * <p/>作者：wu
 * <br/>创建时间：2021-05-10:22:35
 */
public class HiLogConfig {
    static int MAX_LEN = 512;

    static HiStackTraceFormatter hiStackTraceFormatter = new HiStackTraceFormatter();
    static HiThreadFormatter hiThreadFormatter = new HiThreadFormatter();

    public JsonParser injectParser(){
        return null;
    }

    public String getGlobalTag() {
        return "HiLog";
    }

    public boolean enable() {
        return true;
    }

    public boolean includeThread(){
        return false;
    }
    public int stackTraceDepth(){
        return 5;
    }
    public HiLogPrinter[] printers(){
        return null;
    }
    /**
     * 序列化接口，交给用户去决定具体使用哪个，Json、Gson、or other
     */
    public interface JsonParser {
        String toJson(Object src);
    }
}
