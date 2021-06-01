package com.kailaisi.library.restful.annotion

/**
 * 基地址注解
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseUrl(val value: String)
