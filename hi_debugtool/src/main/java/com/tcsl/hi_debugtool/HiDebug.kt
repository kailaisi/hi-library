package com.tcsl.hi_debugtool

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class HiDebug(val name: String, val desc: String = "")
