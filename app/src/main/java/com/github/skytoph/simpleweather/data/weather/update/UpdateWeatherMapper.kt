package com.github.skytoph.simpleweather.data.weather.update

import com.github.skytoph.simpleweather.core.Mapper
import com.github.skytoph.simpleweather.data.airquality.cloud.AirQualityCloud
import com.github.skytoph.simpleweather.data.weather.cloud.mapper.AlertsDataMapper
import com.github.skytoph.simpleweather.data.weather.cloud.mapper.CurrentCloudToDataMapper
import com.github.skytoph.simpleweather.data.weather.cloud.mapper.WeatherCloudMapper
import com.github.skytoph.simpleweather.data.weather.cloud.model.*
import com.github.skytoph.simpleweather.data.weather.mapper.DailyForecastListDataMapper
import com.github.skytoph.simpleweather.data.weather.mapper.HorizonDataMapper
import com.github.skytoph.simpleweather.data.weather.mapper.HourlyForecastListDataMapper
import com.github.skytoph.simpleweather.data.weather.mapper.IndicatorsDataMapper
import com.github.skytoph.simpleweather.data.weather.model.*
import javax.inject.Inject

interface UpdateWeatherMapper : Mapper<WeatherData> {
    fun update(
        weatherData: WeatherData,
        weatherCloud: WeatherCloud,
        airQualityCloud: AirQualityCloud,
    ): WeatherData

    fun update(
        weatherData: WeatherData,
        location: String,
    ): WeatherData

    class Base @Inject constructor(
        private val indicatorsDataMapper: IndicatorsDataMapper,
        private val horizonDataMapper: HorizonDataMapper,
        private val alertsMapper: AlertsDataMapper,
        private val hourlyMapper: HourlyForecastListDataMapper,
        private val dailyMapper: DailyForecastListDataMapper,
    ) : UpdateWeatherMapper {

        override fun update(
            weatherData: WeatherData,
            weatherCloud: WeatherCloud,
            airQualityCloud: AirQualityCloud,
        ): WeatherData = weatherData.update(object : UpdateWeather {

            override fun update(
                id: String,
                placeId: String,
                priority: Int,
                currentWeatherData: CurrentWeatherData,
            ): WeatherData =
                weatherCloud.map(object : WeatherCloudMapper {

                    override fun map(
                        current: CurrentWeatherCloud,
                        timezoneOffset: Int,
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
                            weather: WeatherTypeCloud,
                        ): WeatherData {
                            val currentMapper = object : UpdateCurrentWeather {
                                override fun update(location: String, favorite: Boolean) =
                                    CurrentWeatherData(weather.map(), temp, location, favorite)
                            }
                            val pop = hourly[0].map()
                            val time = dt + timezoneOffset
                            return WeatherData(
                                id,
                                placeId,
                                currentWeatherData.update(currentMapper),
                                indicatorsDataMapper.map(time, temp, pop, airQualityCloud.map()),
                                horizonDataMapper.map(sunrise, sunset, dt, timezoneOffset),
                                alertsMapper.map(alerts, pop, timezoneOffset),
                                hourlyMapper.map(hourly, timezoneOffset),
                                dailyMapper.map(daily),
                                priority
                            )
                        }
                    })
                })
        })

        override fun update(weatherData: WeatherData, location: String): WeatherData =
            weatherData.update(object : UpdateWeatherLocation {

                override fun update(
                    id: String,
                    placeId: String,
                    currentWeatherData: CurrentWeatherData,
                    indicatorsData: IndicatorsData,
                    horizonData: HorizonData,
                    alertData: List<AlertData>,
                    hourlyForecast: List<HourlyForecastData>,
                    dailyForecast: List<DailyForecastData>,
                    priority: Int,
                ) = WeatherData(id,
                    placeId,
                    currentWeatherData.update(object : UpdateCurrentWeatherLocation {
                        override fun update(
                            weatherId: Int,
                            temperature: Double,
                            favorite: Boolean,
                        ) = CurrentWeatherData(weatherId, temperature, location, favorite)
                    }),
                    indicatorsData,
                    horizonData,
                    alertData, hourlyForecast, dailyForecast,
                    priority)
            })

    }
}