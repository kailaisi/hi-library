package com.kailaisi.biz_order.address

import com.kailaisi.library.restful.HiCall
import com.kailaisi.library.restful.annotion.Field
import com.kailaisi.library.restful.annotion.GET

/**
 * 描述：地址相关API
 * <p/>作者：wu
 * <br/>创建时间：2021-07-07:21:40
 */
interface AddressApi {

    @GET("address")
    fun address(@Field("pageIndex") pageIndex: Int, @Field("pageSize") pageSize: Int):HiCall<AddressModel>

}