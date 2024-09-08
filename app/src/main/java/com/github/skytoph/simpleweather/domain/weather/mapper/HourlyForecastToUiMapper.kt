package com.github.skytoph.simpleweather.domain.weather.mapper

import com.github.skytoph.simpleweather.core.Mapper
import com.github.skytoph.simpleweather.core.domain.mapper.WeatherImageId
import com.github.skytoph.simpleweather.core.util.formatter.ProbabilityFormatter
import com.github.skytoph.simpleweather.core.util.formatter.TemperatureFormatter
import com.github.skytoph.simpleweather.core.util.formatter.TimeFormatter
import com.github.skytoph.simpleweather.presentation.weather.model.ForecastUi.Hourly
import javax.inject.Inject


interface HourlyForecastToUiMapper : Mapper<Hourly> {

    fun map(time: Long, temp: Double, weatherId: Int, pop: Double, isDaytime: Boolean): Hourly

    class Base @Inject constructor(
        private val tempFormatter: TemperatureFormatter,
        private val timeFormatter: TimeFormatter,
        private val probabilityFormatter: ProbabilityFormatter,
    ) : HourlyForecastToUiMapper, WeatherImageId() {

        override fun map(
            time: Long, temp: Double, weatherId: Int, pop: Double, isDaytime: Boolean
        ) = Hourly(
            timeFormatter.timeFull(time),
            tempFormatter.format(temp),
            weatherImageRes(weatherId, isDaytime),
            probabilityFormatter.format(pop)
        )
    }
}