package com.github.skytoph.simpleweather.data.cloud.weather.model

import com.github.skytoph.simpleweather.core.Mappable
import com.github.skytoph.simpleweather.data.WeatherData
import com.github.skytoph.simpleweather.data.cloud.weather.mapper.CurrentCloudMapper
import com.squareup.moshi.Json

class CurrentWeatherCloud(
    @field:Json(name="dt")
    private val dt: Long,

    @field:Json(name="temp")
    private val temp: Double,

    @field:Json(name="sunrise")
    private val sunrise: Long,

    @field:Json(name="sunset")
    private val sunset: Long,

    @field:Json(name="uvi")
    private val uvi: Double,

    @field:Json(name="weather")
    private val weather: List<WeatherTypeCloud>

) : Mappable<WeatherData, CurrentCloudMapper> {

    override fun map(mapper: CurrentCloudMapper): WeatherData = mapper.map(dt, temp, sunrise, sunset, uvi, weather)
}