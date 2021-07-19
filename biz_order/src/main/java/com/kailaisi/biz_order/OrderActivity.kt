package com.kailaisi.biz_order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.kailaisi.biz_order.address.Address
import com.kailaisi.common.HiRoute
import com.kailaisi.common.ui.view.load
import com.kailaisi.library.util.HiStatusBar
import com.kailaisi.pub_mod.RouterFlag
import kotlinx.android.synthetic.main.activity_order.*

/**
 * 订单页面
 */

@Route(path = "/order/detail",extras = RouterFlag.FLAG_LOGIN)
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

    @Autowired
    @JvmField
    var goodsPrice: String? = null

    private val viewModel by viewModels<OrderViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiStatusBar.setStatusBar(this, true, translucent = false)
        HiRoute.inject(this)
        setContentView(R.layout.activity_order)
        initView()
        updateTotalPayPrice(amount_view.gtAmountValue())
        viewModel.queryMainAddress().observe(this, Observer {
            updateAddress(it)
        })
    }

    private fun updateAddress(it: Address?) {
        val hasMain=it?.receiver.isNullOrBlank()
    }

    private fun initView() {
        nav_bar.setNavListener { onBackPressed() }
        shopLogo?.apply { shop_logo.load(this) }
        shop_name.text = shopName
        goodsImage?.apply { goods_logo.load(this) }
        goods_title.text = goodsName
        goods_price.text = goodsPrice
        amount_view.setAmountValueChangedListener {
            updateTotalPayPrice(it)
        }
    }

    /**
     * 更新底部总价格
     */
    private fun updateTotalPayPrice(amount: Int) {
        val subPrice = PriceUtil.subPrice(goodsPrice)
        subPrice?.let {
            total_pay_price.text =
                "￥".plus(amount * subPrice.toFloat()) + getString(R.string.free_transport)
        }
    }
}