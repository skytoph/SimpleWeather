package com.github.skytoph.simpleweather.presentation.view.horizon

import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.PointF
import androidx.annotation.ColorRes
import com.github.skytoph.simpleweather.R

class DashedLineDrawer(
    private val paint: Paint,
    private val resourceProvider: ResourceProvider,
) {
    fun prepare(@ColorRes colorId: Int) {
        paint.apply {
            reset()
            val dashLineGap =
                resourceProvider.getDimensionPixel(R.dimen.horizon_dash_line_gap).toFloat()
            val dashLineStrokeWidth =
                resourceProvider.getDimensionPixel(R.dimen.horizon_dash_line_width).toFloat()
            val dashLineOffset = (dashLineGap / 2)
            paint.color = resourceProvider.getColor(colorId)
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