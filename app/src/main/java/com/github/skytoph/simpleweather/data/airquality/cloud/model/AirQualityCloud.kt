package com.github.skytoph.simpleweather.data.airquality.cloud.model

import com.github.skytoph.simpleweather.core.MappableTo
import com.squareup.moshi.Json

data class AirQualityCloud(
    @Json(name = "current")
    private val airQuality: AirQualityCurrentCloud?,
) : MappableTo<Int> {

    override fun map(): Int = airQuality?.map() ?: DEFAULT

    companion object {
        const val DEFAULT: Int = -1
    }
}

data class AirQualityCurrentCloud(
    @Json(name = "european_aqi")
    private val aqi: Int,
    @Json(name = "time")
    private val dt: Long,
) : MappableTo<Int> {

    override fun map(): Int = aqi
}