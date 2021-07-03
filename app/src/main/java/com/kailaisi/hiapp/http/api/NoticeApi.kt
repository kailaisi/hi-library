package com.kailaisi.hiapp.http.api

import com.kailaisi.hiapp.model.CourseNotice
import com.kailaisi.library.restful.HiCall
import com.kailaisi.library.restful.annotion.GET

interface NoticeApi {

   @GET("notice")
    fun notice(): HiCall<CourseNotice>

}
