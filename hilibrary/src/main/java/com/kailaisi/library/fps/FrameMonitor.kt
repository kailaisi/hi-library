package com.kailaisi.library.fps

import android.view.Choreographer
import com.kailaisi.library.log.HiLog
import java.util.concurrent.TimeUnit

/**
 * 描述：Frame监听器
 * <p/>作者：wu
 * <br/>创建时间：2021-07-22:21:53
 */
internal class FrameMonitor : Choreographer.FrameCallback {
    private val choreographer = Choreographer.getInstance()

    //记录上一帧达到的时间
    private var startTime: Long = 0
    private val listeners = arrayListOf<FpsMonitor.FpsCallback>()

    //1s内绘制了多少针
    private var frameCount = 0
    override fun doFrame(frameTimeNanos: Long) {
        val current = TimeUnit.NANOSECONDS.toMillis(frameTimeNanos)
        if (startTime > 0) {
            val time = current - startTime
            frameCount++
            if (time > 1000) {
                val fps = frameCount * 1000 / time.toDouble()
                HiLog.e("FrameMonitor", fps)
                listeners.forEach { it.onFrame(fps) }
                frameCount = 0
                startTime = current
            }
        } else {
            startTime = current
        }
        start()
    }

    fun start() {
        choreographer.postFrameCallback(this)
    }

    fun stop() {
        startTime = 0
        listeners.clear()
        choreographer.removeFrameCallback(this)
    }

    fun addListener(listener: FpsMonitor.FpsCallback) {
        listeners.add(listener)
    }
}