package com.github.skytoph.simpleweather.data.weather.mapper.content.forecast

import com.github.skytoph.simpleweather.core.Mapper
import com.github.skytoph.simpleweather.core.domain.mapper.WeatherImageId
import com.github.skytoph.simpleweather.data.weather.model.content.forecast.HourlyForecastData
import javax.inject.Inject

interface HourlyForecastDataListMapper : Mapper<List<HourlyForecastData>> {

    fun map(
        time: List<Long>,
        temp: List<Double>,
        weatherId: List<Int>,
        pop: List<Double>,
        cloudCover: List<Double>,
        uvi: List<Double>
    ): List<HourlyForecastData>

    class Base @Inject constructor(private val weatherMapper: WeatherCodeMapper) :
        HourlyForecastDataListMapper {

        override fun map(
            time: List<Long>,
            temp: List<Double>,
            weatherId: List<Int>,
            pop: List<Double>,
            cloudCover: List<Double>,
            uvi: List<Double>,
        ) = List(time.size) { index ->
            HourlyForecastData(
                time = time.getOrNull(index) ?: 0,
                temp = temp.getOrNull(index) ?: 0.0,
                uvi = uvi.getOrNull(index) ?: 0.0,
                weatherId = weatherId.getOrNull(index)?.let { code ->
                    weatherMapper.map(
                        code = code,
                        pop = pop.getOrNull(index) ?: return@let code,
                        clouds = cloudCover.getOrNull(index) ?: return@let code
                    )
                } ?: WeatherImageId.CODE_DEFAULT,
                precipitationProb = pop.getOrNull(index) ?: 0.0
            )
        }
    }
}