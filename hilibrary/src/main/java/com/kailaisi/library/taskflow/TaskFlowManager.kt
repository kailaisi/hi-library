package com.kailaisi.library.taskflow

import android.os.Looper
import androidx.annotation.MainThread

/**
 * 对taskRuntime的包装，对外暴露的类，用于启动启动任务
 */
object TaskFlowManager {

    fun addBlockTask(taskId: String): TaskFlowManager {
        TaskRuntime.addBlockTask(taskId)
        return this
    }


    fun addBlockTask(vararg taskIds: String): TaskFlowManager {
        TaskRuntime.addBlockTask(*taskIds)
        return this
    }

    @MainThread
    fun start(task: Task) {
        assert(Thread.currentThread() == Looper.getMainLooper().thread) { "start must be on main thread" }
        val startTask = if (task is Project) {
            task.startTask
        } else task
        TaskRuntime.traversalDependencyTreeAndInit(startTask)
        startTask.start()
        while (TaskRuntime.hasBlockTasks()) {
            kotlin.runCatching {
                Thread.sleep(10)
            }
            //主线程唤醒之后，有等待队列任务，那么可以执行等待任务
            if (TaskRuntime.hasWaitingTasks()) {
                TaskRuntime.runWaitingTask()
            }
        }
    }
}