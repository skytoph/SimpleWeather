package com.github.skytoph.simpleweather.data.weather.mapper

import com.github.skytoph.simpleweather.core.Mappable
import com.github.skytoph.simpleweather.core.Mapper
import javax.inject.Inject

interface Timezone : Mappable<String, TimezoneToStringMapper> {
    fun withOffset(time: Long): Long
    fun withoutOffset(time: Long): Long

    class Base(private val timezone: String, private val offset: Int) : Timezone {
        override fun withOffset(time: Long): Long = time + offset
        override fun withoutOffset(time: Long): Long = time - offset
        override fun map(mapper: TimezoneToStringMapper): String = mapper.map(timezone)
    }
}

interface TimezoneMapper : Mapper<Timezone> {
    fun map(timezoneOffset: Int, timezone: String): Timezone
}

interface TimezoneToStringMapper : Mapper<String> {
    fun map(timezone: String): String

    class Base @Inject constructor() : TimezoneToStringMapper {
        override fun map(timezone: String): String = timezone
    }
}

interface ProvidesOffset {
    fun map(mapper: TimezoneMapper): Timezone
}