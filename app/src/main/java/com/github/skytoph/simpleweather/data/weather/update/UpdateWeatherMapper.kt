package com.github.skytoph.simpleweather.data.weather.update

import com.github.skytoph.simpleweather.core.Mapper
import com.github.skytoph.simpleweather.core.util.time.CurrentTime
import com.github.skytoph.simpleweather.data.airquality.cloud.model.AirQualityCloud
import com.github.skytoph.simpleweather.data.weather.cloud.mapper.CurrentCloudToDataMapper
import com.github.skytoph.simpleweather.data.weather.cloud.mapper.IndicatorsCloudToDataMapper
import com.github.skytoph.simpleweather.data.weather.cloud.mapper.WeatherCloudMapper
import com.github.skytoph.simpleweather.data.weather.cloud.model.*
import com.github.skytoph.simpleweather.data.weather.mapper.content.forecast.DailyForecastListDataMapper
import com.github.skytoph.simpleweather.data.weather.mapper.content.forecast.HourlyForecastListDataMapper
import com.github.skytoph.simpleweather.data.weather.mapper.content.forecast.WarningListDataMapper
import com.github.skytoph.simpleweather.data.weather.mapper.content.horizon.HorizonDataMapper
import com.github.skytoph.simpleweather.data.weather.model.WeatherData
import com.github.skytoph.simpleweather.data.weather.model.content.ContentData
import com.github.skytoph.simpleweather.data.weather.model.content.current.CurrentWeatherData
import com.github.skytoph.simpleweather.data.weather.model.content.forecast.ForecastData
import com.github.skytoph.simpleweather.data.weather.model.content.horizon.HorizonData
import com.github.skytoph.simpleweather.data.weather.model.content.indicators.IndicatorsData
import com.github.skytoph.simpleweather.data.weather.model.identifier.IdentifierData
import com.github.skytoph.simpleweather.data.weather.model.time.ForecastTimeData
import javax.inject.Inject

interface UpdateWeatherMapper : Mapper<WeatherData> {
    fun update(
        weatherData: WeatherData,
        weatherCloud: WeatherCloud,
        airQualityCloud: AirQualityCloud,
    ): WeatherData

    fun update(weatherData: WeatherData, location: Map<String, String>): WeatherData

    fun update(weatherData: WeatherData): WeatherData

    class Base @Inject constructor(
        private val indicatorsMapper: IndicatorsCloudToDataMapper,
        private val horizonMapper: HorizonDataMapper,
        private val warningsMapper: WarningListDataMapper,
        private val hourlyMapper: HourlyForecastListDataMapper,
        private val dailyMapper: DailyForecastListDataMapper,
        private val currentTime: CurrentTime,
    ) : UpdateWeatherMapper {

        override fun update(
            weatherData: WeatherData,
            weatherCloud: WeatherCloud,
            airQualityCloud: AirQualityCloud,
        ): WeatherData = weatherData.update(object : UpdateWeather {

            override fun update(
                identifier: IdentifierData, time: ForecastTimeData, content: ContentData,
            ): WeatherData = content.update(object : UpdateContent {

                override fun update(
                    currentWeather: CurrentWeatherData,
                    indicators: IndicatorsData,
                    horizon: HorizonData,
                    forecast: ForecastData,
                ): WeatherData = weatherCloud.map(object : WeatherCloudMapper {

                    override fun map(
                        current: CurrentWeatherCloud,
                        timezoneOffset: Int,
                        timezone: String,
                        hourly: List<HourlyForecastCloud>,
                        daily: List<DailyForecastCloud>,
                        alerts: List<AlertCloud>,
                    ): WeatherData = current.map(object : CurrentCloudToDataMapper {

                        override fun map(
                            dt: Long,
                            temp: Double,
                            sunrise: Long,
                            sunset: Long,
                            uvi: Double,
                            weather: Int,
                        ): WeatherData {
                            val currentMapper = object : UpdateCurrentWeather {
                                override fun update(location: Map<String, String>) =
                                    CurrentWeatherData(weather, temp, location)
                            }
                            val pop = hourly.firstOrNull()?.map() ?: 0.0
                            return WeatherData(
                                identifier,
                                ForecastTimeData(dt, timezoneOffset, timezone),
                                ContentData(
                                    currentWeather.update(currentMapper),
                                    indicatorsMapper.map(uvi, pop, airQualityCloud.map()),
                                    horizonMapper.map(sunrise, sunset),
                                    ForecastData(
                                        warningsMapper.map(alerts),
                                        hourlyMapper.map(hourly),
                                        dailyMapper.map(daily)
                                    )
                                )
                            )
                        }
                    })
                })
            })
        })

        override fun update(weatherData: WeatherData, location: Map<String, String>): WeatherData =
            weatherData.update(object : UpdateWeather {
                override fun update(
                    identifier: IdentifierData, time: ForecastTimeData, content: ContentData,
                ): WeatherData = content.update(object : UpdateForecastContent {
                    override fun update(
                        currentWeather: CurrentWeatherData,
                        indicators: IndicatorsData,
                        horizon: HorizonData,
                        forecast: ForecastData,
                    ): WeatherData = currentWeather.update(object : UpdateCurrentWeatherLocation {
                        override fun update(weatherId: Int, temperature: Double) =
                            WeatherData(
                                identifier,
                                time,
                                ContentData(
                                    CurrentWeatherData(weatherId, temperature, location),
                                    indicators,
                                    horizon,
                                    forecast
                                )
                            )
                    })
                })
            })

        override fun update(weatherData: WeatherData): WeatherData =
            weatherData.update(object : UpdateWeather {
                override fun update(
                    identifier: IdentifierData, time: ForecastTimeData, content: ContentData,
                ): WeatherData = time.update(object : UpdateForecastTime {
                    override fun update(timezoneOffset: Int, timezone: String): WeatherData =
                        WeatherData(
                            identifier,
                            ForecastTimeData(
                                currentTime.inSeconds(),
                                timezoneOffset,
                                timezone
                            ),
                            content
                        )
                })
            })
    }
}