package com.github.skytoph.simpleweather.core.presentation.view.horizon

import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.PointF
import androidx.annotation.ColorInt
import com.github.skytoph.simpleweather.R
import com.github.skytoph.simpleweather.core.provider.ResourceProvider

class DashedLineDrawer(
    private val paint: Paint,
    private val resources: ResourceProvider,
) {
    fun prepare(@ColorInt color: Int) {
        paint.apply {
            reset()
            val dashLineGap =
                resources.dimensionPixel(R.dimen.horizon_dash_line_gap).toFloat()
            val dashLineStrokeWidth =
                resources.dimensionPixel(R.dimen.horizon_dash_line_width).toFloat()
            val dashLineOffset = (dashLineGap / 2)
            paint.color = color
            style = Paint.Style.STROKE
            isAntiAlias = false
            strokeWidth = dashLineStrokeWidth
            pathEffect = DashPathEffect(floatArrayOf(dashLineGap, dashLineGap), dashLineOffset)
        }
    }

    fun drawLine(canvas: Canvas, pointStart: PointF, pointEnd: PointF) {
        canvas.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y, paint)
    }
}