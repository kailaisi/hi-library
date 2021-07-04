package com.kailaisi.biz_search

import com.kailaisi.library.restful.HiCall
import com.kailaisi.library.restful.HiCallback
import com.kailaisi.library.restful.annotion.Field
import com.kailaisi.library.restful.annotion.GET
import com.kailaisi.library.restful.annotion.POST

/**
 * 描述：搜索页面使用到的API
 * <p/>作者：wu
 * <br/>创建时间：2021-07-04:14:04
 */
interface SearchApi {
    @GET("/goods/search/quick")
    fun quickSearch(@Field("key") key: String): HiCall<QuickSearchList>


    @POST("/goods/search/quick",formPost = false)
    fun goodSearch(
        @Field("keyword") keyword: String,
        @Field("pageIndex") pageIndex: Int,
        @Field("pageSize") pageSize: Int
    ): HiCall<GoodsSearchList>
}