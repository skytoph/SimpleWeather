package com.github.skytoph.simpleweather.data.weather.cache.mapper

import com.github.skytoph.simpleweather.core.Mapper
import com.github.skytoph.simpleweather.data.weather.cache.CurrentDB
import javax.inject.Inject

interface CurrentDBMapper : Mapper<CurrentDB> {

    fun map(weatherId: Int, temperature: Double): CurrentDB

    class Base @Inject constructor() : CurrentDBMapper {

        override fun map(weatherId: Int, temperature: Double) = CurrentDB().apply {
            this.weatherCode = weatherId
            this.temperature = temperature
        }
    }
}
