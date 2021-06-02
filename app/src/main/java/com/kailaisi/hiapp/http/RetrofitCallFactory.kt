package com.kailaisi.hiapp.http

import com.kailaisi.library.restful.HiCall
import com.kailaisi.library.restful.HiCallback
import com.kailaisi.library.restful.HiRequest
import com.kailaisi.library.restful.HiResponse
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*

class RetrofitCallFactory(baseUrl: String) : HiCall.Factory {
    private var gsonConvert: GsonConvert
    var apiService: RetrofitApiService

    init {
        val build = Retrofit.Builder()
            .baseUrl(baseUrl)
            .build()
        apiService = build.create(RetrofitApiService::class.java)
        gsonConvert = GsonConvert()
    }

    override fun newCall(request: HiRequest): HiCall<Any> {
        return RetrofitCall(request)
    }

    internal inner class RetrofitCall<T>(val request: HiRequest) : HiCall<T> {


        override fun execute(): HiResponse<T> {
            val realCall = createRealCall(request)
            val response = realCall.execute()
            return parseResponse(response)
        }

        private fun parseResponse(response: Response<ResponseBody>): HiResponse<T> {
            var rawData: String? = null
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    rawData = body.string()
                }
            } else {
                val errorBody = response.errorBody()
                if (errorBody != null) {
                    rawData = errorBody.string()
                }
            }
            return gsonConvert.convert(rawData!!, request.returnType!!)
        }

        private fun createRealCall(request: HiRequest): Call<ResponseBody> {
            if (request.httpMethod == HiRequest.METHOD.GET) {
                return apiService.get(request.headers, request.domainUrl(), request.parameters)
            } else if (request.httpMethod == HiRequest.METHOD.POST) {
                val parameters = request.parameters
                var builder = FormBody.Builder()
                val jsonObject = JSONObject()
                parameters?.forEach {
                    if (request.formPost) {
                        builder.add(it.key, it.value)
                    } else {
                        jsonObject.put(it.key, it.value)
                    }
                }
                var requestBody: RequestBody? = null
                if (request.formPost) {
                    requestBody = builder.build()
                } else {
                    requestBody = RequestBody.create(MediaType.parse("application/json;utf-8"),
                        jsonObject.toString())
                }
                return apiService.post(request.headers, request.domainUrl(), requestBody)
            } else {
                throw  IllegalArgumentException("not support this method ")
            }
        }

        override fun enqueue(callback: HiCallback<T>?) {
            val realCall = createRealCall(request)
            realCall.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {
                    val parseResponse = parseResponse(response)
                    callback?.onSuccess(parseResponse)
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callback?.onFailed(t)
                }
            })
        }
    }
}

interface RetrofitApiService {
    @GET
    fun get(
        @HeaderMap header: MutableMap<String, String>?,
        @Url url: String, @QueryMap(encoded = true) params: MutableMap<String, String>?,
    ): Call<ResponseBody>


    @POST
    fun post(
        @HeaderMap header: MutableMap<String, String>?,
        @Url url: String, @Body body: RequestBody?,
    ): Call<ResponseBody>
}