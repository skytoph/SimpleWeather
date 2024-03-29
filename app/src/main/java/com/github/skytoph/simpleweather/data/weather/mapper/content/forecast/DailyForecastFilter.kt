package com.github.skytoph.simpleweather.data.weather.mapper.content.forecast

import com.github.skytoph.simpleweather.core.util.time.CurrentTime
import com.github.skytoph.simpleweather.data.weather.model.content.forecast.DailyForecastData
import javax.inject.Inject

interface DailyForecastFilter {
    fun filter(forecast: List<DailyForecastData>): List<DailyForecastData>

    class Base @Inject constructor(private val currentTime: CurrentTime) : DailyForecastFilter {

        override fun filter(forecast: List<DailyForecastData>): List<DailyForecastData> {
            val dayInSeconds = currentTime.dayInSeconds()
            return forecast.filter { it.isNotOutdated(dayInSeconds) }
        }
    }
}