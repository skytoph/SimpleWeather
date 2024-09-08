package com.github.skytoph.simpleweather.core.util.time

import java.util.concurrent.TimeUnit

class RoundedTime(private val time: TimeSeconds) : RoundTime {

    override fun roundToDay(): Long = time.rounded(dayDivider)

    override fun roundToHours(): Long = time.rounded(hourDivider)

    override fun roundToHalfHours(): Long = time.rounded(halfHourDivider)

    private companion object {
        const val dayDivider = 86_400
        const val hourDivider = 3_600
        const val halfHourDivider = 1_800
    }
}

abstract class TimeSeconds(private val time: Long) {
    fun rounded(divider: Int) = time - time % divider

    class FromMillis(millis: Long) : TimeSeconds(TimeUnit.MILLISECONDS.toSeconds(millis))
    class Base(seconds: Long) : TimeSeconds(seconds)
}

interface RoundTime {
    fun roundToDay(): Long
    fun roundToHours(): Long
    fun roundToHalfHours(): Long
}