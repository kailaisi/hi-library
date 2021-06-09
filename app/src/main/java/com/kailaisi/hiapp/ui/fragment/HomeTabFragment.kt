package com.kailaisi.hiapp.ui.fragment

import android.os.Bundle
import com.kailaisi.common.ui.component.HiAbsListFragment

/**
 * 首页的tab对应的Fragment
 */
class HomeTabFragment : HiAbsListFragment() {
    companion object {
        fun newInstance(beanId: String): HomeTabFragment {
            val args = Bundle()
            val fragment = HomeTabFragment()
            args.putString("id", beanId)
            fragment.arguments = args
            return fragment
        }
    }
}