package com.kailaisi.biz_order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.kailaisi.common.HiRoute
import com.kailaisi.common.ui.view.load
import com.kailaisi.library.util.HiStatusBar
import kotlinx.android.synthetic.main.activity_order.*

/**
 * 订单页面
 */

@Route(path = "/order/detail")
class OrderActivity : AppCompatActivity() {

    @Autowired
    @JvmField
    var shopLogo: String? = null

    @Autowired
    @JvmField
    var shopName: String? = null

    @Autowired
    @JvmField
    var goodsName: String? = null

    @Autowired
    @JvmField
    var goodsImage: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiStatusBar.setStatusBar(this, true, translucent = false)
        HiRoute.inject(this)
        setContentView(R.layout.activity_order)
        initView()
    }

    private fun initView() {
        nav_bar.setNavListener { onBackPressed() }
        shopLogo?.apply { shop_logo.load(this) }
        shop_name.text = shopName
        goodsImage?.apply { goods_logo.load(this) }
    }
}