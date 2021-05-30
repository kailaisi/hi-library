package com.kailaisi.library.log;

/**
 * 描述：
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-05-11:21:59
 */
public class HiThreadFormatter implements HiLogFormatter<Thread>{
    @Override
    public String format(Thread data) {
        return "Thread:"+data.getName();
    }
}
