package com.kailaisi.hiapp.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.kailaisi.common.ui.component.HiBaseFragment
import com.kailaisi.hiapp.R
import com.kailaisi.hiapp.databinding.FragmentProfileBinding
import com.kailaisi.hiapp.http.ApiFactory
import com.kailaisi.hiapp.http.api.AccountApi
import com.kailaisi.hiapp.model.UserProfile
import com.kailaisi.library.restful.HiCallback
import com.kailaisi.library.restful.HiResponse
import com.kailaisi.library.util.bindView

/**
 * 描述：
 *
 * 作者：kailaisi
 * <br></br>创建时间：2021-05-15:17:50
 */
class ProfileFragment : HiBaseFragment() {
    val ITEM_SPACE_HOLDER = "   "
    private val mBinding: FragmentProfileBinding by bindView()

    override fun getLayoutId(): Int {
        return R.layout.fragment_profile
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.notifyTitle.setText(R.string.if_notify)
        mBinding.notifyTitle.append(ITEM_SPACE_HOLDER + getString(R.string.item_notify))

        mBinding.notifyCollection.setText(R.string.if_collection)
        mBinding.notifyCollection.append(ITEM_SPACE_HOLDER + getString(R.string.item_collection))

        mBinding.notifyHistory.setText(R.string.if_history)
        mBinding.notifyHistory.append(ITEM_SPACE_HOLDER + getString(R.string.item_history))

        mBinding.notifyLocation.setText(R.string.if_address)
        mBinding.notifyLocation.append(ITEM_SPACE_HOLDER + getString(R.string.item_location))


        queryUserDetail()
    }

    private fun queryUserDetail() {
        ApiFactory.create(AccountApi::class.java).profile()
            .enqueue(object : HiCallback<UserProfile> {
                override fun onSuccess(response: HiResponse<UserProfile>) {
                    val data = response.data
                    if (response.code == HiResponse.SUCCESS && response.data != null) {
                        updateUI(data!!)
                    } else {
                        showToast(response.msg)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    showToast(throwable.message)
                }

            })
    }

    private fun showToast(message: String?) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(data: UserProfile) {

    }
}