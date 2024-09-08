package com.github.skytoph.simpleweather.data.weather.mapper.content.forecast

import com.github.skytoph.simpleweather.core.util.time.CurrentTime
import com.github.skytoph.simpleweather.data.weather.mapper.Timezone
import com.github.skytoph.simpleweather.data.weather.model.content.forecast.DailyForecastData
import javax.inject.Inject

interface DailyForecastFilter {
    fun filter(forecast: List<DailyForecastData>, timezone: Timezone): List<DailyForecastData>

    class Base @Inject constructor(private val currentTime: CurrentTime) : DailyForecastFilter {

        override fun filter(forecast: List<DailyForecastData>, timezone: Timezone): List<DailyForecastData> {
            val dayInSeconds = timezone.withoutOffset(currentTime.dayInSeconds(timezone))
            val filter = forecast.filter { it.isNotOutdated(dayInSeconds) }
            return filter
        }
    }
}