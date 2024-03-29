package com.github.skytoph.simpleweather.core.presentation.view.horizon

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.withStyledAttributes
import com.github.skytoph.simpleweather.R
import com.github.skytoph.simpleweather.core.provider.ResourceManager
import com.github.skytoph.simpleweather.core.provider.ResourceProvider

class HorizonView : View {
    private val curveDrawer: CurveDrawer
    private val lineDrawer: DashedLineDrawer
    private val textDrawer: TextDrawer
    private val sunDrawer: DrawableDrawer
    private val pointsCalculator: HorizonCurveCalculator

    private val paint = Paint()

    private val pNightStart = PointF()
    private val pMidnight = PointF()
    private val pSunrise = PointF()
    private val pMidday = PointF()
    private val pSunset = PointF()
    private val pSunriseTime = PointF()
    private val pSunsetTime = PointF()
    private val pLineEnd = PointF()

    private lateinit var fontFamily: String
    private lateinit var sunsetTimeValue: String
    private lateinit var sunriseTimeValue: String

    @ColorRes
    private var titleColor: Int = 0

    @ColorRes
    private var valueColor: Int = 0

    private var t = 0f

    //region constructors
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet? = null,
    ) : super(context, attrs) {
        init(attrs)
    }
    //endregion

    init {
        val resourceManager: ResourceProvider = ResourceManager.Base(context)
        minimumHeight = resources.getDimensionPixelSize(R.dimen.horizon_min_height)
        curveDrawer = CurveDrawer(paint, resourceManager)
        lineDrawer = DashedLineDrawer(paint, resourceManager)
        textDrawer = TextDrawer(paint, resourceManager)
        sunDrawer = DrawableDrawer(resourceManager)
        pointsCalculator = HorizonCurveCalculator(
            listOf(
                pNightStart,
                pMidnight,
                pSunrise,
                pMidday,
                pSunset,
                pSunriseTime,
                pSunsetTime,
                pLineEnd
            )
        )
    }

    private fun init(attrs: AttributeSet?) {
        context.withStyledAttributes(attrs, R.styleable.HorizonView) {
            titleColor = getColor(R.styleable.HorizonView_titleColor, R.color.light_gray)
            valueColor = getColor(R.styleable.HorizonView_valueColor, R.color.dark_gray)

            sunriseTimeValue = getString(R.styleable.HorizonView_sunrise_time) ?: "00:00"
            sunsetTimeValue = getString(R.styleable.HorizonView_sunset_time) ?: "00:00"
            fontFamily = getString(R.styleable.HorizonView_fontFamily) ?: ""
            t = getFloat(R.styleable.HorizonView_t, 0f)
        }
    }

    fun setValues(sunrise: String, sunset: String, sunPosition: Double) {
        sunriseTimeValue = sunrise
        sunsetTimeValue = sunset
        t = sunPosition.toFloat()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = resources.getDimensionPixelSize(R.dimen.horizon_desired_width)
        val desiredHeight = resources.getDimensionPixelSize(R.dimen.horizon_desired_height)

        val width = measureDimension(desiredWidth, widthMeasureSpec)
        val height = measureDimension(desiredHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    private fun measureDimension(desiredSize: Int, measureSpec: Int): Int {
        var result = 0

        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        when (specMode) {
            MeasureSpec.EXACTLY -> result = specSize
            MeasureSpec.AT_MOST -> result = Math.min(desiredSize, specSize)
            MeasureSpec.UNSPECIFIED -> result = desiredSize
        }

        return result
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        pointsCalculator.calculatePositions(
            width.toFloat(),
            height.toFloat(),
            halfSunSize = resources.getDimension(R.dimen.horizon_sun_size) / 2,
        )
    }

    override fun onDraw(canvas: Canvas) {
        curveDrawer.drawCurve(canvas, pNightStart, pMidnight, pSunrise, R.color.blue)
        curveDrawer.drawCurve(canvas, pSunrise, pMidday, pSunset, R.color.light_blue)

        drawLines(canvas)
        drawText(canvas)

        sunDrawer.draw(
            canvas,
            pointsCalculator.getSunPosition(t),
            R.drawable.weather_sun,
            R.dimen.horizon_sun_size
        )
    }

    private fun drawLines(canvas: Canvas) {
        lineDrawer.prepare(titleColor)

        lineDrawer.drawLine(canvas, pNightStart, pLineEnd)
        lineDrawer.drawLine(canvas, pSunrise, pSunriseTime)
        lineDrawer.drawLine(canvas, pSunset, pSunsetTime)
    }

    private fun drawText(canvas: Canvas) {
        val timeTextSize =
            resources.getDimensionPixelSize(R.dimen.horizon_time_value_text_size).toFloat()
        val labelTextSize =
            resources.getDimensionPixelSize(R.dimen.horizon_time_label_text_size).toFloat()

        textDrawer.prepare()

        //draw labels
        drawLabels(canvas, timeTextSize, labelTextSize)

        //draw time
        drawTimeValues(canvas, timeTextSize)
    }

    private fun drawLabels(canvas: Canvas, timeTextOffset: Float, textSize: Float) {
        textDrawer.setup(fontFamily, titleColor, textSize)

        val labelY =
            pSunriseTime.y - timeTextOffset - resources.getDimension(R.dimen.horizon_label_bottom_margin)

        textDrawer.drawText(canvas, pSunriseTime.x, labelY, R.string.sunrise)
        textDrawer.drawText(canvas, pSunsetTime.x, labelY, R.string.sunset)

        paint.textAlign = Paint.Align.RIGHT

        val positionX = width - resources.getDimension(R.dimen.horizon_right_margin)
        val positionY = pSunset.y - resources.getDimension(R.dimen.horizon_bottom_margin)
        textDrawer.drawText(canvas, positionX, positionY, R.string.horizon)
    }

    private fun drawTimeValues(canvas: Canvas, textSize: Float) {
        textDrawer.setup(fontFamily, valueColor, textSize)

        val marginBottom = resources.getDimension(R.dimen.horizon_time_value_bottom_margin)
        textDrawer.drawText(
            canvas,
            pSunriseTime.x,
            pSunriseTime.y - marginBottom,
            sunriseTimeValue
        )
        textDrawer.drawText(canvas, pSunsetTime.x, pSunsetTime.y - marginBottom, sunsetTimeValue)
    }
}