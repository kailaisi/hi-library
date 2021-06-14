package com.kailaisi.library.cache

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2021-06-13:21:36
 */
@Entity(tableName = "tcb_cache")
class Cache {
    @PrimaryKey(autoGenerate = true)
    var key:String=""

    var data:ByteArray?=null
}