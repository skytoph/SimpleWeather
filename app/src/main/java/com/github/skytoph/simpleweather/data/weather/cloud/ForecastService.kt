package com.github.skytoph.simpleweather.data.weather.cloud

import com.github.skytoph.simpleweather.data.weather.cloud.model.WeatherCloud
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastService {
    @GET("https://api.open-meteo.com/v1/forecast?current=temperature_2m,precipitation_probability,weather_code,cloud_cover,uv_index&hourly=temperature_2m,precipitation_probability,weather_code,cloud_cover,uv_index&daily=weather_code,temperature_2m_max,temperature_2m_min,sunrise,sunset,precipitation_probability_mean&timeformat=unixtime&timezone=auto&models=best_match\n")
    fun getForecast(
        @Query(value = "latitude", encoded = true) lat: String,
        @Query(value = "longitude", encoded = true) lng: String,
    ): Call<WeatherCloud>
}