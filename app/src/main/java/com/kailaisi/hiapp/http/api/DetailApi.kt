package com.kailaisi.hiapp.http.api

import com.kailaisi.hiapp.model.DetailModel
import com.kailaisi.hiapp.model.TabCategory
import com.kailaisi.library.restful.HiCall
import com.kailaisi.library.restful.annotion.CacheStrategy
import com.kailaisi.library.restful.annotion.GET
import com.kailaisi.library.restful.annotion.Path

/**
 * 首页的接口
 */
interface DetailApi {
    @CacheStrategy(CacheStrategy.CACHE_FIRST)
    @GET("goods/detail/{id}")
    fun queryDetail(@Path("id") goodsId: String): HiCall<DetailModel>

}