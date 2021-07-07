package com.kailaisi.hiapp.ui.fragment

import android.os.Bundle
import android.view.View
import com.kailaisi.common.HiRoute
import com.kailaisi.common.ui.component.HiBaseFragment
import com.kailaisi.hiapp.R

/**
 * 描述：
 *
 * 作者：kailaisi
 * <br></br>创建时间：2021-05-15:17:50
 */
class FavoriteFragment : HiBaseFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutView.findViewById<View>(R.id.tv).setOnClickListener {
            val bundle = Bundle()
            HiRoute.startActivity(context, bundle,"/order/detail")
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_favorite
    }
}