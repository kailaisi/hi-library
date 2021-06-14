package com.kailaisi.hiapp.ui.login

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.kailaisi.common.ui.component.HiBaseActivity
import com.kailaisi.hiapp.databinding.ActivityLoginBinding
import com.kailaisi.hiapp.http.ApiFactory
import com.kailaisi.hiapp.http.api.AccountApi
import com.kailaisi.hiapp.ui.account.AccountManager
import com.kailaisi.library.restful.HiCallback
import com.kailaisi.library.restful.HiResponse
import com.kailaisi.library.util.HiStatusBar
import com.kailaisi.library.util.inflate

@Route(path = "/account/login")
class LoginActivity : HiBaseActivity() {
    val REQUEST_CODE_REGISTER = 1000
    private val mBinding: ActivityLoginBinding by inflate()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mBinding.tvRegister.setOnClickListener {
            ARouter.getInstance().build("/account/register").navigation(this, REQUEST_CODE_REGISTER)
        }
        mBinding.btnLogin.setOnClickListener {
            val userName = mBinding.userName.editText.text.toString()
            val pwd = mBinding.pwd.editText.text.toString()
            ApiFactory.create(AccountApi::class.java).login(userName, pwd)
                .enqueue(object : HiCallback<String> {
                    override fun onSuccess(response: HiResponse<String>) {
                        if (response.sucessful()) {
                            AccountManager.loginSuccess(response.data!!)
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "登录失败", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailed(throwable: Throwable) {
                        Toast.makeText(this@LoginActivity, throwable.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                })
        }
        HiStatusBar.setStatusBar(this, true, Color.WHITE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}