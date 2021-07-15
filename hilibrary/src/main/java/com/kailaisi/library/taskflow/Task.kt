package com.kailaisi.library.taskflow

import androidx.core.os.TraceCompat
import java.lang.RuntimeException
import java.util.*
import kotlin.Comparator

abstract class Task @JvmOverloads constructor(
    val id: String,
    val isAsyncTask: Boolean,
    val delayMills: Long = 0,
    var priority: Int = 0
) : Runnable, Comparable<Task> {

    //执行时间
    var executeTime: Long = 0
        protected set

    //任务状态
    var state: Int = TaskState.IDLE
        protected set

    //当前任务的依赖任务，只有当dependTasks都执行完了之后，才能执行当前任务
    val dependTasks = mutableListOf<Task>()

    //当前任务的后置任务，只有当前任务执行完毕之后，后置的任务才能执行
    val behindTasks = mutableListOf<Task>()

    private var taskRuntimeListener: TaskRuntimeListener? = TaskRuntimeListener()
    private val taskListeners = mutableListOf<TaskListener>(taskRuntimeListener!!)

    //用于在运行时log的输出，输出当前task依赖的task的名称
    private val dependTaskNames = mutableListOf<String>()

    val taskComparator = Comparator { o1: Task, o2: Task ->
        Utils.compareTask(o1, o2)
    }

    fun addTaskListener(taskListener: TaskListener) {
        if (!taskListeners.contains(taskListener)) {
            taskListeners.add(taskListener)
        }
    }

    open fun start() {
        if (state != TaskState.IDLE) {
            throw RuntimeException("cannot run task $id again")
        }
        toStart()
        executeTime = System.currentTimeMillis()
        //执行当前任务
        //todo executeTask(this)
    }

    private fun toStart() {
        state = TaskState.START
        for (taskListener in taskListeners) {
            taskListener.onStart(this)
        }
    }

    override fun run() {
        //改变任务的状态 onstart-  onrunning  onfinished   -然后通知后置任务去开始执行
        TraceCompat.beginSection(id)
        toRunning()
        run(id)//真正的执行，初始化任务代码的方法
        toFinish()
        //通知后置任务去执行
        notifyBehind()
        recycle()
        TraceCompat.endSection()
    }

    private fun notifyBehind() {
        if (behindTasks.isNotEmpty()) {
            if (behindTasks.size > 1) {
                Collections.sort(behindTasks, taskComparator)
            }
            //遍历，通知，其一个前置依赖已经执行完成了
            for (behindTask in behindTasks) {
                behindTask.dependTaskFinished(this)
            }
        }
    }

    //前置依赖已经执行完了
    private fun dependTaskFinished(task: Task) {
        if (dependTasks.isEmpty()) {
            return
        }
        //移除前置依赖
        dependTasks.remove(task)
        //所有的前置以来任务执行完了，则执行当前任务
        if (dependTasks.isEmpty()) {
            start()
        }
    }

    //添加一个前置任务
    fun addDependOn(task: Task) {
        if (task != this) {
            dependTasks.add(task)
            dependTaskNames.add(task.id)
            //还需要将this设置为task的behindTask
            if (!task.behindTasks.contains(this)) {
                task.behindTasks.add(this)
            }
        }
    }

    //移除前置依赖
    fun removeDependence(task: Task) {
        if (task != this) {
            dependTasks.remove(task)
            dependTaskNames.remove(task.id)
            task.behindTasks.remove(this)
        }
    }

    //添加一个后置以来
    fun addBehind(task: Task) {
        if (task != this) {
            behindTasks.add(task)
            if (!behindTasks.contains(this)) {
                task.addDependOn(this)
            }
        }
    }

    //移除后置任务
    fun removeBehind(task: Task) {
        if (task != this) {
            behindTasks.remove(task)
            task.removeDependence(this)
        }
    }

    private fun recycle() {
        dependTasks.clear()
        behindTasks.clear()
        taskListeners.clear()
        taskRuntimeListener = null
    }

    private fun toFinish() {
        state = TaskState.FINISHED
        for (listener in taskListeners) {
            listener.onFinished(this)
        }
    }

    private fun toRunning() {
        state = TaskState.RUNNING
        for (listener in taskListeners) {
            listener.onRunning(this)
        }
    }

    abstract fun run(id: String)

    override fun compareTo(other: Task): Int {
        return Utils.compareTask(this, other)
    }
}