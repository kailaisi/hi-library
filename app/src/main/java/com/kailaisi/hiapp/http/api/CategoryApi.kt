package com.kailaisi.hiapp.http.api

import com.kailaisi.hiapp.model.Subcategory
import com.kailaisi.hiapp.model.TabCategory
import com.kailaisi.library.restful.HiCall
import com.kailaisi.library.restful.annotion.GET
import com.kailaisi.library.restful.annotion.Path

interface CategoryApi {
    @GET("category/categories")
    fun queryCategoryList():HiCall<List<TabCategory>>

    @GET("category/categories/{categoryId}")
    fun querySubCategoryList(@Path("categoryId") categoryId:String):HiCall<List<Subcategory>>

}