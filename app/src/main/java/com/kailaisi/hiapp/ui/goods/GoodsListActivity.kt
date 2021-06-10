package com.kailaisi.hiapp.ui.goods

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.kailaisi.common.ui.component.HiBaseActivity
import com.kailaisi.hiapp.R
import com.kailaisi.hiapp.databinding.ActivityGoodsListBinding
import com.kailaisi.library.util.HiStatusBar
import com.kailaisi.library.util.inflate

/**
 * 列表页
 */
@Route(path = "/goods/list")
class GoodsListActivity : HiBaseActivity() {
    val mBinding:ActivityGoodsListBinding by inflate()
    @Autowired
    @JvmField
    var categoryTitle: String = ""

    @Autowired
    @JvmField
    var categoryId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        HiStatusBar.setStatusBar(this, true)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods_list)
        ARouter.getInstance().inject(this)

    }

}