package com.kailaisi.library.restful.annotion

/**
 *属性注解
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Field(val value: String)
