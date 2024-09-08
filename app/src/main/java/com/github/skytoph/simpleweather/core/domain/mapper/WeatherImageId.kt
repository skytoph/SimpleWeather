package com.github.skytoph.simpleweather.core.domain.mapper

import com.github.skytoph.simpleweather.R
import com.github.skytoph.simpleweather.presentation.weather.model.WeatherResources

abstract class WeatherImageId {

    protected fun weatherImageRes(code: Int, isDaytime: Boolean = true): WeatherResources = when (code) {
        in RANGE_CLEAR -> WeatherResources(
            imageId = if (isDaytime) R.drawable.weather_sun else R.drawable.weather_moon,
            titleId = R.string.weather_clear
        )

        VALUE_PARTLY_CLOUDY -> WeatherResources(
            imageId = if (isDaytime) R.drawable.weather_clouds_sun else R.drawable.weather_clouds_moon,
            titleId = R.string.weather_partly_cloudy
        )

        VALUE_CLOUDY -> WeatherResources(imageId = R.drawable.weather_clouds, titleId = R.string.weather_cloudy)

        in RANGE_RAIN_DRIZZLE, in RANGE_RAIN, in RANGE_RAIN_SHOWERS ->
            WeatherResources(imageId = R.drawable.weather_rain, titleId = rainDescription(code))

        in RANGE_RAIN_DRIZZLE_PARTLY_CLOUDY, in RANGE_RAIN_PARTLY_CLOUDY, in RANGE_RAIN_SHOWERS_PARTLY_CLOUDY -> WeatherResources(
            imageId = if (isDaytime) R.drawable.weather_sun_rain else R.drawable.weather_moon_rain,
            titleId = rainDescription(code)
        )

        in RANGE_THUNDER -> WeatherResources(
            imageId = R.drawable.weather_thunderstorm,
            titleId = if (code in 96..99) R.string.weather_thunderstorm_with_hail else R.string.weather_thunderstorm
        )

        in RANGE_THUNDER_RAIN ->
            WeatherResources(imageId = R.drawable.weather_rain_thunderstorm, titleId = R.string.weather_thunderstorm_with_rain)

        in RANGE_SNOW_FALL, in RANGE_SNOW_SHOWERS ->
            WeatherResources(imageId = R.drawable.weather_snow, titleId = snowDescription(code))

        in RANGE_SNOW_FALL_PARTLY_CLOUDY, in RANGE_SNOW_SHOWERS_PARTLY_CLOUDY -> WeatherResources(
            imageId = if (isDaytime) R.drawable.weather_sun_snow else R.drawable.weather_moon_snow,
            titleId = snowDescription(code)
        )

        in RANGE_FREEZING_DRIZZLE, in RANGE_FREEZING_RAIN ->
            WeatherResources(imageId = R.drawable.weather_snow_rain, titleId = snowRainDescription(code))

        in RANGE_FREEZING_DRIZZLE_PARTLY_CLOUDY, in RANGE_FREEZING_RAIN_PARTLY_CLOUDY -> WeatherResources(
            imageId = if (isDaytime) R.drawable.weather_sun_snow_rain else R.drawable.weather_moon_snow_rain,
            titleId = snowRainDescription(code)
        )

        in RANGE_FOG -> WeatherResources(imageId = R.drawable.weather_fog, titleId = R.string.weather_fog)

        else -> WeatherResources(
            imageId = if (isDaytime) R.drawable.weather_clouds_sun else R.drawable.weather_clouds_moon,
            titleId = R.string.weather_partly_cloudy
        )
    }

    private fun rainDescription(code: Int) = when (code) {
        51, 151 -> R.string.weather_rain_light_drizzle
        53, 153 -> R.string.weather_rain_moderate_drizzle
        55, 155 -> R.string.weather_rain_dense_intensity_drizzle
        61, 161 -> R.string.weather_rain_slight
        63, 163 -> R.string.weather_rain_moderate
        65, 165 -> R.string.weather_rain_heavy
        80, 180 -> R.string.weather_rain_shower_slight
        81, 181 -> R.string.weather_rain_shower_moderate
        82, 182 -> R.string.weather_rain_shower_violent
        else -> R.string.weather_rain
    }

    private fun snowDescription(code: Int) = when (code) {
        71, 171 -> R.string.weather_snow_fall_slight
        73, 173 -> R.string.weather_snow_fall_moderate
        75, 175 -> R.string.weather_snow_fall_heavy
        77, 177 -> R.string.weather_snow_grains
        85, 185 -> R.string.weather_snow_shower_slight
        86, 186 -> R.string.weather_snow_shower_moderate
        else -> R.string.weather_snow
    }

    private fun snowRainDescription(code: Int) = when (code) {
        56, 156 -> R.string.weather_freezing_drizzle_light
        57, 157 -> R.string.weather_freezing_drizzle_dense
        66, 166 -> R.string.weather_freezing_rain_light
        67, 167 -> R.string.weather_freezing_rain_dense
        else -> R.string.weather_freezing_rain
    }

    companion object {
        const val PARTLY_CLOUDY = 100
        const val THUNDERSTORM_WITH_RAIN = 500
        const val CODE_DEFAULT = 2
        val RANGE_CLEAR = 0..1
        const val VALUE_PARTLY_CLOUDY = 2
        const val VALUE_CLOUDY = 3
        val RANGE_FOG = 45..48
        val RANGE_RAIN_DRIZZLE = 51..55
        val RANGE_FREEZING_DRIZZLE = 56..57
        val RANGE_RAIN = 61..65
        val RANGE_FREEZING_RAIN = 66..67
        val RANGE_SNOW_FALL = 71..77
        val RANGE_RAIN_SHOWERS = 80..82
        val RANGE_SNOW_SHOWERS = 85..86
        val RANGE_THUNDER = 95..99
        val RANGE_THUNDER_RAIN = 95 + THUNDERSTORM_WITH_RAIN..99 + THUNDERSTORM_WITH_RAIN
        val RANGE_RAIN_DRIZZLE_PARTLY_CLOUDY = 151..155
        val RANGE_FREEZING_DRIZZLE_PARTLY_CLOUDY = 156..157
        val RANGE_RAIN_PARTLY_CLOUDY = 161..165
        val RANGE_SNOW_FALL_PARTLY_CLOUDY = 171..177
        val RANGE_RAIN_SHOWERS_PARTLY_CLOUDY = 180..182
        val RANGE_SNOW_SHOWERS_PARTLY_CLOUDY = 185..186
        val RANGE_FREEZING_RAIN_PARTLY_CLOUDY = 166..167
    }
}