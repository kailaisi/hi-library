package com.kailaisi.hiapp.http.api

import com.kailaisi.hiapp.model.UserProfile
import com.kailaisi.library.restful.HiCall
import com.kailaisi.library.restful.annotion.Field
import com.kailaisi.library.restful.annotion.GET
import com.kailaisi.library.restful.annotion.POST

interface AccountApi {

    @POST("user/login")
    fun login(
        @Field("userName") name: String,
        @Field("password") password: String,
    ): HiCall<String>

    @POST("user/registtration")
    fun register(
        @Field("userName") name: String,
        @Field("password") password: String,
        @Field("imoocId") imoocId: String,
        @Field("orderId") orderId: String,
    ): HiCall<String>

    @GET("user/profile")
    fun profile(): HiCall<UserProfile>

}
