package com.kailaisi.library.cache

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

/**
 * 描述：
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-06-13:21:36
 */
@Entity(tableName = "tcb_cache")
class Cache {
    @NotNull
    @PrimaryKey()
    var key: String = ""

    var data: ByteArray? = null
}