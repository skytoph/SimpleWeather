package com.github.skytoph.simpleweather.data.weather.mapper.content.forecast

import com.github.skytoph.simpleweather.core.Mapper
import com.github.skytoph.simpleweather.data.weather.cache.model.content.forecast.DailyForecastDB
import com.github.skytoph.simpleweather.data.weather.cache.model.content.forecast.HourlyForecastDB
import com.github.skytoph.simpleweather.data.weather.cache.model.content.forecast.WarningDB
import com.github.skytoph.simpleweather.data.weather.model.content.forecast.ForecastData
import javax.inject.Inject

interface ForecastDBToDataMapper : Mapper<ForecastData> {
    fun map(
        warnings: List<WarningDB>,
        hourly: List<HourlyForecastDB>,
        daily: List<DailyForecastDB>,
    ): ForecastData

    class Base @Inject constructor(
        private val warningsMapper: WarningListDataMapper,
        private val hourlyMapper: HourlyForecastDataMapper,
        private val dailyMapper: DailyForecastDataMapper,
    ) : ForecastDBToDataMapper {

        override fun map(
            warnings: List<WarningDB>,
            hourly: List<HourlyForecastDB>,
            daily: List<DailyForecastDB>,
        ): ForecastData = ForecastData(warningsMapper.map(warnings),
            hourly.map { it.map(hourlyMapper) },
            daily.map { it.map(dailyMapper) })
    }
}
