package com.kailaisi.library.taskflow

import android.util.SparseArray

/**
 * 用于记录每个Task实例在运行时的信息的封装
 */
class TaskRuntimeInfo(val task: Task) {
    val stateTime = SparseArray<Long>()

    var isBlockTask = false

    var threadName: String? = null

    fun setStateTime(state: Int, time: Long) {
        stateTime.put(state, time)
    }

    fun isSameTask(task: Task?): Boolean {
        return task != null && this.task == task
    }

}
