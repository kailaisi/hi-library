package com.kailaisi.library.taskflow

import android.util.Log
import com.kailaisi.library.BuildConfig

class TaskRuntimeListener:TaskListener {
    override fun onStart(task: Task) {
        if (BuildConfig.DEBUG){
            Log.e(TAG, task.id+ START_METHOD)
        }
    }

    override fun onRunning(task: Task) {
        if (BuildConfig.DEBUG){
            Log.e(TAG, task.id+ START_METHOD)
        }
    }

    override fun onFinished(task: Task) {
        if (BuildConfig.DEBUG){
            Log.e(TAG, task.id+ START_METHOD)
        }
    }

    companion object{
        const val TAG="TaskFlow"
        const val  START_METHOD="--- onStart ---"
        const val  RUNNING_METHOD="--- onRunning ---"
        const val  FINISHED_METHOD="--- onFinished ---"
    }
}