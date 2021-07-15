package com.kailaisi.library.taskflow

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(TaskState.IDLE, TaskState.START, TaskState.RUNNING, TaskState.FINISHED)
annotation class TaskState {
    companion object {
        const val IDLE = 0
        const val START = 1
        const val RUNNING = 2
        const val FINISHED = 3
    }
}


