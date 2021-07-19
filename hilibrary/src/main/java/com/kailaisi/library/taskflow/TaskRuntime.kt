package com.kailaisi.library.taskflow

import android.util.Log
import com.kailaisi.library.BuildConfig
import com.kailaisi.library.executor.HiExecutor
import com.kailaisi.library.util.MainHandler
import java.util.*

/**
 * 对Task的一些封装处理
 */
internal object TaskRuntime {
    //通过add指定启动阶段  需要阻塞完成的任务，只有当Block当中的任务都执行完了才会释放application的阻塞
    //才会拉起launchActivity
    val blockTasks = mutableListOf<String>()

    //如果blockTasks集合中的任务还没有完成，那么在主线程中的任务  会被添加到waitingTasks集合厘米啊去
    //目的是为了优先保证   阻塞任务的优先完成，尽可能早的拉起launchActivity
    val waitingTasks = mutableListOf<Task>()

    val taskRuntimeInfos = mutableMapOf<String, TaskRuntimeInfo>()


    val taskComparator = Comparator { o1: Task, o2: Task ->
        Utils.compareTask(o1, o2)
    }

    fun addBlockTask(id: String?) {
        if (!id.isNullOrBlank()) {
            blockTasks.add(id)
        }
    }

    fun addBlockTask(vararg ids: String) {
        if (ids.isNotEmpty()) {
            ids.forEach {
                addBlockTask(it)
            }
        }
    }

    fun setThreadName(task: Task, threadName: String) {
        getTaskRuntimeInfo(task.id)?.threadName = threadName
    }

    @JvmStatic
    fun setStateInfo(task: Task) {
        getTaskRuntimeInfo(task.id)?.setStateTime(task.state, System.currentTimeMillis())
    }

    fun getTaskRuntimeInfo(id: String): TaskRuntimeInfo? {
        return taskRuntimeInfos[id]
    }

    @JvmStatic
    fun removeBlockTask(id: String) {
        blockTasks.remove(id)
    }


    //根据task的属性和不同的策略，进行调度
    fun executeTask(task: Task) {
        if (task.isAsyncTask) {
            //异步任务
            HiExecutor.execute(runnable = task)
        } else {
            //主线程里面执行的
            //如果是延迟任务，那么它存在后置的阻塞任务，那么可能会导致我们的应用启动发生阻塞
            // 那么不处理
            if (task.delayMills > 0 && !hasBlockBehindTask(task)) {
                MainHandler.postDelay(task.delayMills, runnable = task)
                return
            }
            if (!hasBlockTasks()) {
                task.run()
            } else {
                addWaitingTask(task)
            }
        }
    }

    //把主线程上需要执行的任务，但又不影响launchActivity的启动，那么添加到等待队列
    private fun addWaitingTask(task: Task) {
        if (!waitingTasks.contains(task)) {
            waitingTasks.add(task)
        }
    }

    @JvmStatic
    fun hasBlockTasks(): Boolean {
        return !blockTasks.isNullOrEmpty()
    }


    //检测一个延迟任务后面是否存在着阻塞任务
    private fun hasBlockBehindTask(task: Task): Boolean {
        if (task is Project.CriticalTask) {
            return false
        }
        val behinds = task.behindTasks
        behinds.forEach {
            val taskRuntimeInfo = getTaskRuntimeInfo(it.id)
            if (taskRuntimeInfo != null && taskRuntimeInfo.isBlockTask) {
                return true
            } else {
                hasBlockBehindTask(it)
            }
        }
        return false
    }

    //校验：依赖树中是否存在环形依赖的校验，依赖树中是否存在taskId相同的任务，初始化task对应的taskRuntimeInfo
    //遍历依赖树，完成启动前的检查和初始化
    @JvmStatic
    fun traversalDependencyTreeAndInit(task: Task) {
        val set = linkedSetOf<Task>()
        set.add(task)
        innerTraversalDependencyTreeAndInit(task, set)
        blockTasks.forEach {
            if (!taskRuntimeInfos.containsKey(it)) {
                throw RuntimeException("block id $it is not in dependency")
            } else {
                val task = getTaskRuntimeInfo(it)
                traversalDependencyPriority(task)
            }
        }
    }

    private fun traversalDependencyPriority(task: TaskRuntimeInfo?) {
        if (task == null) return

    }

    private fun innerTraversalDependencyTreeAndInit(task: Task, set: LinkedHashSet<Task>) {
        var taskRuntimeInfo = getTaskRuntimeInfo(task.id)
        //重复添加校验
        if (taskRuntimeInfo == null) {
            taskRuntimeInfo = TaskRuntimeInfo(task)
            if (blockTasks.contains(task.id)) {
                taskRuntimeInfo.isBlockTask = true
            }
            taskRuntimeInfos[task.id] = taskRuntimeInfo
        } else {
            if (!taskRuntimeInfo.isSameTask(task)) {
                throw RuntimeException("not allow to contain the same id $task.id")
            }
        }

        //环形依赖校验
        task.behindTasks.forEach {
            if (!set.contains(it)) {
                set.add(it)
            } else {
                throw RuntimeException("not allow loop back dependency，  id $task.id")
            }
            if (BuildConfig.DEBUG && it.behindTasks.isEmpty()) {
                //可以输出出来链   it=end
                Log.e(
                    TaskRuntimeListener.TAG,
                    set.joinToString(separator = "---->") { inner -> inner.id })
            }
            //递归调用，进行处理
            innerTraversalDependencyTreeAndInit(it, set)
            set.remove(it)
        }
    }

    @JvmStatic
    fun hasWaitingTasks(): Boolean {
        return !waitingTasks.isNullOrEmpty()
    }

    @JvmStatic
    fun runWaitingTask() {
        if (hasWaitingTasks()) {
            if (waitingTasks.size > 1) {
                Collections.sort(waitingTasks, taskComparator)
            }
            if (hasBlockTasks()) {
                val head = waitingTasks.removeAt(0)
                head.run()
            } else {
                waitingTasks.forEach {
                    MainHandler.postDelay(it.delayMills, it)
                }
                waitingTasks.clear()
            }

        }
    }
}