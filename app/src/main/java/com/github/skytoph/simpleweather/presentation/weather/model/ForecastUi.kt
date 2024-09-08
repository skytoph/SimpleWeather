package com.github.skytoph.simpleweather.presentation.weather.model

import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.TooltipCompat
import com.github.skytoph.simpleweather.core.Matcher

data class ListUi(
    private val hourly: List<ForecastUi.Hourly>,
    private val daily: List<ForecastUi.Daily>,
    private val warnings: List<WarningUi>,
) {

    fun show(submitLists: (List<WarningUi>, List<ForecastUi.Hourly>, List<ForecastUi.Daily>) -> Unit) =
        submitLists(warnings, hourly, daily)
}

sealed class ForecastUi(
    private val time: String,
    private val temp: String,
    private val weather: WeatherResources,
    private val precipitationProb: String,
) {

    fun show(
        weatherImageView: ImageView,
        timeTextView: TextView,
        tempTextView: TextView,
        popTextView: TextView,
    ) {
        val description = weatherImageView.context.resources.getString(weather.titleId)
        TooltipCompat.setTooltipText(weatherImageView, description)
        weatherImageView.setImageResource(weather.imageId)
        timeTextView.text = time
        timeTextView.text = time
        tempTextView.text = temp
        popTextView.text = precipitationProb
    }

    data class Hourly(
        private val time: String,
        private val temp: String,
        private val weather: WeatherResources,
        private val precipitationProb: String,
    ) : ForecastUi(time, temp, weather, precipitationProb), Matcher<Hourly> {

        override fun matches(item: Hourly): Boolean = time == item.time
        override fun contentMatches(item: Hourly): Boolean = equals(item)
    }

    data class Daily(
        private val day: String,
        private val maxTemp: String,
        private val minTemp: String,
        private val weather: WeatherResources,
        private val precipitationProb: String,
    ) : ForecastUi(day, maxTemp, weather, precipitationProb), Matcher<Daily> {

        fun show(tempMinTextView: TextView) {
            tempMinTextView.text = minTemp
        }

        override fun matches(item: Daily): Boolean = day == item.day
        override fun contentMatches(item: Daily): Boolean = equals(item)
    }
}

data class WeatherResources(@DrawableRes val imageId: Int, @StringRes val titleId: Int)