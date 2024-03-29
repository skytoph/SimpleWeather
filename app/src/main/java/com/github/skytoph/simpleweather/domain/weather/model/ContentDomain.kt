package com.github.skytoph.simpleweather.domain.weather.model

import com.github.skytoph.simpleweather.core.Mappable
import com.github.skytoph.simpleweather.domain.weather.mapper.*
import com.github.skytoph.simpleweather.presentation.weather.model.CurrentWeatherUi
import com.github.skytoph.simpleweather.presentation.weather.model.HorizonUi
import com.github.skytoph.simpleweather.presentation.weather.model.IndicatorsUi
import com.github.skytoph.simpleweather.presentation.weather.model.WeatherUi

data class ContentDomain(
    private val currentWeather: CurrentWeatherDomain,
    private val indicators: IndicatorsDomain,
    private val horizon: HorizonDomain,
    private val forecast: ForecastDomain,
) : Mappable<WeatherUi, ContentUiMapper> {

    override fun map(mapper: ContentUiMapper): WeatherUi =
        mapper.map(currentWeather, indicators, horizon, forecast)

    fun map(mapper: OutdatedWeatherUiMapper): WeatherUi.Outdated = currentWeather.map(mapper)

    fun isOutdated(): Boolean = forecast.isOutdated()
}

data class CurrentWeatherDomain(
    private val location: Map<String, String>,
    private val temperature: Double,
    private val weatherId: Int,
    private val updated: Long,
    private val isDayNow: Boolean,
) : Mappable<CurrentWeatherUi, CurrentWeatherDomainToUiMapper> {

    override fun map(mapper: CurrentWeatherDomainToUiMapper): CurrentWeatherUi =
        mapper.map(location, temperature, weatherId, updated, isDayNow)

    fun map(mapper: OutdatedWeatherUiMapper): WeatherUi.Outdated =
        mapper.map(location)
}

data class IndicatorsDomain(
    private val timezone: String,
    private val uvi: Double,
    private val precipitationProb: Double,
    private val airQualityIndex: Int,
) : Mappable<IndicatorsUi, IndicatorsDomainToUiMapper> {

    override fun map(mapper: IndicatorsDomainToUiMapper): IndicatorsUi =
        mapper.map(timezone, uvi, precipitationProb, airQualityIndex)
}

data class HorizonDomain(
    private val sunrise: Int,
    private val sunset: Int,
    private val dayLength: Int,
    private val daylight: Int,
    private val sunPosition: Double,
) : Mappable<HorizonUi, HorizonDomainToUiMapper> {

    override fun map(mapper: HorizonDomainToUiMapper): HorizonUi =
        mapper.map(sunrise, sunset, dayLength, daylight, sunPosition)
}