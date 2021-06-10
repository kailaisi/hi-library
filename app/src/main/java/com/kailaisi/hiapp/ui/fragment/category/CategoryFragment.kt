package com.kailaisi.hiapp.ui.fragment.category

import android.graphics.Color
import android.os.Bundle
import android.util.SparseIntArray
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.kailaisi.common.ui.component.HiBaseFragment
import com.kailaisi.common.ui.view.EmptyView
import com.kailaisi.common.ui.view.load
import com.kailaisi.hiapp.R
import com.kailaisi.hiapp.databinding.FragmentCategoryBinding
import com.kailaisi.hiapp.http.ApiFactory
import com.kailaisi.hiapp.http.api.CategoryApi
import com.kailaisi.hiapp.model.Subcategory
import com.kailaisi.hiapp.model.TabCategory
import com.kailaisi.library.restful.HiCallback
import com.kailaisi.library.restful.HiResponse
import com.kailaisi.library.util.bindView

/**
 * 描述：
 *
 * 作者：kailaisi
 * <br></br>创建时间：2021-05-15:17:50
 */
class CategoryFragment : HiBaseFragment() {
    private val SPAN_COUNT: Int = 3
    val mBinding: FragmentCategoryBinding by bindView()
    var emptyView: EmptyView? = null
    val subCategoryCache= mutableMapOf<String,List<Subcategory>>()

    override fun getLayoutId(): Int {
        return R.layout.fragment_category
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        queryCategoryList()
    }

    private fun queryCategoryList() {
        ApiFactory.create(CategoryApi::class.java).queryCategoryList()
            .enqueue(object : HiCallback<List<TabCategory>> {
                override fun onSuccess(response: HiResponse<List<TabCategory>>) {
                    if (response.sucessful() && response.data != null) {
                        onQueryCategoryListSuccess(response.data!!)
                    } else {
                        showEmptyView()
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    showEmptyView()
                }

            })
    }

    private fun onQueryCategoryListSuccess(data: List<TabCategory>) {
        if (isAlive) {
            emptyView?.visibility = View.GONE
            mBinding.slideView.apply {
                visibility = View.VISIBLE
                bindMenuView(itemCount = data.size, onBindView = { holder, position ->
                    val tabCategory = data[position]
                    holder.findViewById<TextView>(R.id.menu_item_text)?.text =
                        tabCategory.categoryName
                }, onItemClick = { holder, position ->
                    val key = data[position].categoryId
                    if (subCategoryCache.containsKey(key)){
                        onQuerySubCategoryListSuccess(subCategoryCache[key]!!)
                    }else {
                        querySubCategoryList(key)
                    }
                })
            }

        }
    }

    /*查询右侧数据*/
    private fun querySubCategoryList(id: String) {
        ApiFactory.create(CategoryApi::class.java).querySubCategoryList(id)
            .enqueue(object : HiCallback<List<Subcategory>> {
                override fun onSuccess(response: HiResponse<List<Subcategory>>) {
                    if (response.sucessful() && response.data != null) {
                        if (!subCategoryCache.containsKey(id)){
                            subCategoryCache.put(id,response.data!!)
                        }
                        onQuerySubCategoryListSuccess(response.data!!)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    showEmptyView()
                }

            })
    }


    private val layoutManager = GridLayoutManager(context, SPAN_COUNT)

    private val decoration =
        CategoryItemDecoration({ postion: Int -> subCategoryList[postion].groupName }, SPAN_COUNT)
    private val subCategoryList = mutableListOf<Subcategory>()
    private val groupSpanSizeOffset = SparseIntArray()
    private val spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            var spanSize = 1
            val groupName = subCategoryList[position].groupName
            val nextGroupName =
                if (position + 1 < subCategoryList.size) subCategoryList[position + 1].groupName else null
            if (groupName == nextGroupName) {
                spanSize = 1
            } else {
                //当前位置和下一个位置，不在同一个分组
                //拿到当前组position在offset中的索引下标
                val indexOfKey = groupSpanSizeOffset.indexOfKey(position)
                val size = groupSpanSizeOffset.size()
                //拿到前一组中保存的offset偏移量
                val lastGroupOffset = if (size <= 0) {
                    0
                } else if (indexOfKey >= 0) {
                    //当前偏移量已经记录了，是发生在上下滑动
                    if (indexOfKey == 0) 0 else groupSpanSizeOffset[indexOfKey - 1]
                } else {
                    //当前组的偏移量记录，还没有存在，第一次布局的时候会发生
                    val valueAt = groupSpanSizeOffset.valueAt(size - 1)
                    valueAt
                }
                //给当前组设置spansize，
                spanSize = SPAN_COUNT - (position + lastGroupOffset) % 3

                if (indexOfKey < 0) {
                    //得到当前组和前面组的所有的spansize的偏移量之和
                    val groupOffset = lastGroupOffset + spanSize - 1
                    groupSpanSizeOffset.put(position, groupOffset)
                }
            }
            return spanSize
        }

    }

    private fun onQuerySubCategoryListSuccess(data: List<Subcategory>) {
        if (isAlive) {
            groupSpanSizeOffset.clear()
            decoration.clear()
            subCategoryList.clear()
            subCategoryList.addAll(data)
            if (layoutManager.spanSizeLookup != spanSizeLookup) {
                layoutManager.spanSizeLookup = spanSizeLookup
            }

            mBinding.slideView.apply {
                visibility = View.VISIBLE
                bindContentView(
                    itemDecoration = decoration,
                    itemCount = data.size,
                    layoutManager = layoutManager,
                    onBindView = { holder, position ->
                        val tabCategory = data[position]
                        holder.findViewById<ImageView>(R.id.content_item_image)
                            ?.load(tabCategory.subcategoryIcon)
                        holder.findViewById<TextView>(R.id.content_item_title)?.text =
                            tabCategory.subcategoryName
                    }, onItemClick = { holder, position ->
                        val tabCategory = data[position]
                        Toast.makeText(context, tabCategory.subcategoryName, Toast.LENGTH_SHORT)
                            .show()
                    })
            }

        }
    }

    private fun showEmptyView() {
        if (isAlive) {
            if (emptyView == null) {
                emptyView = EmptyView(requireContext()).apply {
                    setIcon(R.string.if_empty3)
                    setDesc(getString(R.string.list_empty_desc))
                    setButton(getString(R.string.list_empty_action), View.OnClickListener {
                        queryCategoryList()
                    })
                    setBackgroundColor(Color.WHITE)
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                }
            }
        }
        mBinding.slideView.visibility = View.GONE
        emptyView?.visibility = View.VISIBLE
    }
}