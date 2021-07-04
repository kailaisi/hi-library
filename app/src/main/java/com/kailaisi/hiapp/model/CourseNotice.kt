package com.kailaisi.hiapp.model

import com.kailaisi.service_login.NoticeInfo
import java.io.Serializable

data class CourseNotice(val total:Int,val list: List<NoticeInfo>?):Serializable
