package com.kailaisi.hiapp.model

import com.kailaisi.pub_mod.NoticeInfo
import java.io.Serializable

data class CourseNotice(val total:Int,val list: List<NoticeInfo>?):Serializable
