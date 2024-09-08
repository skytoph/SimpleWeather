package com.github.skytoph.simpleweather.presentation.weather.model

import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.TooltipCompat

abstract class TextWithTooltipUi(
    private val text: String,
    @StringRes private val tooltipTextId: Int,
    @StringRes private val tooltipValueId: Int,
) {
    fun show(textView: TextView) {
        val res = textView.resources
        val tooltip = res.getString(tooltipTextId, res.getString(tooltipValueId, text))

        textView.text = text
        TooltipCompat.setTooltipText(textView, tooltip)
    }
}