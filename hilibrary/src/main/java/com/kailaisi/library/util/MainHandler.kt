package com.kailaisi.library.util

import android.os.Handler
import android.os.Looper
import android.os.Message

/**
 * 描述：主线程处理
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-06-14:14:33
 */
object MainHandler {

    private val handler = Handler(Looper.getMainLooper())

    fun post(runnable: Runnable) {
        handler.post(runnable)
    }


    fun remove(runnable: Runnable) {
        handler.removeCallbacks(runnable)
    }
    fun postDelay(delayed: Long, runnable: Runnable) {
        handler.postDelayed(runnable, delayed)
    }

    fun sendAtFrontOfQueue(runnable: Runnable) {
        val obtain = Message.obtain(handler, runnable)
        handler.sendMessageAtFrontOfQueue(obtain)
    }
}