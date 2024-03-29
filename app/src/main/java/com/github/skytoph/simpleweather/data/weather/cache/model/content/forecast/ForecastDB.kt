package com.github.skytoph.simpleweather.data.weather.cache.model.content.forecast

import com.github.skytoph.simpleweather.core.Mappable
import com.github.skytoph.simpleweather.data.weather.cache.model.content.MappableToHorizon
import com.github.skytoph.simpleweather.data.weather.mapper.content.forecast.FindForecastMapper
import com.github.skytoph.simpleweather.data.weather.mapper.content.forecast.ForecastDBToDataMapper
import com.github.skytoph.simpleweather.data.weather.model.content.forecast.ForecastData
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmField

@RealmClass(embedded = true)
open class ForecastDB : RealmObject(), Mappable<ForecastData, ForecastDBToDataMapper> {

    @RealmField(name = FIELD_WARNINGS)
    var warnings: RealmList<WarningDB> = RealmList()

    @RealmField(name = FIELD_HOURLY)
    var hourly: RealmList<HourlyForecastDB> = RealmList()

    @RealmField(name = FIELD_DAILY)
    var daily: RealmList<DailyForecastDB> = RealmList()

    companion object {
        const val FIELD_WARNINGS = "warnings"
        const val FIELD_HOURLY = "hourly"
        const val FIELD_DAILY = "daily"
    }

    override fun map(mapper: ForecastDBToDataMapper): ForecastData =
        mapper.map(warnings, hourly, daily)

    fun findWeather(mapper: FindForecastMapper): MappableToForecast = mapper.map(hourly, daily)

    fun findHorizon(mapper: FindForecastMapper): MappableToHorizon = mapper.map(daily)
}