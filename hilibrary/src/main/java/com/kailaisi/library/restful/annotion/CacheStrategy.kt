package com.kailaisi.library.restful.annotion

import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Retention
/**
 * 描述：缓存策略机制
 * <p/>作者：wu
 * <br/>创建时间：2021-06-14:12:20
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
annotation class CacheStrategy(val value: Int= NET_ONLY) {
    companion object {
        const val CACHE_FIRST = 0;//请求接口时，缓存优先；然后再读取接口，接口成功后更新缓存（页面初始化数据）
        const val NET_ONLY = 1;//仅仅接口请求（一般是分页和独立非列表页）
        const val NET_CACHE = 2 //先接口，更新成功后更新缓存（一般是下拉刷新）
    }
}
