package com.github.skytoph.simpleweather.data.airquality

import com.github.skytoph.simpleweather.data.airquality.cloud.model.AirQualityCloud
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AirQualityService {

    @GET("https://air-quality-api.open-meteo.com/v1/air-quality?current=european_aqi&timeformat=unixtime&timezone=auto")
    fun getAirQuality(
        @Query(value = "latitude", encoded = true) lat: String,
        @Query(value = "longitude", encoded = true) lng: String,
    ): Call<AirQualityCloud>
}
