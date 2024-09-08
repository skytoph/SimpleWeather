package com.github.skytoph.simpleweather.data.weather.model.content

import com.github.skytoph.simpleweather.core.Mappable
import com.github.skytoph.simpleweather.core.MappableToDB
import com.github.skytoph.simpleweather.core.data.DataBase
import com.github.skytoph.simpleweather.data.weather.cache.mapper.content.ContentDBMapper
import com.github.skytoph.simpleweather.data.weather.cache.model.WeatherDB
import com.github.skytoph.simpleweather.data.weather.cache.model.content.ContentDB
import com.github.skytoph.simpleweather.data.weather.mapper.ContentDataToDomainMapper
import com.github.skytoph.simpleweather.data.weather.model.WeatherData
import com.github.skytoph.simpleweather.data.weather.model.content.current.CurrentWeatherData
import com.github.skytoph.simpleweather.data.weather.model.content.forecast.ForecastData
import com.github.skytoph.simpleweather.data.weather.model.content.horizon.HorizonData
import com.github.skytoph.simpleweather.data.weather.model.content.indicators.IndicatorsData
import com.github.skytoph.simpleweather.data.weather.update.UpdateContent
import com.github.skytoph.simpleweather.data.weather.update.UpdateForecastContent
import com.github.skytoph.simpleweather.domain.weather.model.ContentDomain

data class ContentData(
    private val currentWeather: CurrentWeatherData,
    private val indicators: IndicatorsData,
    private val horizon: HorizonData,
    private val forecast: ForecastData,
) : MappableToDB.EmbeddedValid<ContentDB, WeatherDB, ContentDBMapper>,
    Mappable<ContentDomain, ContentDataToDomainMapper> {

    override fun map(mapper: ContentDBMapper, database: DataBase, parent: WeatherDB): ContentDB =
        mapper.map(currentWeather, indicators, horizon, forecast, parent, database)

    override fun map(mapper: ContentDataToDomainMapper): ContentDomain =
        mapper.map(currentWeather, indicators, horizon, forecast)

    fun update(mapper: UpdateContent) =
        mapper.update(currentWeather, indicators, horizon, forecast)

    fun update(mapper: UpdateForecastContent): WeatherData =
        mapper.update(currentWeather, indicators, horizon, forecast)

    fun containsLocation(language: String): Boolean = currentWeather.containsLocation(language)
}