package com.kailaisi.hiapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseBindingActivity<T:ViewBinding> : AppCompatActivity() {
    val TAG by lazy {
        javaClass.simpleName.toString()
    }
    lateinit var mBinding:T


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=getBinding()
        setContentView(mBinding.root)
        initView()
    }

    fun startActivity(clazz: Class<*>){
        startActivity(Intent(this,clazz))
    }

    abstract fun initView()


    abstract fun getBinding():T
}
