package com.github.skytoph.simpleweather.core.util.time

import com.github.skytoph.simpleweather.data.weather.mapper.Timezone
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface CurrentTime {
    fun inMillis(): Long
    fun inSeconds(): Long
    fun hoursInSeconds(): Long
    fun dayInSeconds(): Long
    fun dayInSeconds(offset: Timezone): Long

    class Base @Inject constructor() : CurrentTime {

        override fun inMillis(): Long = Calendar.getInstance().timeInMillis

        override fun inSeconds(): Long =
            TimeUnit.MILLISECONDS.toSeconds(inMillis())

        override fun hoursInSeconds(): Long =
            RoundedTime(TimeSeconds.FromMillis(inMillis())).roundToHours()

        override fun dayInSeconds(): Long =
            RoundedTime(TimeSeconds.FromMillis(inMillis())).roundToDay()

        override fun dayInSeconds(offset: Timezone): Long =
            RoundedTime(TimeSeconds.FromMillis(offset.withOffset(inMillis()))).roundToDay()
    }
}