package com.kailaisi.library.aspectj

/**
 * 描述：标记的注解信息
 * <p/>作者：wu
 * <br/>创建时间：2021-07-21:22:07
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CONSTRUCTOR)
@Retention(AnnotationRetention.RUNTIME)
annotation class MethodTrace()
