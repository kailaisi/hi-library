package com.kailaisi.library.fps

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView
import com.kailaisi.library.R
import com.kailaisi.library.log.HiLog
import com.kailaisi.library.util.ActivityManager
import com.kailaisi.library.util.AppGlobals
import java.text.DecimalFormat

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2021-07-22:21:05
 */
object FpsMonitor {
    private val fpsViewer = FpsViewer()
    fun toggle() {
        fpsViewer.toggle()
    }

    fun listener(listener: FpsCallback) {
        fpsViewer.addListener(listener)
    }

    private class FpsViewer {
        private var params = WindowManager.LayoutParams()
        private var isPlaying = false
        private val application = AppGlobals.get()!!
        private var fpsView =
            LayoutInflater.from(application).inflate(R.layout.fps_view, null, false) as TextView
        private val decimal = DecimalFormat("#.0 fps")
        private var wm: WindowManager? = null
        private val frameMonitor = FrameMonitor()

        init {
            wm = application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            params.width = WindowManager.LayoutParams.WRAP_CONTENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT

            params.flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            params.format = PixelFormat.TRANSLUCENT
            params.gravity = Gravity.RIGHT or Gravity.TOP
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                params.type = WindowManager.LayoutParams.TYPE_TOAST
            }
            ActivityManager.instance.addFrontBackCallback(object :
                ActivityManager.FrontBackCallback {
                override fun onChanged(front: Boolean) {
                    if (front) {
                        play()
                    } else {
                        stop()
                    }
                }
            })
            frameMonitor.addListener(object : FpsCallback {
                override fun onFrame(fps: Double) {
                    fpsView.text = decimal.format(fps)
                }
            })
        }

        private fun stop() {
            frameMonitor.stop()
            if (isPlaying) {
                wm?.removeView(fpsView)
                isPlaying = false
            }
        }

        private fun play() {
            if (!hasOverlayPermission()) {
                HiLog.e("没有悬浮窗权限")
                startOverlaySettingActivity()
                return
            }
            frameMonitor.start()
            if (!isPlaying) {
                isPlaying = true
                wm?.addView(fpsView, params)
            }
        }

        //引导开启悬浮权限
        private fun startOverlaySettingActivity() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                application.startActivity(
                    Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + application.packageName)
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }

        private fun hasOverlayPermission(): Boolean {
            return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(
                application
            )
        }

        fun toggle() {
            if (isPlaying) {
                stop()
            } else {
                play()
            }
        }

        fun addListener(listener: FpsCallback) {
            frameMonitor.addListener(listener)
        }
    }

    interface FpsCallback {
        fun onFrame(fps: Double)
    }
}