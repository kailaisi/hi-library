package com.kailaisi.library.cache

import androidx.room.*

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2021-06-13:21:55
 */
@Dao
interface CacheDao {

    @Insert(entity = Cache::class, onConflict = OnConflictStrategy.REPLACE)
    fun save(cache: Cache): Long

    @Query("select * from tcb_cache where `key`=:key")
    fun getCache(key: String): Cache?

    @Delete(entity = Cache::class)
    fun delete(cache: Cache)
}