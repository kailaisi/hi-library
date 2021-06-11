package com.tcsl.hi_debugtool

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.kailaisi.library.util.HiDisplayUtils
import java.lang.reflect.Method

class DebugToolDialogFragment : DialogFragment() {
    private val debugTools = arrayOf(DebugTools::class.java)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val parent = dialog?.window?.findViewById(android.R.id.content) ?: container

        val view = inflater.inflate(R.layout.dialog_debug_tool, parent, false)
        dialog?.window?.setLayout((HiDisplayUtils.getScreenWidth(view.context) * 0.7f).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT)

        dialog?.window?.setBackgroundDrawableResource(R.drawable.shape_hi_debug_tool)
        return view
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
    }

    data class DebugFunction(
        val name: String,
        val desc: String,
        val enable: Boolean,
        var method: Method,
        var target: Any,
    )
}