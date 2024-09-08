package com.github.skytoph.simpleweather.data.weather.model.content.forecast

import com.github.skytoph.simpleweather.core.Mappable
import com.github.skytoph.simpleweather.core.MappableToDB
import com.github.skytoph.simpleweather.core.data.DataBase
import com.github.skytoph.simpleweather.data.weather.cache.mapper.content.forecast.HourlyForecastDBMapper
import com.github.skytoph.simpleweather.data.weather.cache.model.content.forecast.ForecastDB
import com.github.skytoph.simpleweather.data.weather.cache.model.content.forecast.HourlyForecastDB
import com.github.skytoph.simpleweather.data.weather.mapper.content.forecast.WarningPopMapper
import com.github.skytoph.simpleweather.domain.weather.mapper.HourlyForecastDomainMapper
import com.github.skytoph.simpleweather.domain.weather.model.HourlyDomain

data class HourlyForecastData(
    private val time: Long,
    private val temp: Double,
    private val weatherId: Int,
    private val precipitationProb: Double,
    private val uvi: Double,
) : Mappable<HourlyDomain, HourlyForecastDomainMapper>,
    MappableToDB.EmbeddedValid<HourlyForecastDB, ForecastDB, HourlyForecastDBMapper> {

    override fun map(mapper: HourlyForecastDomainMapper): HourlyDomain =
        mapper.map(time, temp, weatherId, precipitationProb)

    override fun map(
        mapper: HourlyForecastDBMapper,
        dataBase: DataBase,
        parent: ForecastDB,
    ): HourlyForecastDB = mapper.map(time, temp, weatherId, precipitationProb, uvi, parent, dataBase)

    fun isNotOutdated(timeSeconds: Long): Boolean = timeSeconds <= time

    fun isTimeForecasted(timeSeconds: Long) = time == timeSeconds

    fun map(mapper: WarningPopMapper): Double = mapper.map(precipitationProb)
}