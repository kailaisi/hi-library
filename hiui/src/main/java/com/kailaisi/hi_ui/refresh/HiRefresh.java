package com.kailaisi.hi_ui.refresh;

/**
 * 描述：
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-05-17:21:35
 */
public interface HiRefresh {
    /**
     * 下拉刷新过程中，禁止滚动
     */
    void setDisableRefreshScroll(boolean disable);

    /**
     * 刷新完成
     */
    void refreshFinished();

    /**
     * 设置下拉刷新的监听器
     *
     * @param listener
     */
    void setRefreshListener(HiRefreshListener listener);

    /**
     * 设置下拉刷新头部
     * @param view
     */
    void setRefreshOverView(HiOverView view);

    interface HiRefreshListener {
        //触发下拉刷新
        void onRefresh();
        //启动下拉刷新
        boolean enableRefresh();
    }
}
