package com.kailaisi.library.cache

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kailaisi.library.util.AppGlobals

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2021-06-13:21:49
 */
@Database(entities = [Cache::class], version = 1)
abstract class CacheDatabase : RoomDatabase() {

    abstract val cacheDao: CacheDao

    companion object {
        private val database by lazy {
            val context = AppGlobals.get()!!.applicationContext
            Room.databaseBuilder(context, CacheDatabase::class.java, "hi_cache").build()
        }

        fun get(): CacheDatabase {
            return database
        }
    }
}