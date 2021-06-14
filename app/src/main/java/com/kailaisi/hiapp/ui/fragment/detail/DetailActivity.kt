package com.kailaisi.hiapp.ui.fragment.detail

import android.graphics.Color
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.kailaisi.common.HiRoute
import com.kailaisi.common.ui.component.HiBaseActivity
import com.kailaisi.hiapp.R
import com.kailaisi.hiapp.databinding.ActivityDetailBinding
import com.kailaisi.hiapp.databinding.ActivityGoodsListBinding
import com.kailaisi.hiapp.databinding.ActivityProfileDetailBinding
import com.kailaisi.hiapp.model.GoodsModel
import com.kailaisi.library.util.HiStatusBar
import com.kailaisi.library.util.inflate

@Route(path = "/detail/main")
class DetailActivity : HiBaseActivity() {
    @JvmField
    @Autowired
    var goodsId: String? = null

    var goodsModel: GoodsModel? = null

    val mBinding: ActivityDetailBinding by inflate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiStatusBar.setStatusBar(this, true, Color.TRANSPARENT, true)
        HiRoute.inject(this)
        assert(goodsId.isNullOrBlank()){"goodsId must be not null"}
        setContentView(mBinding.root)
    }
}