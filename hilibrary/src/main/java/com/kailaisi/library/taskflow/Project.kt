package com.kailaisi.library.taskflow

/**
 * 一个任务的任务组，里面可能包含了多个任务
 */
class Project private constructor(id: String) : Task(id) {
    lateinit var endTask: Task
    lateinit var startTask: Task
    override fun run(id: String) {
        //TODO("Not yet implemented")
    }

    override fun addBehind(task: Task) {
        //给当前任务组增加一个后置的任务，
        //把这个任务放到最后，这样，只有task执行完了以后，任务组才算执行完毕
        endTask.addBehind(task)
    }

    override fun addDependOn(task: Task) {
        startTask.addBehind(task)
    }

    override fun removeDependence(task: Task) {
        startTask.removeDependence(task)
    }

    override fun removeBehind(task: Task) {
        endTask.removeBehind(task)
    }

    class Builder(val projectName: String, iTaskCreator: TaskCreator) {
        private val mTaskFactory = TaskFactory(iTaskCreator)
        private val mStartTask: Task = CriticalTask(projectName + "_start")
        private val mEndTask: Task = CriticalTask(projectName + "_end")
        private val mProject = Project(projectName)
        private var mPriority = 0//默认任务组中，所有的任务优先级的   最高的

        //本次添加的task是否可以直接依赖startTask，
        private var mCurrentShouldDependOnStartTask = true

        private var mCurrentAddTask: Task? = null
        fun add(id: String): Builder {
            val task = mTaskFactory.getTask(id)
            if (task.priority > mPriority) {
                mPriority = task.priority
            }
            return add(task)
        }

        private fun add(task: Task): Builder {
            if (mCurrentShouldDependOnStartTask && mCurrentAddTask != null) {
                mStartTask.addBehind(task)
            }
            mCurrentAddTask = task
            mCurrentShouldDependOnStartTask = true
            mCurrentAddTask?.addBehind(mEndTask)
            return this
        }

        fun dependOn(id: String): Builder {
            return dependOn(mTaskFactory.getTask(id))
        }

        private fun dependOn(task: Task): Builder {
            //确定刚才添加的mCurrentTask和task的依赖关系
            task.addBehind(mCurrentAddTask!!)
            mEndTask.removeDependence(task)
            mCurrentShouldDependOnStartTask = false
            return this
        }

        fun build(): Project {
            if (mCurrentAddTask == null) {
                mStartTask.addBehind(mEndTask)
            } else {
                if (mCurrentShouldDependOnStartTask) {
                    mStartTask.addBehind(mCurrentAddTask!!)
                }
            }
            mStartTask.priority = mPriority
            mEndTask.priority = mPriority
            mProject.startTask=mStartTask
            mProject.endTask=mEndTask
            return mProject
        }
    }

    private class TaskFactory(private val iTaskCreator: TaskCreator) {
        //利用iTaskCreator创建task实例，并管理
        private val mCacheTasks = HashMap<String, Task>()

        fun getTask(id: String): Task {
            var task = mCacheTasks[id]
            if (task != null) {
                return task
            }
            task = iTaskCreator.createTask(id)
            requireNotNull(task) { "create task failed,make sure taskcreateor can create a task with only taskId" }
            mCacheTasks[id] = task
            return task
        }
    }

    //用于创建临时的节点信息
    private class CriticalTask internal constructor(id: String) : Task(id) {
        override fun run(id: String) {
        }
    }
}