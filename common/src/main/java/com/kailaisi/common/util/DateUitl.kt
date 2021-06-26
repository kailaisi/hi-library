package com.kailaisi.common.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil{
    public const val  MD_FORMATE="MM-dd"
    private const val DEFAULT_FORMAT="yyyy-MM-dd HH:mm:ss"

    fun getMDData(date: Date): String {
        val simpleDateFormat = SimpleDateFormat(MD_FORMATE, Locale.CANADA)
        return simpleDateFormat.format(date)
    }


    fun getMDData(dateString: String): String {
        val simpleDateFormat = SimpleDateFormat(DEFAULT_FORMAT, Locale.CANADA)
        return getMDData((simpleDateFormat.parse(dateString)))
    }

}
