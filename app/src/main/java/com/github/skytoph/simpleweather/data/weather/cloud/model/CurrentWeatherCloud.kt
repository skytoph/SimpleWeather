package com.github.skytoph.simpleweather.data.weather.cloud.model

import com.github.skytoph.simpleweather.core.Mappable
import com.github.skytoph.simpleweather.data.weather.cloud.mapper.CurrentCloudToDataMapper
import com.github.skytoph.simpleweather.data.weather.model.WeatherData
import com.squareup.moshi.Json

/*
 "current": {
    "time": 1725351300,
    "interval": 900,
    "temperature_2m": 27.5,
    "precipitation_probability": 13,
    "weather_code": 3,
    "cloud_cover": 85,
    "uv_index": 4.7
  }
*/

data class CurrentWeatherCloud(
    @Json(name = "time")
    private val dt: Long,

    @Json(name = "temperature_2m")
    private val temp: Double,

    @Json(name = "precipitation_probability")
    private val pop: Double,

    @Json(name = "uv_index")
    private val uvi: Double,

    @Json(name = "cloud_cover")
    private val cloudCover: Double,

    @Json(name = "weather_code")
    private val weather: Int

) : Mappable<WeatherData, CurrentCloudToDataMapper> {

    override fun map(mapper: CurrentCloudToDataMapper): WeatherData =
        mapper.map(dt, temp, uvi, weather, pop, cloudCover)

    companion object {
        val empty = CurrentWeatherCloud(0, 0.0, 0.0, 0.0, 0.0, 0)
    }
}