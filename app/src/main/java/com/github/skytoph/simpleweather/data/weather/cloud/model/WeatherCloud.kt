package com.github.skytoph.simpleweather.data.weather.cloud.model

import com.github.skytoph.simpleweather.core.Mappable
import com.github.skytoph.simpleweather.data.weather.cloud.mapper.WeatherCloudMapper
import com.github.skytoph.simpleweather.data.weather.model.WeatherData
import com.squareup.moshi.Json

/*
  {
  "latitude": 47.3125,
  "longitude": 32.875,
  "generationtime_ms": 0.055909156799316406,
  "utc_offset_seconds": 10800,
  "timezone": "Europe/Kiev",
  "timezone_abbreviation": "EEST",
  "elevation": 22,
  "current_units": {},
  "current": {
    "time": 1725351300,
    "interval": 900,
    "temperature_2m": 27.5,
    "precipitation_probability": 13,
    "weather_code": 3,
    "cloud_cover": 85,
    "uv_index": 4.7
  },
  "hourly_units": {},
  "hourly": {
    "time": [1725310800],
    "temperature_2m": [23.6],
    "precipitation_probability": [3],
    "weather_code": [2],
    "cloud_cover": [51, 77],
    "uv_index": [0]
  },
  "daily_units": {},
  "daily": {
    "time": [1725310800],
    "weather_code": [3],
    "temperature_2m_max": [29.3],
    "temperature_2m_min": [20.4],
    "sunrise": [1725333048],
    "sunset": [1725380672],
    "precipitation_probability_max": [46]
  }
}
 */

data class WeatherCloud(
    @Json(name = "latitude")
    private val lat: Double,

    @Json(name = "longitude")
    private val lon: Double,

    @Json(name = "current")
    private val current: CurrentWeatherCloud?,

    @Json(name = "timezone")
    private val timezone: String?,

    @Json(name = "utc_offset_seconds")
    private val timezoneOffset: Int,

    @Json(name = "hourly")
    private val hourly: HourlyForecastCloud?,

    @Json(name = "daily")
    private val daily: DailyForecastCloud?,

) : Mappable<WeatherData, WeatherCloudMapper> {

    override fun map(mapper: WeatherCloudMapper): WeatherData = mapper.map(
        current ?: CurrentWeatherCloud.empty,
        timezoneOffset,
        timezone.toString(),
        hourly ?: HourlyForecastCloud.empty,
        daily ?: DailyForecastCloud.empty,
        emptyList()
    )
}