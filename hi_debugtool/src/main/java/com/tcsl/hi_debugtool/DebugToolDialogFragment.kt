package com.tcsl.hi_debugtool

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kailaisi.common.tab.HiTabViewAdapter
import com.kailaisi.library.util.HiDisplayUtils
import java.lang.reflect.Method

class DebugToolDialogFragment : DialogFragment() {
    lateinit var containerView: View
    private val debugTools = arrayOf(DebugTools::class.java)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val parent = dialog?.window?.findViewById(android.R.id.content) ?: container

        containerView = inflater.inflate(R.layout.dialog_debug_tool, parent, false)
        dialog?.window?.setLayout((HiDisplayUtils.getScreenWidth(view?.context) * 0.7f).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT)

        dialog?.window?.setBackgroundDrawableResource(R.drawable.shape_hi_debug_tool)
        return containerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemDecoration = DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(view.context,
            R.drawable.shape_hi_debug_divider)!!)
        val size = debugTools.size
        val functions = mutableListOf<DebugFunction>()
        for (index in 0 until size) {
            val clazz = debugTools[index]
            val target = clazz.getConstructor().newInstance()
            val declaredMethods = target.javaClass.declaredMethods
            for (method in declaredMethods) {
                var title = ""
                var desc = ""
                var enable = false
                val annotation = method.getAnnotation(HiDebug::class.java)
                if (annotation != null) {
                    title = annotation.name
                    desc = annotation.desc
                    enable = true
                } else {
                    method.isAccessible = true
                    title = method.invoke(target) as String
                }
                val func = DebugFunction(title, desc, enable, method, target)
                functions.add(func)
            }
        }
        val recyclerView = containerView.findViewById<RecyclerView>(R.id.rv_item)
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.adapter = DebugToolAdapter(functions)

    }

    class DebugToolAdapter(val list: List<DebugFunction>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflate = LayoutInflater.from(parent.context)
                .inflate(R.layout.hi_debug_tool_item, parent, false)
            return object : RecyclerView.ViewHolder(inflate) {}
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val itemTitle = holder.itemView.findViewById<TextView>(R.id.item_title)
            val itemDesc = holder.itemView.findViewById<TextView>(R.id.item_desc)
            val item = list[position]
            itemTitle.text = item.name
            if (item.desc.isNullOrBlank()){
                itemDesc.visibility=View.GONE
            }else {
                itemDesc.visibility=View.VISIBLE
                itemDesc.text = item.desc
            }

            if (item.enable){
                holder.itemView.setOnClickListener {
                    item.invoke()
                }
            }
        }

        override fun getItemCount(): Int {
            return list.size
        }

    }

    data class DebugFunction(
        val name: String,
        val desc: String,
        val enable: Boolean,
        var method: Method,
        var target: Any,
    ) {
        fun invoke() {
            method.invoke(target)
        }
    }
}