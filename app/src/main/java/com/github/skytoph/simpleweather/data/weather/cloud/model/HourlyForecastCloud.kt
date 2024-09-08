package com.github.skytoph.simpleweather.data.weather.cloud.model

import com.github.skytoph.simpleweather.core.Mappable
import com.github.skytoph.simpleweather.core.MappableTo
import com.github.skytoph.simpleweather.data.weather.mapper.content.forecast.HourlyForecastDataListMapper
import com.github.skytoph.simpleweather.data.weather.model.content.forecast.HourlyForecastData
import com.squareup.moshi.Json

/*
  "hourly": {
    "time": [1725310800],
    "temperature_2m": [23.6],
    "precipitation_probability": [3],
    "weather_code": [2],
    "cloud_cover": [51, 77],
    "uv_index": [0]
  }
 */

data class HourlyForecastCloud(
    @Json(name = "time")
    private val dt: List<Long>? = emptyList(),

    @Json(name = "temperature_2m")
    private val temp: List<Double>? = emptyList(),

    @Json(name = "weather_code")
    private val weather: List<Int>? = emptyList(),

    @Json(name = "precipitation_probability")
    private val pop: List<Double>? = emptyList(),

    @Json(name = "cloud_cover")
    private val cloudCover: List<Double>? = emptyList(),

    @Json(name = "uv_index")
    private val uvi: List<Double>? = emptyList(),
) : Mappable<List<HourlyForecastData>, HourlyForecastDataListMapper>, MappableTo<Double> {

    override fun map(mapper: HourlyForecastDataListMapper): List<HourlyForecastData> =
        mapper.map(
            dt ?: emptyList(),
            temp ?: emptyList(),
            weather ?: emptyList(),
            pop ?: emptyList(),
            cloudCover ?: emptyList(),
            uvi ?: emptyList()
        )

    override fun map(): Double = pop?.first() ?: 0.0

    companion object {
        val empty: HourlyForecastCloud = HourlyForecastCloud()
    }
}