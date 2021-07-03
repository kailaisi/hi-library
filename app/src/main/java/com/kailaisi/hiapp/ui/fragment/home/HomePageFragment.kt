package com.kailaisi.hiapp.ui.fragment.home

import android.os.Bundle
import android.util.SparseArray
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.kailaisi.common.ui.component.HiBaseFragment
import com.kailaisi.hi_ui.tab.common.IHiTabLayout
import com.kailaisi.hi_ui.tab.top.HiTabTopInfo
import com.kailaisi.hiapp.R
import com.kailaisi.hiapp.databinding.FragmentHomeBinding
import com.kailaisi.hiapp.model.TabCategory
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
        val viewModel = ViewModelProvider(this).get(HomePageViewModel::class.java)
        viewModel.queryTabList().observe(viewLifecycleOwner, Observer {
            it?.let { updateUI(it) }
        })
        mBinding.titleBar.apply {
            setNavListener(View.OnClickListener { activity?.finish() })
            addRightTextButton("111", View.generateViewId())
            addRightTextButton("222", View.generateViewId())
        }
    }


    private val onTabSelectedListener =
        IHiTabLayout.OnTabSelectedListener<HiTabTopInfo<*>> { index, _, _ ->
            if (mBinding.viewpager.currentItem != index) {
                mBinding.viewpager.setCurrentItem(index, false)
            }
            selectTabIndex = index
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
            mBinding.tabTopLayout.addTabSelectedChangedListener(onTabSelectedListener)
            if (viewpager.adapter == null) {
                viewpager.adapter = HomePageAdapter(
                    childFragmentManager,
                    FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                )
            }
            (viewpager.adapter as HomePageAdapter).update(data)
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

    inner class HomePageAdapter(fm: FragmentManager, behavior: Int) :
        FragmentPagerAdapter(fm, behavior) {
        val data = mutableListOf<TabCategory>()

        private val fragments = SparseArray<Fragment>(data.size)

        override fun getCount(): Int {
            return fragments.size()
        }

        override fun getItem(position: Int): Fragment {
            /*这里使用id来进行缓存，防止页面刷新之后，position是一样的，但是categoryId不一致，导致的复用问题*/
            val categoryId = data[position].categoryId.toInt()
            val fragment = fragments.get(categoryId, null)
            if (fragment == null) {
                HomeTabFragment.newInstance(data[position].categoryId)
                fragments.put(categoryId, fragment)
            }
            return fragment
        }

        override fun getItemPosition(`object`: Any): Int {
            /*关于object位置的信息是否需要刷新，要判断，刷新前后数据是否发生了变化*/
            val indexOfValue = fragments.indexOfValue(`object` as Fragment)
            val item = getItem(indexOfValue)
            return if (item == `object`) PagerAdapter.POSITION_NONE else PagerAdapter.POSITION_UNCHANGED
        }

        override fun getItemId(position: Int): Long {
            return data[position].categoryId.toLong()
        }

        fun update(list: List<TabCategory>) {
            data.clear()
            data.addAll(list)
            notifyDataSetChanged()
        }

    }
}
