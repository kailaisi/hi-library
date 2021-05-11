package com.kailaisi.library.log;

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2021-05-11:23:02
 */
public class HiStackTraceUtil {
    private static StackTraceElement[] cropTrace(StackTraceElement[] elements,int maxDepth){
        int realDepth=elements.length;
        if (realDepth>0){
            realDepth=Math.min(maxDepth,realDepth);
        }
        StackTraceElement[] real=new StackTraceElement[realDepth];
        System.arraycopy(elements,0,real,0,realDepth);
        return real;
    }
}
