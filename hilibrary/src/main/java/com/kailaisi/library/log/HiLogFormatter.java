package com.kailaisi.library.log;

/**
 * 描述：格式化接口
 * <p/>作者：wu
 * <br/>创建时间：2021-05-11:21:56
 */
interface HiLogFormatter<T> {
    String format(T data);
}
