package com.kailaisi.library.util

import androidx.lifecycle.*
import java.util.concurrent.ConcurrentHashMap

/**
 * 描述：基于LiveData的事件总线
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-05-28:21:53
 */
object HiDataBus {
    private val eventMap = ConcurrentHashMap<String, StickyLiveData<*>>()
    fun <T> with(event: String): StickyLiveData<T> {
        //基于事件名称  订阅 、分发消息
        //一个LiveData只能分发一种数据类型，所以需要不同的LiveData实例去分发
        var liveData = eventMap[event]
        if (liveData == null) {
            liveData = StickyLiveData<T>(event)
            eventMap[event] = liveData
        }
        return liveData as StickyLiveData<T>
    }


    class StickyLiveData<T>(private val eventName: String) : LiveData<T>() {
        var mData: T? = null

        var mVersion = 0

        fun setStickyData(data: T) {
            this.mData = data
            setValue(data)
        }

        fun postStickyData(data: T) {
            this.mData = data
            postValue(data)
        }

        override fun setValue(value: T) {
            mVersion++
            super.setValue(value)
        }

        override fun postValue(value: T) {
            mVersion++
            super.postValue(value)
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            observerSticky(owner, false, observer)
        }

        fun observerSticky(owner: LifecycleOwner, sticky: Boolean, observer: Observer<in T>) {
            //允许指定注册的观察者 是否关心黏性事件，
            // sticky=true，如果之前存在已经发送的数据，那么这个observer会收到之前的黏性事件
            owner.lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    //宿主发生销毁，则主动移除掉
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        eventMap.remove(eventName)
                    }
                }

            })
            super.observe(owner, StickyObserver(this, sticky, observer))
        }
    }

    class StickyObserver<T>(
        val stickyLiveData: StickyLiveData<T>, val sticky: Boolean, val observer: Observer<in T>,
    ) : Observer<T> {
        //两个对其，是为了控制黏性事件的分发
        //sticky ：false  则只能接受注册之后发送的数据
        private var lastVersion = stickyLiveData.mVersion
        override fun onChanged(t: T) {
            if (lastVersion >= stickyLiveData.mVersion) {
                if (sticky && stickyLiveData.mData != null) {
                    observer.onChanged(stickyLiveData.mData)
                }
                return
            }
            lastVersion = stickyLiveData.mVersion
            observer.onChanged(t)
        }

    }


}
