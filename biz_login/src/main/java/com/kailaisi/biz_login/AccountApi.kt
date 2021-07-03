package com.kailaisi.biz_login

import com.kailaisi.library.restful.HiCall
import com.kailaisi.library.restful.annotion.Field
import com.kailaisi.library.restful.annotion.GET
import com.kailaisi.library.restful.annotion.POST
import com.kailaisi.pub_mod.UserProfile

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

/*    @GET("notice")
    fun notice(): HiCall<CourseNotice>*/

}
