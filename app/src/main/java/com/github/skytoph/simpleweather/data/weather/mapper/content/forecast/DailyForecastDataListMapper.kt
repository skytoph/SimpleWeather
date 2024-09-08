package com.github.skytoph.simpleweather.data.weather.mapper.content.forecast

import com.github.skytoph.simpleweather.core.Mapper
import com.github.skytoph.simpleweather.core.domain.mapper.WeatherImageId
import com.github.skytoph.simpleweather.data.weather.mapper.content.horizon.HorizonDataMapper
import com.github.skytoph.simpleweather.data.weather.model.content.forecast.DailyForecastData
import javax.inject.Inject

interface DailyForecastDataListMapper : Mapper<List<DailyForecastData>> {

    fun map(
        time: List<Long>,
        tempMin: List<Double>,
        tempMax: List<Double>,
        weatherId: List<Int>,
        pop: List<Double>,
        uvi: List<Double>,
        sunrise: List<Long>,
        sunset: List<Long>,
    ): List<DailyForecastData>

    class Base @Inject constructor(
        private val horizonMapper: HorizonDataMapper,
        private val weatherMapper: WeatherCodeMapper
    ) : DailyForecastDataListMapper {

        override fun map(
            time: List<Long>,
            tempMin: List<Double>,
            tempMax: List<Double>,
            weatherId: List<Int>,
            pop: List<Double>,
            uvi: List<Double>,
            sunrise: List<Long>,
            sunset: List<Long>,
        ) = List(time.size) { index ->
            DailyForecastData(
                time = time.getOrNull(index) ?: 0,
                temp = (tempMin.getOrNull(index) ?: 0.0) to (tempMax.getOrNull(index) ?: 0.0),
                weatherId = weatherId.getOrNull(index)?.let { code ->
                    weatherMapper.map(
                        code = code,
                        pop = pop.getOrNull(index) ?: return@let code,
                        clouds = 100.0
                    )
                } ?: WeatherImageId.CODE_DEFAULT,
                precipitationProb = pop.getOrNull(index) ?: 0.0,
                uvi = uvi.getOrNull(index) ?: 0.0,
                horizon = horizonMapper.map(
                    sunrise = sunrise.getOrNull(index) ?: 0,
                    sunset = sunset.getOrNull(index) ?: 0
                )
            )
        }
    }
}