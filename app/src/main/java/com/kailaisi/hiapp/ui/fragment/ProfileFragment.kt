package com.kailaisi.hiapp.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.launcher.ARouter
import com.kailaisi.common.ui.component.HiBaseFragment
import com.kailaisi.common.ui.view.loadCircle
import com.kailaisi.common.ui.view.loadCorner
import com.kailaisi.hi_ui.banner.core.HiBannerMo
import com.kailaisi.hiapp.R
import com.kailaisi.hiapp.databinding.FragmentProfileBinding
import com.kailaisi.hiapp.http.ApiFactory
import com.kailaisi.hiapp.http.api.AccountApi
import com.kailaisi.hiapp.model.NoticeInfo
import com.kailaisi.hiapp.model.UserProfile
import com.kailaisi.hiapp.ui.account.AccountManager
import com.kailaisi.library.restful.HiCallback
import com.kailaisi.library.restful.HiResponse
import com.kailaisi.library.util.HiDisplayUtils
import com.kailaisi.library.util.bindView

/**
 * 描述：
 *
 * 作者：kailaisi
 * <br></br>创建时间：2021-05-15:17:50
 */
class ProfileFragment : HiBaseFragment() {
    private val REQUEST_LOGIN = 1
    private val mBinding: FragmentProfileBinding by bindView()

    override fun getLayoutId(): Int {
        return R.layout.fragment_profile
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.notifyTitle.text = getString(R.string.item_notify)

        mBinding.notifyCollection.text = getString(R.string.item_collection)

        mBinding.notifyHistory.text = getString(R.string.item_history)

        mBinding.notifyLocation.text = getString(R.string.item_location)

        queryCourseNotice()

        queryUserDetail()
    }

    private fun queryCourseNotice() {
    }

    private fun queryUserDetail() {
        AccountManager.getUserProfile(this, Observer {
            if (it != null) {
                updateUI(it)
            } else {
                showToast(getString(R.string.fetch_user_profile_failed))
            }
        }, false)
    }

    private fun showToast(message: String?) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(data: UserProfile) {
        mBinding.userName.text =
            if (data.isLogin) data.userName else getString(R.string.profile_not_login)
        mBinding.tvLoginDes.text =
            if (data.isLogin) getString(R.string.profile_login_desc_welcome_back) else getString(R.string.profile_login_desc)
        mBinding.tabItemCollection.text =
            spannableItem(data.favoriteCount, getString(R.string.profile_tab_item_collection))
        mBinding.tabItemHistory.text =
            spannableItem(data.brrowseCount, getString(R.string.profile_tab_item_history))
        mBinding.tabItemLearn.text =
            spannableItem(data.learnMinutes, getString(R.string.profile_tab_item_history))

        if (data.isLogin) {
            mBinding.ivUser.loadCircle(data.avatar)
        } else {
            mBinding.ivUser.setImageResource(R.mipmap.ic_launcher)
            mBinding.ivUser.setOnClickListener {
                AccountManager.login(context) {
                    queryUserDetail()
                }
            }
        }
        updateBanner(data.bannerNoticeList)
    }

    private fun updateBanner(bannerNoticeList: List<NoticeInfo>?) {
        if (bannerNoticeList.isNullOrEmpty()) return
        val models = bannerNoticeList.map {
            val mo = object : HiBannerMo() {}
            mo.url = it.cover
            mo
        }
        mBinding.banner.apply {
            setBannerData(R.layout.item_profile_banner_item, models)
            setBindAdapter { holder, mo, pos ->
                if (holder == null || mo == null) return@setBindAdapter
                val view = holder.findViewById<ImageView>(R.id.iv_image)
                view.loadCorner(mo.url, HiDisplayUtils.dip2px(10f))
            }
            visibility = View.VISIBLE
            setOnBannerClickListener { viewHolder, bannerMo, pos ->
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(models[pos].url)))
            }
        }
    }

    private fun spannableItem(count: Int, bottom: String): CharSequence {
        val cou = count.toString()
        val ssb = SpannableStringBuilder()
        val ss = SpannableString(cou)
        /*变色*/
        ss.setSpan(ForegroundColorSpan(resources.getColor(R.color.color_000)),
            0,
            ss.length,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        ss.setSpan(AbsoluteSizeSpan(18, true), 0, ss.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        ss.setSpan(StyleSpan(Typeface.BOLD), 0, ss.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        ssb.append(ss).append(bottom)
        return ssb
    }
}