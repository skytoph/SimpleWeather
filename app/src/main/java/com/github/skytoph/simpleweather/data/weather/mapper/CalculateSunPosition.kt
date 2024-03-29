package com.github.skytoph.simpleweather.data.weather.mapper

import com.github.skytoph.simpleweather.core.util.time.RoundedTime
import com.github.skytoph.simpleweather.core.util.time.TimeSeconds
import com.github.skytoph.simpleweather.data.weather.model.content.horizon.IsDaytime
import java.util.concurrent.TimeUnit

interface CalculateSunPosition : IsDaytime {
    fun dayLength(): Int
    fun remainingDaylight(): Int
    fun sunPosition(): Double

    abstract class Abstract(
        protected val sunrise: Int,
        protected val sunset: Int,
        protected val current: Int,
    ) : CalculateSunPosition {

        constructor(sunrise: Long, sunset: Long, current: Long) :
                this(sunrise.mod(DATE_MOD), sunset.mod(DATE_MOD), current.mod(DATE_MOD)) {
            if (sunrise > sunset) throw IllegalStateException("sunrise and sunset arguments are incorrect")
        }

        override fun dayLength(): Int = sunset - sunrise

        override fun remainingDaylight(): Int =
            when {
                current in sunrise until sunset -> sunset - current
                current < sunrise && sunset > sunrise -> dayLength()
                else -> 0
            }

        override fun sunPosition(): Double {
            val nightLength by lazy { DAY_IN_SECONDS - dayLength() }

            return when {
                current in sunrise until sunset -> (current - sunrise).toDouble() / dayLength()

                current > sunset -> -(current - sunset).toDouble() / nightLength

                current < sunrise -> (sunrise - current).toDouble() / nightLength - 1

                else -> 0.0
            }
        }

        override fun isDaytime(time: Long): Boolean {
            val roundedSunrise = RoundedTime(TimeSeconds.Base(sunrise.toLong())).roundToHalfHours()
            val roundedSunset = RoundedTime(TimeSeconds.Base(sunset.toLong())).roundToHalfHours()
            return time.mod(DATE_MOD) in roundedSunrise until roundedSunset
        }

        private companion object {
            val DAY_IN_SECONDS = TimeUnit.DAYS.toSeconds(1)
            const val DATE_MOD = 86400
        }
    }
}