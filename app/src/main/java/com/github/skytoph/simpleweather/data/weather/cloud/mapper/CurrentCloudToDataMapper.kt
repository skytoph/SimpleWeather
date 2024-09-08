package com.github.skytoph.simpleweather.data.weather.cloud.mapper

import com.github.skytoph.simpleweather.core.Mapper
import com.github.skytoph.simpleweather.data.weather.model.WeatherData

interface CurrentCloudToDataMapper : Mapper<WeatherData> {
    fun map(dt: Long, temp: Double, uvi: Double, weather: Int, pop: Double, clouds: Double): WeatherData
}
