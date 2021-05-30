package com.kailaisi.library.executor

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.IntRange
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

/**
 * 描述：支持按照任务的优先级去执行
 * 支持线程池暂停，恢复（批量文件下载，上传）
 * 支持异步结果主动回调主线程的能力
 * todo  线程池监控，耗时任务检测，定时，延迟
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-05-30:16:16
 */
object HiExecutor {
    private const val TAG = "HiExecuter"
    private var isPaused: Boolean = false
    private var hiExecutor: ThreadPoolExecutor
    private var lock: ReentrantLock = ReentrantLock()
    private var pauseCondition: Condition
    private val mainHandler = Handler(Looper.getMainLooper())

    init {
        pauseCondition = lock.newCondition()
        val cpuCount = Runtime.getRuntime().availableProcessors()
        val corePoosSize = cpuCount + 1
        val maxPoolSize = cpuCount * 2 + 1
        val blockingQueue: PriorityBlockingQueue<out Runnable> = PriorityBlockingQueue()
        val keepAliveTime = 30L
        val unit = TimeUnit.SECONDS
        val seq = AtomicLong()
        val threadFactory = ThreadFactory {
            val thread = Thread(it)
            thread.name = "hi-executor-" + seq.getAndIncrement()
            return@ThreadFactory thread
        }

        hiExecutor = object : ThreadPoolExecutor(corePoosSize,
            maxPoolSize,
            keepAliveTime,
            unit,
            blockingQueue as BlockingQueue<Runnable>,
            threadFactory) {
            override fun beforeExecute(t: Thread?, r: Runnable?) {
                if (isPaused) {
                    lock.lock()
                    try {
                        pauseCondition.await()
                    } finally {
                        lock.unlock()
                    }
                }
            }

            override fun afterExecute(r: Runnable?, t: Throwable?) {
                //监控线程池耗时任务，线程创建数量，正在运行的数量
                Log.d(TAG, "已执行完的任务的优先级是：${(r as PriorityRunnable).priority}")
            }
        }
    }

    fun execute(@IntRange(from = 0, to = 10) priority: Int=0, runnable: Runnable) {
        PriorityRunnable(priority, runnable)
        hiExecutor.execute(runnable)
    }

    fun execute(@IntRange(from = 0, to = 10) priority: Int=0, runnable: Callable<*>) {
        PriorityRunnable(priority, runnable)
        hiExecutor.execute(runnable)
    }

    class PriorityRunnable(val priority: Int, val runnable: Runnable) : Runnable,
        Comparable<PriorityRunnable> {
        override fun run() {
            runnable.run()
        }

        override fun compareTo(other: PriorityRunnable): Int {
            return other.priority - priority
        }

    }

    abstract class Callable<T> : Runnable {
        override fun run() {
            mainHandler.post { onPrepare() }
            val t = onBackground()
            mainHandler.post { onCompleted(t) }
        }

        abstract fun onBackground(): T
        abstract fun onCompleted(t: T)
        open fun onPrepare() {
            //转动画
        }
    }

    @Synchronized
    fun pause() {
        isPaused = true
        Log.e(TAG, "hiexecutor is paused")
    }

    @Synchronized
    fun resume() {
        isPaused = false
        lock.lock()
        try {
            pauseCondition.signalAll()
        } finally {
            lock.unlock()
        }
        Log.e(TAG, "hiexecutor is resume")
    }
}