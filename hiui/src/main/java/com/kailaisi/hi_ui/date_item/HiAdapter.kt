package com.kailaisi.hi_ui.date_item

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.RuntimeException
import java.lang.reflect.ParameterizedType

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2021-05-27:21:50
 */
class HiAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var dataSet = ArrayList<HiDataItem<*, RecyclerView.ViewHolder>>()

    private var typeArray = SparseArray<HiDataItem<*, RecyclerView.ViewHolder>>()

    init {
        this.mContext = context
        this.mInflater = LayoutInflater.from(context)
    }

    fun addItem(index:Int,item: HiDataItem<*,RecyclerView.ViewHolder> ,notify:Boolean){
        if (index>0){
            dataSet.add(index,item)
        }else{
            dataSet.add(item)
        }

        val notifyPos=if (index>0) index else dataSet.size-1
        if (notify){
            notifyItemInserted(notifyPos)
        }
    }

    fun addItems(items:List<HiDataItem<*,RecyclerView.ViewHolder>>,notify: Boolean){
        val start=dataSet.size
        for (item in items){
            dataSet.add(item)
        }
        if (notify){
            notifyItemRangeChanged(start,items.size)
        }
    }

    fun remove(index: Int): HiDataItem<*, RecyclerView.ViewHolder>? {
        if (index>0 && index<dataSet.size){
            val removeAt = dataSet.removeAt(index)
            notifyItemRemoved(index)
            return removeAt
        }
        return null
    }
    fun remove(data:HiDataItem<*, *>?) {
        if (data!=null) {
            val indexOf = dataSet.indexOf(data)
            remove(indexOf)
        }
    }
    override fun getItemCount(): Int {
        return dataSet.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val dataItem = typeArray.get(viewType)
        var itemView = dataItem.getItemView(parent)
        if (itemView==null){
            var layout = dataItem.getItemLayoutRes()
            if (layout<0){
                throw  RuntimeException("dataItem:${dataItem.javaClass.name} must override getItemView or getItemLayoutRes")
            }
            itemView = mInflater!!.inflate(layout, parent, false)
        }
        return createViewHolderInternal(dataItem.javaClass,itemView)
    }

    private fun createViewHolderInternal(
        javaClass: Class<HiDataItem<*, RecyclerView.ViewHolder>>,
        itemView: View?
    ): RecyclerView.ViewHolder {
        val superclass = javaClass.genericSuperclass
        if (superclass is ParameterizedType){
            val arguments = superclass.actualTypeArguments
            arguments.forEach {
                if (it is Class<*> && RecyclerView.ViewHolder::class.java.isAssignableFrom(it)){
                    return it.getConstructor(View::class.java).newInstance(itemView) as RecyclerView.ViewHolder
                }
            }
        }
        return object:RecyclerView.ViewHolder(itemView!!){}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        dataSet[position].onBindData(holder,position)
    }



    override fun getItemViewType(position: Int): Int {
        val type= dataSet[position].javaClass.hashCode()
        if (typeArray.indexOfKey(type)<0){
            typeArray.put(type,dataSet[position])
        }
        return type
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager){
            val spanCount = layoutManager.spanCount
            layoutManager.spanSizeLookup=object :GridLayoutManager.SpanSizeLookup(){
                override fun getSpanSize(position: Int): Int {
                    if (position<dataSet.size){
                        val spanSize = dataSet[position].getSpanSize()
                        return if (spanSize<=0) spanCount else spanSize
                    }
                    return spanCount
                }

            }
        }
    }

    fun refreshItem(hiDataItem: HiDataItem<*,*>) {
        val indexOf = dataSet.indexOf(hiDataItem)
        notifyItemChanged(indexOf)
    }
}