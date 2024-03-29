package com.github.skytoph.simpleweather.data.weather.mapper

import com.github.skytoph.simpleweather.core.Mapper
import com.github.skytoph.simpleweather.core.util.time.CurrentTime
import com.github.skytoph.simpleweather.data.weather.mapper.content.forecast.FindForecastedPop
import com.github.skytoph.simpleweather.data.weather.model.content.forecast.ForecastData
import com.github.skytoph.simpleweather.data.weather.model.content.forecast.WarningData
import com.github.skytoph.simpleweather.domain.weather.model.WarningDomain
import javax.inject.Inject

interface WarningsDomainMapper : Mapper<List<WarningDomain>> {
    fun map(
        warnings: List<WarningData>,
        timezone: Timezone,
        forecast: ForecastData,
        currentPop: Double,
    ): List<WarningDomain>

    class Base @Inject constructor(
        private val findPopMapper: FindForecastedPop, private val currentTime: CurrentTime,
    ) : WarningsDomainMapper {

        override fun map(
            warnings: List<WarningData>,
            timezone: Timezone,
            forecast: ForecastData,
            currentPop: Double,
        ): List<WarningDomain> {
            val mapper = object : WarningDataToDomainMapper {
                override fun map(
                    name: String, startTime: Long, endTime: Long, description: String,
                ): WarningDomain {
                    val started = currentTime.inSeconds() > startTime
                    return WarningDomain(name,
                        timezone.withOffset(if (started) endTime else startTime),
                        started,
                        forecast.map(findPopMapper, startTime, currentPop),
                        description)
                }
            }
            return warnings.map { it.map(mapper) }
        }
    }
}