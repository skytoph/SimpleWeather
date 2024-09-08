package com.github.skytoph.simpleweather.data.weather.mapper.content.forecast

import com.github.skytoph.simpleweather.core.Mapper
import com.github.skytoph.simpleweather.data.weather.model.content.forecast.HourlyForecastData
import javax.inject.Inject

interface HourlyForecastDataMapper : Mapper<HourlyForecastData> {

    fun map(time: Long, temp: Double, weatherId: Int, pop: Double, uvi: Double): HourlyForecastData

    class Base @Inject constructor() : HourlyForecastDataMapper {

        override fun map(
            time: Long, temp: Double, weatherId: Int, pop: Double, uvi: Double
        ) = HourlyForecastData(time, temp, weatherId, pop, uvi)
    }
}
