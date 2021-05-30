package com.kailaisi.library.log;

import android.os.Trace;

/**
 * 描述：堆栈信息打印
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-05-11:22:00
 */
public class HiStackTraceFormatter implements HiLogFormatter<StackTraceElement[]> {
    @Override
    public String format(StackTraceElement[] data) {
        StringBuilder sb = new StringBuilder(128);
        if (data == null || data.length == 0) {
            return null;
        } else if (data.length == 1) {
            return "\t-" + data[0].toString();
        } else {
            int len = data.length;
            for (int i = 0; i < len; i++) {
                if (i == 0) {
                    sb.append("stackTrace:\n");
                }
                if (i != len - 1) {
                    sb.append("\t-├");
                    sb.append(data[i].toString());
                    sb.append("\n");
                } else {
                    sb.append("\t└");
                    sb.append(data[i].toString());
                }
            }
            return sb.toString();
        }
    }
}
