package com.kailaisi.library.log;

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2021-05-11:23:02
 */
public class HiStackTraceUtil {

    /**
     * 获取忽略包名的堆栈信息
     */
    public static StackTraceElement[] getCopedRealStackTrack(StackTraceElement[] stackTrace,String ignorePackage,int maxDepth){
        return cropTrace(getRealStackTrack(stackTrace, ignorePackage),maxDepth);
    }

    private static StackTraceElement[] cropTrace(StackTraceElement[] elements,int maxDepth){
        int realDepth=elements.length;
        if (realDepth>0){
            realDepth=Math.min(maxDepth,realDepth);
        }
        StackTraceElement[] real=new StackTraceElement[realDepth];
        System.arraycopy(elements,0,real,0,realDepth);
        return real;
    }

    /**
     * 获取忽略包名的堆栈信息
     */
    private static StackTraceElement[] getRealStackTrack(StackTraceElement[] stackTrace,String ignorePackage){
        int ignoreDepth=0;
        int allDepth=stackTrace.length;
        String className;
        for (int i = allDepth-1; i >=0 ; i--) {
            className=stackTrace[i].getClassName();
            if (ignorePackage!=null && className.startsWith(ignorePackage)){
                ignoreDepth++;
            }
        }
        int realDepth=allDepth-ignoreDepth;
        StackTraceElement[] real=new StackTraceElement[realDepth];
        System.arraycopy(stackTrace,ignoreDepth,real,0,realDepth);
        return real;
    }
}
