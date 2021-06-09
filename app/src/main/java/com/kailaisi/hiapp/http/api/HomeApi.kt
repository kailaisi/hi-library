package com.kailaisi.hiapp.http.api

import com.kailaisi.hiapp.model.TabCategory
import com.kailaisi.library.restful.HiCall
import com.kailaisi.library.restful.annotion.GET

/**
 * 首页的接口
 */
interface HomeApi {
    @GET("category/categories")
    fun queryTabList():HiCall<List<TabCategory>>
}