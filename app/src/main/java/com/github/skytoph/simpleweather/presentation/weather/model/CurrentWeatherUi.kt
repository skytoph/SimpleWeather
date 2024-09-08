package com.github.skytoph.simpleweather.presentation.weather.model

import android.widget.ImageView
import android.widget.TextView
import com.github.skytoph.simpleweather.R

data class CurrentWeatherUi(
    private val city: String,
    private val degree: String,
    private val updated: String,
    private val weather: WeatherResources,
) {

    fun show(
        weatherImageView: ImageView,
        descriptionTextView: TextView,
        cityTextView: TextView,
        degreeTextView: TextView,
        updatedTextView: TextView,
    ) {
        weatherImageView.setImageResource(weather.imageId)
        descriptionTextView.text = weatherImageView.context.getString(weather.titleId)
        cityTextView.text = city
        degreeTextView.text = degree
        updatedTextView.text = updatedTextView.context.getString(R.string.last_update_time, updated)
    }
}