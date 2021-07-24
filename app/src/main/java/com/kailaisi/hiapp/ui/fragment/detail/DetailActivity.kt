package com.kailaisi.hiapp.ui.fragment.detail

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.kailaisi.common.HiRoute
import com.kailaisi.common.ui.component.HiBaseActivity
import com.kailaisi.common.ui.view.EmptyView
import com.kailaisi.hi_ui.date_item.HiAdapter
import com.kailaisi.hi_ui.date_item.HiDataItem
import com.kailaisi.hiapp.R
import com.kailaisi.hiapp.model.DetailModel
import com.kailaisi.library.util.HiRes
import com.kailaisi.library.util.HiStatusBar
import com.kailaisi.pub_mod.GoodsModel
import com.kailaisi.pub_mod.selectPrice
import kotlinx.android.synthetic.main.activity_detail.*

@Route(path = "/detail/main")
class DetailActivity : HiBaseActivity() {
    private var emptyView: EmptyView? = null
    lateinit var viewModel: DetailViewModel

    @JvmField
    @Autowired
    var goodsId: String? = null

    var goodsModel: GoodsModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        HiStatusBar.setStatusBar(this, true, Color.TRANSPARENT, true)
        HiRoute.inject(this)
        assert(goodsId.isNullOrBlank()) { "goodsId must be not null" }
        initView()
        preBindData()
        viewModel = DetailViewModel.get(goodsId, this)
        viewModel.queryDetailData().observe(this, Observer {
            if (it == null) {
                showEmptyView()
            } else {
                bindData(it)
            }
        })
    }

    private fun preBindData() {
        goodsModel?.also {
            val hiAdapter = rv_item.adapter as HiAdapter
            hiAdapter.addItemAt(
                0, HeaderItem(
                    it.sliderImages,
                    selectPrice(it.groupPrice, it.marketPrice),
                    it.completedNumText,
                    it.goodsName
                ), false
            )
        }

    }

    private fun bindData(detailModel: DetailModel) {
        rv_item.visibility = View.VISIBLE
        emptyView?.visibility = View.GONE
        val hiAdapter = rv_item.adapter as HiAdapter
        val dataItems = mutableListOf<HiDataItem<*, *>>()
        //头部item
        dataItems.add(
            HeaderItem(
                detailModel.sliderImages,
                selectPrice(detailModel.groupPrice, detailModel.marketPrice),
                detailModel.completedNumText,
                detailModel.goodsName
            )
        )
        //评论item
        dataItems.add(CommentItem(detailModel))
        //店铺
        dataItems.add(ShopItem(detailModel))
        //商品描述
        //图库
        //相似商品
        hiAdapter.clearItems()
        hiAdapter.addItems(dataItems, true)
    }

    private fun showEmptyView() {
        if (emptyView == null) {
            emptyView = EmptyView(this).apply {
                setIcon(R.string.if_empty3)
                setDesc(getString(R.string.list_empty_desc))
                layoutParams = ConstraintLayout.LayoutParams(-1, -1)
                setBackgroundColor(HiRes.getColor(R.color.white))
                setButton(getString(R.string.list_empty_action), View.OnClickListener {
                    viewModel.queryDetailData()
                })
                root.addView(emptyView)
            }
        }
        rv_item.visibility = View.GONE
        emptyView?.visibility = View.VISIBLE
    }

    private fun initView() {
        iv_back.setOnClickListener { onBackPressed() }
        rv_item.layoutManager = GridLayoutManager(this, 2)
        rv_item.adapter = HiAdapter(this)
    }
}