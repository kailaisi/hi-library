package com.kailaisi.library.aspectj

import android.util.Log
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2021-07-21:22:00
 */
@Aspect
class ActivityAspect {
    /**
     * 统计setContentView方法的耗时
     */
    @Around("execution(* android.app.Activity.se tContentView(..))")
    fun setContentView(joinPoint: ProceedingJoinPoint) {
        adviceCode(joinPoint)
    }

    private fun adviceCode(joinPoint: ProceedingJoinPoint) {
        val signature = joinPoint.signature
        val className = signature.declaringType.simpleName
        val methodName = signature.name
        val time = System.currentTimeMillis()
        joinPoint.proceed()
        Log.e(
            "ActivityAspect",
            "$className :$methodName cost time:${System.currentTimeMillis() - time}"
        )
    }


    @Around("execution(@com.kailaisi.library.aspectj.MethodTrace * *(..))")
    fun methodTrace(joinPoint: ProceedingJoinPoint) {
        adviceCode(joinPoint)
    }
}