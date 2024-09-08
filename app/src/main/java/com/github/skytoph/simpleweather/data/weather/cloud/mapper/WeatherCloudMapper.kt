package com.github.skytoph.simpleweather.data.weather.cloud.mapper

import com.github.skytoph.simpleweather.core.Mapper
import com.github.skytoph.simpleweather.data.weather.cloud.model.AlertCloud
import com.github.skytoph.simpleweather.data.weather.cloud.model.CurrentWeatherCloud
import com.github.skytoph.simpleweather.data.weather.cloud.model.DailyForecastCloud
import com.github.skytoph.simpleweather.data.weather.cloud.model.HourlyForecastCloud
import com.github.skytoph.simpleweather.data.weather.model.WeatherData

interface WeatherCloudMapper : Mapper<WeatherData> {
    fun map(
        current: CurrentWeatherCloud,
        timezoneOffset: Int,
        timezone: String,
        hourly: HourlyForecastCloud,
        daily: DailyForecastCloud,
        alerts: List<AlertCloud>,
    ): WeatherData
}