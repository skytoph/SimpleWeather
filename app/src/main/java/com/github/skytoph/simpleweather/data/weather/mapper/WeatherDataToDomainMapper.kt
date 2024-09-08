package com.github.skytoph.simpleweather.data.weather.mapper

import com.github.skytoph.simpleweather.core.Mapper
import com.github.skytoph.simpleweather.data.weather.mapper.content.forecast.DailyForecastFilter
import com.github.skytoph.simpleweather.data.weather.model.content.ContentData
import com.github.skytoph.simpleweather.data.weather.model.content.current.CurrentWeatherData
import com.github.skytoph.simpleweather.data.weather.model.content.forecast.DailyForecastData
import com.github.skytoph.simpleweather.data.weather.model.content.forecast.ForecastData
import com.github.skytoph.simpleweather.data.weather.model.content.forecast.HourlyForecastData
import com.github.skytoph.simpleweather.data.weather.model.content.forecast.WarningData
import com.github.skytoph.simpleweather.data.weather.model.content.horizon.HorizonData
import com.github.skytoph.simpleweather.data.weather.model.content.indicators.IndicatorsData
import com.github.skytoph.simpleweather.data.weather.model.identifier.IdentifierData
import com.github.skytoph.simpleweather.data.weather.model.time.ForecastTimeData
import com.github.skytoph.simpleweather.domain.weather.mapper.DailyForecastDomainMapper
import com.github.skytoph.simpleweather.domain.weather.mapper.HourlyForecastListDomainMapper
import com.github.skytoph.simpleweather.domain.weather.model.ContentDomain
import com.github.skytoph.simpleweather.domain.weather.model.DailyDomain
import com.github.skytoph.simpleweather.domain.weather.model.ForecastDomain
import com.github.skytoph.simpleweather.domain.weather.model.WeatherDomain
import javax.inject.Inject

interface WeatherDataToDomainMapper : Mapper<WeatherDomain> {

    fun map(identifier: IdentifierData, time: ForecastTimeData, content: ContentData): WeatherDomain

    class Base @Inject constructor(
        private val currentMapper: CurrentWeatherDataToDomainMapper,
        private val indicatorsMapper: IndicatorsDomainMapper,
        private val horizonDomainMapper: HorizonDataToDomainMapper,
        private val hourlyMapper: HourlyForecastListDomainMapper,
        private val dailyFilter: DailyForecastFilter,
        private val sunMapper: SunPositionMapper,
        private val warningMapper: WarningsDomainMapper,
    ) : WeatherDataToDomainMapper {

        override fun map(
            identifier: IdentifierData, time: ForecastTimeData, content: ContentData,
        ): WeatherDomain {
            val timezone = time.map(object : TimezoneMapper {
                override fun map(timezoneOffset: Int, timezone: String) =
                    Timezone.Base(timezone, timezoneOffset)
            })
            val contentMapper = object : ContentDataToDomainMapper {
                override fun map(
                    currentWeather: CurrentWeatherData,
                    indicators: IndicatorsData,
                    horizon: HorizonData,
                    forecast: ForecastData,
                ): ContentDomain {
                    val sunPosition = sunMapper.map(horizon, timezone)
                    val dailyMapper = object : DailyForecastDomainMapper {
                        override fun map(
                            time: Long, temp: Pair<Double, Double>, weatherId: Int, pop: Double,
                        ) = DailyDomain(timezone.withOffset(time), temp, weatherId, pop)
                    }
                    val forecastMapper = object : ForecastDomainMapper {
                        override fun map(
                            alerts: List<WarningData>,
                            hourlyForecast: List<HourlyForecastData>,
                            dailyForecast: List<DailyForecastData>,
                        ): ForecastDomain = ForecastDomain(
                            warningMapper.map(alerts, timezone, forecast, indicators.map()),
                            hourlyMapper.map(hourlyForecast, sunPosition, timezone),
                            dailyFilter.filter(dailyForecast, timezone).map { it.map(dailyMapper) })
                    }
                    return ContentDomain(
                        currentMapper.map(currentWeather, sunPosition, time, timezone),
                        indicatorsMapper.map(indicators, timezone),
                        sunPosition.map(horizonDomainMapper),
                        forecast.map(forecastMapper)
                    )
                }
            }
            return WeatherDomain(identifier.map(), content.map(contentMapper))
        }
    }
}