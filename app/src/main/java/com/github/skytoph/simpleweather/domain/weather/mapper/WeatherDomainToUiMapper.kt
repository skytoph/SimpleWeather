package com.github.skytoph.simpleweather.domain.weather.mapper

import com.github.skytoph.simpleweather.core.Mapper
import com.github.skytoph.simpleweather.core.provider.ResourceProvider
import com.github.skytoph.simpleweather.domain.weather.model.WeatherDomain.*
import com.github.skytoph.simpleweather.presentation.weather.WeatherUi
import javax.inject.Inject

interface WeatherDomainToUiMapper : Mapper<WeatherUi> {

    fun map(
        id: String,
        currentWeather: CurrentWeather,
        indicators: Indicators,
        horizon: Horizon,
        warnings: List<Warning>,
        hourly: List<HourlyForecast>,
        daily: List<DailyForecast>,
    ): WeatherUi

    class Base @Inject constructor(
        private val weatherMapper: CurrentWeatherDomainToUiMapper,
        private val indicatorsMapper: IndicatorsDomainToUiMapper,
        private val horizonMapper: HorizonDomainToUiMapper,
        private val warningsMapper: WarningsDomainToUiMapper,
        private val hourlyMapper: HourlyForecastListToUiMapper,
        private val dailyMapper: DailyForecastListToUiMapper,
    ) : WeatherDomainToUiMapper {

        override fun map(
            id: String,
            currentWeather: CurrentWeather,
            indicators: Indicators,
            horizon: Horizon,
            warnings: List<Warning>,
            hourly: List<HourlyForecast>,
            daily: List<DailyForecast>,
        ): WeatherUi = WeatherUi.Base(
            id,
            currentWeather.map(weatherMapper),
            warningsMapper.map(warnings),
            indicators.map(indicatorsMapper),
            hourlyMapper.map(hourly),
            dailyMapper.map(daily),
            horizon.map(horizonMapper),
        )
    }
}
