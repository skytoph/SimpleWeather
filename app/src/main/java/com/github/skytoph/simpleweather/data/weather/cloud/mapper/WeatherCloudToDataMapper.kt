package com.github.skytoph.simpleweather.data.weather.cloud.mapper

import com.github.skytoph.simpleweather.core.Mapper
import com.github.skytoph.simpleweather.data.airquality.cloud.model.AirQualityCloud
import com.github.skytoph.simpleweather.data.location.cloud.PlaceData
import com.github.skytoph.simpleweather.data.location.mapper.PlaceCloudMapper
import com.github.skytoph.simpleweather.data.weather.cloud.model.AlertCloud
import com.github.skytoph.simpleweather.data.weather.cloud.model.CurrentWeatherCloud
import com.github.skytoph.simpleweather.data.weather.cloud.model.DailyForecastCloud
import com.github.skytoph.simpleweather.data.weather.cloud.model.HourlyForecastCloud
import com.github.skytoph.simpleweather.data.weather.cloud.model.WeatherCloud
import com.github.skytoph.simpleweather.data.weather.mapper.content.current.CurrentWeatherDataMapper
import com.github.skytoph.simpleweather.data.weather.mapper.content.forecast.DailyForecastDataListMapper
import com.github.skytoph.simpleweather.data.weather.mapper.content.forecast.HourlyForecastDataListMapper
import com.github.skytoph.simpleweather.data.weather.mapper.content.forecast.WarningListDataMapper
import com.github.skytoph.simpleweather.data.weather.mapper.content.forecast.WeatherCodeMapper
import com.github.skytoph.simpleweather.data.weather.mapper.content.horizon.HorizonDataMapper
import com.github.skytoph.simpleweather.data.weather.model.WeatherData
import com.github.skytoph.simpleweather.data.weather.model.content.ContentData
import com.github.skytoph.simpleweather.data.weather.model.content.forecast.ForecastData
import com.github.skytoph.simpleweather.data.weather.model.identifier.IdentifierData
import com.github.skytoph.simpleweather.data.weather.model.time.ForecastTimeData
import javax.inject.Inject

interface WeatherCloudToDataMapper : Mapper<WeatherData> {
    fun map(
        weatherCloud: WeatherCloud,
        airQualityCloud: AirQualityCloud,
        locationCloud: PlaceData,
        favorite: Boolean = false,
    ): WeatherData

    class Base @Inject constructor(
        private val currentMapper: CurrentWeatherDataMapper,
        private val indicatorsMapper: IndicatorsCloudToDataMapper,
        private val horizonMapper: HorizonDataMapper,
        private val warningsMapper: WarningListDataMapper,
        private val hourlyMapper: HourlyForecastDataListMapper,
        private val dailyMapper: DailyForecastDataListMapper,
        private val weatherMapper: WeatherCodeMapper
    ) : WeatherCloudToDataMapper {

        override fun map(
            weatherCloud: WeatherCloud,
            airQualityCloud: AirQualityCloud,
            locationCloud: PlaceData,
            favorite: Boolean,
        ): WeatherData = weatherCloud.map(object : WeatherCloudMapper {

            override fun map(
                current: CurrentWeatherCloud,
                timezoneOffset: Int,
                timezone: String,
                hourly: HourlyForecastCloud,
                daily: DailyForecastCloud,
                alerts: List<AlertCloud>,
            ): WeatherData = locationCloud.map(object : PlaceCloudMapper {

                override fun map(
                    placeId: String, location: Map<String, String>, lat: Double, lng: Double
                ) = current.map(object : CurrentCloudToDataMapper {

                    override fun map(
                        dt: Long,
                        temp: Double,
                        uvi: Double,
                        weather: Int,
                        pop: Double,
                        clouds: Double,
                    ): WeatherData = WeatherData(
                        IdentifierData(placeId, lat, lng, favorite),
                        ForecastTimeData(dt, timezoneOffset, timezone),
                        ContentData(
                            currentMapper.map(
                                weatherMapper.map(weather, pop, clouds), temp, location
                            ),
                            indicatorsMapper.map(uvi, hourly.map(), airQualityCloud.map()),
                            daily.horizon.let { horizonMapper.map(it.first, it.second) },
                            ForecastData(
                                warningsMapper.map(alerts),
                                hourly.map(hourlyMapper),
                                daily.map(dailyMapper)
                            )
                        )
                    )
                })
            })
        })
    }
}