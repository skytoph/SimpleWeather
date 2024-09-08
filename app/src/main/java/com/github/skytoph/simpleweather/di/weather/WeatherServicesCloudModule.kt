package com.github.skytoph.simpleweather.di.weather

import com.github.skytoph.simpleweather.data.airquality.AirQualityService
import com.github.skytoph.simpleweather.data.weather.cloud.ForecastService
import com.github.skytoph.simpleweather.di.core.RetrofitWeather
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object WeatherServicesCloudModule {

    @Provides
    fun forecastService(@RetrofitWeather retrofit: Retrofit): ForecastService =
        retrofit.create(ForecastService::class.java)

    @Provides
    fun airQualityService(@RetrofitWeather retrofit: Retrofit): AirQualityService =
        retrofit.create(AirQualityService::class.java)
}