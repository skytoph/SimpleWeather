package com.github.skytoph.simpleweather.data.weather.cache.mapper.content.forecast

import com.github.skytoph.simpleweather.core.Mapper
import com.github.skytoph.simpleweather.core.data.DataBase
import com.github.skytoph.simpleweather.data.weather.cache.model.content.forecast.ForecastDB
import com.github.skytoph.simpleweather.data.weather.cache.model.content.forecast.HourlyForecastDB
import javax.inject.Inject

interface HourlyForecastDBMapper : Mapper<HourlyForecastDB> {

    fun map(
        time: Long, temp: Double, weatherId: Int, pop: Double, uvi: Double,
        parent: ForecastDB, dataBase: DataBase,
    ): HourlyForecastDB

    class Base @Inject constructor() : HourlyForecastDBMapper {

        override fun map(
            time: Long,
            temp: Double,
            weatherId: Int,
            pop: Double,
            uvi: Double,
            parent: ForecastDB,
            dataBase: DataBase,
        ) = dataBase.createEmbeddedObject<HourlyForecastDB>(parent, ForecastDB.FIELD_HOURLY).apply {
            this.time = time
            this.temp = temp
            this.uvi = uvi
            this.weatherId = weatherId
            this.precipitationProb = pop
        }
    }
}
