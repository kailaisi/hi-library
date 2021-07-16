package com.kailaisi.library.taskflow

interface TaskCreator {
    fun createTask(taskName: String): Task?
}