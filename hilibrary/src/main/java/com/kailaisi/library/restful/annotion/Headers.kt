package com.kailaisi.library.restful.annotion

/**
 * header信息注解
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Headers(val value: Array<String>)
