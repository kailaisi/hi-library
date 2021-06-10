package com.kailaisi.hi_ui.banner.core;

/**
 * 描述：HiBanner的数据绑定接口，基于该接口可以实现数据绑定和框架层解耦
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-05-24:21:55
 */
public interface IBindAdapter {
    void onBind(HiBannerAdapter.HiBannerViewHolder holder,HiBannerMo mo,int pos);
}
