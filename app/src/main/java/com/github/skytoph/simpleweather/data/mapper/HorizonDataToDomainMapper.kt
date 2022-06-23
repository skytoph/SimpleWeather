package com.github.skytoph.simpleweather.data.mapper

import com.github.skytoph.simpleweather.core.SunCalculator
import com.github.skytoph.simpleweather.core.Mapper
import com.github.skytoph.simpleweather.domain.model.WeatherDomain

interface HorizonDataToDomainMapper : Mapper<WeatherDomain.Horizon> {

    fun map(sunrise: Long, sunset: Long, currentTime: Long): WeatherDomain.Horizon

    class Base(
        private val sunCalculator: SunCalculator,
    ) : HorizonDataToDomainMapper {

        override fun map(sunrise: Long, sunset: Long, currentTime: Long): WeatherDomain.Horizon =
            WeatherDomain.Horizon(sunrise,
                sunset,
                sunCalculator.duration(sunrise, sunset),
                sunCalculator.remainingDaylight(sunrise, sunset, currentTime),
                sunCalculator.sunPosition(sunrise, sunset, currentTime))
    }
}
