package com.kailaisi.hiapp.http.api

import com.google.gson.JsonObject
import com.kailaisi.library.restful.HiCall
import com.kailaisi.library.restful.annotion.Field
import com.kailaisi.library.restful.annotion.GET

interface TestApi {
    @GET("cites")
    open fun getArchCode(@Field("name") name: String): HiCall<JsonObject>
}