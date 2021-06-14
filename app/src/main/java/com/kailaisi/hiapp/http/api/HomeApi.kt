package com.kailaisi.hiapp.http.api

import com.kailaisi.hiapp.model.HomeModel
import com.kailaisi.hiapp.model.TabCategory
import com.kailaisi.library.restful.HiCall
import com.kailaisi.library.restful.annotion.CacheStrategy
import com.kailaisi.library.restful.annotion.Field
import com.kailaisi.library.restful.annotion.GET
import com.kailaisi.library.restful.annotion.Path

/**
 * 首页的接口
 */
interface HomeApi {
    @CacheStrategy(CacheStrategy.CACHE_FIRST)
    @GET("category/categories")
    fun queryTabList(): HiCall<List<TabCategory>>

    @GET("home/{categoryId}")
    fun queryTabCategoryList(
        @CacheStrategy cacheStrategy: Int,
        @Path("categoryId") categoryId: String,
        @Field("pageIndex") pageIndex: Int,
        @Field("pageSize") pageSize: Int,
    ): HiCall<HomeModel>
}