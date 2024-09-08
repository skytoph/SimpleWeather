package com.github.skytoph.simpleweather.data.weather.cloud.model

import com.github.skytoph.simpleweather.core.Mappable
import com.github.skytoph.simpleweather.data.weather.mapper.content.forecast.DailyForecastDataListMapper
import com.github.skytoph.simpleweather.data.weather.model.content.forecast.DailyForecastData
import com.squareup.moshi.Json

/*
  "daily": {
    "time": [1725310800],
    "weather_code": [3],
    "temperature_2m_max": [29.3],
    "temperature_2m_min": [20.4],
    "sunrise": [1725333048],
    "sunset": [1725380672],
    "uv_index": [0],
    "precipitation_probability_max": [46]
  }
*/

data class DailyForecastCloud(
    @Json(name = "time")
    private val dt: List<Long>? = emptyList(),

    @Json(name = "weather_code")
    private val weather: List<Int>? = emptyList(),

    @Json(name = "temperature_2m_min")
    private val tempMin: List<Double>? = emptyList(),

    @Json(name = "temperature_2m_max")
    private val tempMax: List<Double>? = emptyList(),

    @Json(name = "precipitation_probability_mean")
    private val pop: List<Double>? = emptyList(),

    @Json(name = "uv_index")
    private val uvi: List<Double>? = emptyList(),

    @Json(name = "sunrise")
    private val sunrise: List<Long>? = emptyList(),

    @Json(name = "sunset")
    private val sunset: List<Long>? = emptyList(),
) : Mappable<List<DailyForecastData>, DailyForecastDataListMapper> {

    val horizon: Pair<Long, Long>
        get() = (sunrise?.first() ?: 0L) to (sunset?.first() ?: 0L)

    override fun map(mapper: DailyForecastDataListMapper): List<DailyForecastData> =
        mapper.map(
            time = dt ?: emptyList(),
            tempMin = tempMin ?: emptyList(),
            tempMax = tempMax ?: emptyList(),
            weatherId = weather ?: emptyList(),
            pop = pop ?: emptyList(),
            uvi = uvi ?: emptyList(),
            sunrise = sunrise ?: emptyList(),
            sunset = sunset ?: emptyList(),
        )

    companion object {
        val empty: DailyForecastCloud = DailyForecastCloud()
    }
}