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
import com.kailaisi.hiapp.R
import com.kailaisi.hiapp.databinding.ActivityDetailBinding
import com.kailaisi.hiapp.databinding.ActivityGoodsListBinding
import com.kailaisi.hiapp.databinding.ActivityProfileDetailBinding
import com.kailaisi.hiapp.model.DetailModel
import com.kailaisi.hiapp.model.GoodsModel
import com.kailaisi.library.util.HiStatusBar
import com.kailaisi.library.util.inflate

@Route(path = "/detail/main")
class DetailActivity : HiBaseActivity() {
    private var emptyView: EmptyView? = null
    lateinit var viewModel:DetailViewModel
    @JvmField
    @Autowired
    var goodsId: String? = null

    var goodsModel: GoodsModel? = null

    val mBinding: ActivityDetailBinding by inflate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiStatusBar.setStatusBar(this, true, Color.TRANSPARENT, true)
        HiRoute.inject(this)
        assert(goodsId.isNullOrBlank()) { "goodsId must be not null" }
        setContentView(mBinding.root)
        initView()
        viewModel = DetailViewModel.get(goodsId, this)
        viewModel.queryDetailData().observe(this, Observer {
            if (it==null){
                showEmptyView()
            }else{
                bindData(it)
            }

        })
    }

    private fun bindData(it: DetailModel) {
    }

    private fun showEmptyView() {
        if (emptyView==null){
            emptyView=EmptyView(this).apply {
                setIcon(R.string.if_empty3)
                setDesc(getString(R.string.list_empty_desc))
                layoutParams=ConstraintLayout.LayoutParams(-1,-1)
                setBackgroundColor(Color.WHITE)
                setButton(getString(R.string.list_empty_action), View.OnClickListener {
                    viewModel.queryDetailData()
                })
                mBinding.root.addView(emptyView)
            }
        }
        mBinding.rvItem.visibility=View.GONE
        emptyView?.visibility=View.VISIBLE
    }

    private fun initView() {
        mBinding.ivBack.setOnClickListener { onBackPressed() }
        mBinding.rvItem.layoutManager = GridLayoutManager(this, 2)
        mBinding.rvItem.adapter = HiAdapter(this)
    }
}