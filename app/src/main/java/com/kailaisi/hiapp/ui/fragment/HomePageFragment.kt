package com.kailaisi.hiapp.ui.fragment

import android.os.Bundle
import android.util.SparseArray
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.kailaisi.common.ui.component.HiBaseFragment
import com.kailaisi.hi_ui.tab.top.HiTabTopInfo
import com.kailaisi.hiapp.R
import com.kailaisi.hiapp.databinding.FragmentHomeBinding
import com.kailaisi.hiapp.http.ApiFactory
import com.kailaisi.hiapp.http.api.HomeApi
import com.kailaisi.hiapp.model.TabCategory
import com.kailaisi.library.restful.HiCallback
import com.kailaisi.library.restful.HiResponse
import com.kailaisi.library.util.bindView

/**
 * 描述：
 *
 * 作者：kailaisi
 * <br></br>创建时间：2021-05-15:17:50
 */
class HomePageFragment : HiBaseFragment() {
    private var selectTabIndex: Int = 0
    val mBinding by bindView<FragmentHomeBinding>()
    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        queryTabList()
    }

    private fun queryTabList() {
        ApiFactory.create(HomeApi::class.java).queryTabList()
            .enqueue(object : HiCallback<List<TabCategory>> {
                override fun onSuccess(response: HiResponse<List<TabCategory>>) {
                    val data = response.data
                    if (response.sucessful() && data != null) {
                        updateUI(data)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                }

            })
    }

    private fun updateUI(data: List<TabCategory>) {
        //需要注意是否已经销毁了
        if (isAlive) {
            val defaultColor = ContextCompat.getColor(requireContext(), R.color.color_333)
            val selectColor = ContextCompat.getColor(requireContext(), R.color.color_dd2)
            val list = data.map {
                HiTabTopInfo(it.categoryName, defaultColor, selectColor)
            }
            mBinding.tabTopLayout.inflateInfo(list)
            mBinding.tabTopLayout.defaultSelected(list[selectTabIndex])
            val viewpager = mBinding.viewpager
            mBinding.tabTopLayout.addTabSelectedChangedListener { index, preInfo, nextInfo ->
                if (viewpager.currentItem != index) {
                    viewpager.setCurrentItem(index, false)
                }
                selectTabIndex = index
            }
            viewpager.adapter = HomePageAdapter(childFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                data)
            viewpager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    //区分两种情况，1。顶部点击切换，2。滑动切换
                    if (position != selectTabIndex) {
                        mBinding.tabTopLayout.defaultSelected(list[position])
                        selectTabIndex = position
                    }
                }
            })
        }

    }

    inner class HomePageAdapter(fm: FragmentManager, behavior: Int, val data: List<TabCategory>) :
        FragmentPagerAdapter(fm, behavior) {
        val fragments = SparseArray<Fragment>(data.size)
        override fun getCount(): Int {
            return fragments.size()
        }

        override fun getItem(position: Int): Fragment {
            val fragment = fragments.get(position, null)
            if (fragment == null) {
                HomeTabFragment.newInstance(data[position].categoryId)
                fragments.put(position, fragment)
            }
            return fragment
        }

    }
}
