package com.github.skytoph.simpleweather.data.weather.mapper.content.forecast

import com.github.skytoph.simpleweather.core.domain.mapper.WeatherImageId
import javax.inject.Inject

interface WeatherCodeMapper {
    fun map(code: Int, pop: Double, clouds: Double): Int

    class Base @Inject constructor() : WeatherCodeMapper {
        override fun map(code: Int, pop: Double, clouds: Double): Int = when (code) {
            in WeatherImageId.RANGE_RAIN_DRIZZLE,
            in WeatherImageId.RANGE_RAIN,
            in WeatherImageId.RANGE_RAIN_SHOWERS,
            in WeatherImageId.RANGE_SNOW_FALL,
            in WeatherImageId.RANGE_SNOW_SHOWERS,
            in WeatherImageId.RANGE_FREEZING_DRIZZLE,
            in WeatherImageId.RANGE_FREEZING_RAIN -> when {
                pop < POP_MIN -> when {
                    clouds < CLOUDS_PARTLY -> WeatherImageId.RANGE_CLEAR.first
                    clouds < CLOUDS_OVERCAST -> WeatherImageId.VALUE_PARTLY_CLOUDY
                    else -> WeatherImageId.VALUE_CLOUDY
                }

                clouds < CLOUDS_OVERCAST -> code + WeatherImageId.PARTLY_CLOUDY

                else -> code
            }

            in WeatherImageId.RANGE_THUNDER ->
                if (pop >= POP_MIN) code + WeatherImageId.THUNDERSTORM_WITH_RAIN else code

            else -> code
        }

        private companion object {
            const val CLOUDS_OVERCAST = 65
            const val CLOUDS_PARTLY = 30
            const val POP_MIN = 25
        }
    }
}
