package com.kailaisi.library.restful.annotion

/**
 * 基地址注解
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Path(val value: String)
