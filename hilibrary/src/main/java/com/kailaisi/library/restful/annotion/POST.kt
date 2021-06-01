package com.kailaisi.library.restful.annotion

/**
 * 方法注解
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class POST(val value: String, val formPost: Boolean = true)
