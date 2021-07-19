package com.kailaisi.library.taskflow

import android.util.Log
import com.kailaisi.library.BuildConfig

class TaskRuntimeListener : TaskListener {
    override fun onStart(task: Task) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, task.id + START_METHOD)
        }
    }

    override fun onRunning(task: Task) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, task.id + RUNNING_METHOD)
        }
    }

    override fun onFinished(task: Task) {
        logTaskRuntimeInfo(task)
    }

    private fun logTaskRuntimeInfo(task: Task) {
        val taskRuntimeInfo = TaskRuntime.getTaskRuntimeInfo(task.id) ?: return
        val startTime = taskRuntimeInfo.stateTime[TaskState.START]
        val runningTime = taskRuntimeInfo.stateTime[TaskState.RUNNING]
        val endTime = taskRuntimeInfo.stateTime[TaskState.FINISHED]
        val builder = StringBuilder()
        builder.append(WRAPPER)
            .append(TAG)
            .append(WRAPPER)
            .append(WRAPPER)
            .append(HALF_LINE)
            .append(if (task is Project) "Project" else "task $task.id")
            .append(HALF_LINE)
        addTaskInfoLineInfo(builder, DEPENDENCY, getTaskDependenciesInfo(task))
        addTaskInfoLineInfo(builder, IS_BLOCK_TASK, taskRuntimeInfo.isBlockTask.toString())
        addTaskInfoLineInfo(builder, THREAD_NAME, taskRuntimeInfo.threadName!!)
        addTaskInfoLineInfo(builder, THREAD_NAME, taskRuntimeInfo.threadName!!)
        addTaskInfoLineInfo(builder, START_TIME, startTime.toString() + "ms")
        addTaskInfoLineInfo(builder, WAITING_TIME, (runningTime - startTime).toString() + "ms")
        addTaskInfoLineInfo(builder, FINISH_TIME, endTime.toString() + "ms")

        builder.append(HALF_LINE)
            .append(HALF_LINE)
            .append(WRAPPER)
            .append(WRAPPER)
        if (BuildConfig.DEBUG) {
            Log.d(TAG, builder.toString())
        }
    }

    private fun getTaskDependenciesInfo(task: Task): String {
        return task.dependTaskNames.joinToString { "$it " }
    }

    private fun addTaskInfoLineInfo(builder: StringBuilder, key: String, value: Any) {
        builder.append("$key : $value")
    }

    companion object {
        const val TAG = "TaskFlow"
        const val START_METHOD = "--- onStart ---"
        const val RUNNING_METHOD = "--- onRunning ---"
        const val FINISHED_METHOD = "--- onFinished ---"
        const val DEPENDENCY = "依赖任务"
        const val THREAD_NAME = "线程名称"
        const val WRAPPER = "=========="
        const val HALF_LINE = "======"
        const val IS_BLOCK_TASK = "是否阻塞"
        const val START_TIME = "启动时间"
        const val WAITING_TIME = "等待时间"
        const val FINISH_TIME = "结束时间"
        const val TASK_CONSUME = "任务耗时"

    }
}