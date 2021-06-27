package com.kailaisi.common.ui.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.kailaisi.hi_ui.tab.bottom.HiViewUtil
import java.security.MessageDigest

fun ImageView.load(url: String?) {
    Glide.with(this).load(url).into(this)
}

fun ImageView.loadCircle(url: String) {
    Glide.with(this)
        .load(url)
        .transform(CircleCrop())
        .into(this)
}

/*glide的图片裁剪和imageview的scaletype有冲突，所以需要用centerrCrop先裁剪一下*/
@BindingAdapter(value = ["url", "corner"], requireAll = false)
fun ImageView.loadCorner(url: String, corner: Int) {
    if (HiViewUtil.isActivityDestroyed(context)) return

    val transform = Glide.with(this)
        .load(url)
        .transform(CenterCrop())
    if (corner > 0) {
        transform.transform(RoundedCorners(corner))
    }
    transform.into(this)
}

fun ImageView.loadCircleBorder(
    url: String,
    borderWidth: Float = 0f,
    borderColor: Int = Color.WHITE,
) {
    Glide.with(this)
        .load(url)
        .transform(CircleBorderTransform(borderWidth, borderColor))
        .into(this)
}

class CircleBorderTransform(val borderWidth: Float, borderColor: Int) : CenterCrop() {
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int,
    ): Bitmap {
        val transform = super.transform(pool, toTransform, outWidth, outHeight)
        val canvas = Canvas(transform)
        val height = (outHeight / 2).toFloat()
        val width = (outWidth / 2).toFloat()
        canvas.drawCircle(width, height, height.coerceAtMost(width) - borderWidth / 2, paint)
        canvas.setBitmap(null)
        return transform
    }
}
