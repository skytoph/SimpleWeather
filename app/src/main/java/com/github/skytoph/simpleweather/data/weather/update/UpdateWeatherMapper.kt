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
import com.github.skytoph.simpleweather.data.weather.model.CurrentWeatherData
import com.github.skytoph.simpleweather.data.weather.model.WeatherData
import javax.inject.Inject

interface UpdateWeatherMapper : Mapper<WeatherData> {
    fun update(
        weatherData: WeatherData,
        weatherCloud: WeatherCloud,
        airQualityCloud: AirQualityCloud,
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

            override fun update(id: String, currentWeatherData: CurrentWeatherData): WeatherData =
                weatherCloud.map(object : WeatherCloudMapper {

                    override fun map(
                        current: CurrentWeatherCloud,
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
                            return WeatherData(
                                id,
                                currentWeatherData.update(currentMapper),
                                indicatorsDataMapper.map(dt, temp, pop, airQualityCloud.map()),
                                horizonDataMapper.map(sunrise, sunset, dt),
                                alertsMapper.map(alerts, pop),
                                hourlyMapper.map(hourly),
                                dailyMapper.map(daily)
                            )
                        }
                    })
                })
        })
    }

}