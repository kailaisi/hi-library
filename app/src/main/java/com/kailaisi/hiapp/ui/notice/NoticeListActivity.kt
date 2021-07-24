package com.kailaisi.hiapp.ui.notice

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.kailaisi.common.http.ApiFactory
import com.kailaisi.common.ui.component.HiBaseActivity
import com.kailaisi.hi_ui.date_item.HiAdapter
import com.kailaisi.hiapp.R
import com.kailaisi.hiapp.http.api.NoticeApi
import com.kailaisi.hiapp.model.CourseNotice
import com.kailaisi.library.restful.HiCallback
import com.kailaisi.library.restful.HiResponse
import com.kailaisi.library.util.HiStatusBar
import kotlinx.android.synthetic.main.activity_notice_list.*

/**
 * 通知列表
 */
@Route(path = "/notice/list")
class NoticeListActivity : HiBaseActivity() {
    private lateinit var adapter: HiAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiStatusBar.setStatusBar(this, true, translucent = false)
        setContentView(R.layout.activity_notice_list)
        action_back.setOnClickListener { onBackPressed() }
        initView()
    }

    private fun queryCourseNotice() {
        ApiFactory.create(NoticeApi::class.java).notice()
            .enqueue(object : HiCallback<CourseNotice> {
                override fun onSuccess(response: HiResponse<CourseNotice>) {
                    response.data?.let {
                        bindData(it)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun bindData(data: CourseNotice) {
        val items = data.list?.map {
            adapter.addItemAt(0, NoticeItem(it), true)
        }

    }

    private fun initView() {
        adapter = HiAdapter(this)
        rv_item.adapter = adapter
    }
}