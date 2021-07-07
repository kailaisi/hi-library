package com.kailaisi.hiapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.DialogFragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.kailaisi.common.ui.component.HiBaseActivity
import com.kailaisi.hiapp.BuildConfig
import com.kailaisi.hiapp.databinding.ActivityMainBinding
import com.kailaisi.hiapp.ui.login.MainActivityLogic
import com.kailaisi.library.util.HiStatusBar

@Route(path = "/app/main")
class MainActivity : HiBaseActivity(), MainActivityLogic.ActivityProvider {
    lateinit var logic: MainActivityLogic
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        logic = MainActivityLogic(this, savedInstanceState)
        HiStatusBar.setStatusBar(this, true)
    }

    /**
     * 防止压后台之后，再回来导致的fragment重叠
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        logic.onSaveInstanceState(outState)
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (BuildConfig.DEBUG) {
                val clazz = Class.forName("com.kailaisi.hi_debugtool.DebugToolDialogFragment")
                val target = clazz.getConstructor().newInstance() as DialogFragment
                target.show(supportFragmentManager, "DebugToolDialogFragment")
            }
        }
        return super.onKeyDown(keyCode, event)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.fragments.forEach {
            it.onActivityResult(requestCode, resultCode, data)
        }
    }
}