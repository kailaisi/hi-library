package com.kailaisi.hiapp.http.api

import com.google.gson.JsonObject
import com.kailaisi.library.restful.HiCall
import com.kailaisi.library.restful.annotion.Field
import com.kailaisi.library.restful.annotion.GET
import com.kailaisi.library.restful.annotion.POST

interface AccountApi {

    @POST("user/login")
    open fun getArchCode(@Field("name") name: String): HiCall<JsonObject>
}