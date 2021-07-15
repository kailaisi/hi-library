package com.kailaisi.library.taskflow

interface TaskListener {
    fun onStart(task: Task)
    fun onRunning(task: Task)
    fun onFinished(task: Task)
}