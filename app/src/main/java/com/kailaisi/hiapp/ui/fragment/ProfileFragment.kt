package com.kailaisi.hiapp.ui.fragment

import android.graphics.Typeface
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
import com.kailaisi.common.HiRoute
import com.kailaisi.common.ui.component.HiBaseFragment
import com.kailaisi.common.ui.view.loadCircle
import com.kailaisi.common.ui.view.loadCorner
import com.kailaisi.hi_ui.banner.core.HiBannerMo
import com.kailaisi.hiapp.R
import com.kailaisi.library.util.HiDisplayUtils
import com.kailaisi.library.util.HiRes
import com.kailaisi.service_login.LoginServiceProvider
import com.kailaisi.service_login.NoticeInfo
import com.kailaisi.service_login.UserProfile
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * 描述：
 *
 * 作者：kailaisi
 * <br></br>创建时间：2021-05-15:17:50
 */
class ProfileFragment : HiBaseFragment() {
    private val REQUEST_LOGIN = 1

    override fun getLayoutId(): Int {
        return R.layout.fragment_profile
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notify_title.text = getString(R.string.item_notify)

        notify_collection.text = getString(R.string.item_collection)

        notify_history.text = getString(R.string.item_history)

        notify_location.text = getString(R.string.item_location)

        ll_notify.setOnClickListener {
            ARouter.getInstance().build("/notice/list").navigation(context)
        }
        queryCourseNotice()

        queryUserDetail()
    }

    private fun queryCourseNotice() {
    }

    private fun queryUserDetail() {
        LoginServiceProvider.getUserProfile(this, Observer {
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
        user_name.text =
            if (data.isLogin) data.userName else getString(R.string.profile_not_login)
        tv_login_des.text =
            if (data.isLogin) getString(R.string.profile_login_desc_welcome_back) else getString(R.string.profile_login_desc)
        tab_item_collection.text =
            spannableItem(data.favoriteCount, getString(R.string.profile_tab_item_collection))
        tab_item_history.text =
            spannableItem(data.brrowseCount, getString(R.string.profile_tab_item_history))
        tab_item_learn.text =
            spannableItem(data.learnMinutes, getString(R.string.profile_tab_item_history))

        if (data.isLogin) {
            iv_user.loadCircle(data.avatar)
        } else {
            iv_user.setImageResource(R.mipmap.ic_launcher)
            iv_user.setOnClickListener {
                LoginServiceProvider.login(context, Observer {
                    queryUserDetail()
                })
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
        banner.apply {
            setBannerData(R.layout.item_profile_banner_item, models)
            setBindAdapter { holder, mo, pos ->
                if (holder == null || mo == null) return@setBindAdapter
                val view = holder.findViewById<ImageView>(R.id.iv_image)
                view.loadCorner(mo.url, HiDisplayUtils.dip2px(10f))
            }
            visibility = View.VISIBLE
            setOnBannerClickListener { _, bannerMo, _ ->
                HiRoute.startActivity4Browser(bannerMo.url)
            }
        }
    }

    private fun spannableItem(count: Int, bottom: String): CharSequence {
        val cou = count.toString()
        val ssb = SpannableStringBuilder()
        val ss = SpannableString(cou)
        /*变色*/
        ss.setSpan(
            ForegroundColorSpan(HiRes.getColor(R.color.color_000)),
            0,
            ss.length,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
        ss.setSpan(AbsoluteSizeSpan(18, true), 0, ss.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        ss.setSpan(StyleSpan(Typeface.BOLD), 0, ss.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        ssb.append(ss).append(bottom)
        return ssb
    }
}