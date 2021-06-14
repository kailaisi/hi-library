package com.kailaisi.hiapp.route;

/**
 * 描述：跳转需要的字段
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-06-06:13:39
 */
public interface RouterFlag {
    /*登录*/
    int FLAG_LOGIN = 0x01;
    /*认证*/
    int FLAG_AUTHENTICATION = FLAG_LOGIN << 1;
    /*VIP*/
    int FLAG_VIP = FLAG_AUTHENTICATION << 1;
}
