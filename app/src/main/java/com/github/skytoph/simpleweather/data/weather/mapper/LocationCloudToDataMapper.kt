package com.github.skytoph.simpleweather.data.weather.mapper

import com.github.skytoph.simpleweather.data.location.mapper.LocalNameDataMapper
import java.util.Locale
import javax.inject.Inject

interface LocationCloudToDataMapper {
    fun map(location: Map<String, String?>, defaultName: String): Map<String, String>
    fun map(location: String, locale: Locale): Pair<String, String>

    class Base @Inject constructor(private val mapper: LocalNameDataMapper) :
        LocationCloudToDataMapper {

        override fun map(location: Map<String, String?>, defaultName: String): Map<String, String> =
            location.mapNotNull { (key, value) -> value?.let { mapper.map(Locale(key), it) } }
                .toMutableList()
                .apply { add(mapper.map(name = defaultName)) }
                .toMap()

        override fun map(location: String, locale: Locale): Pair<String, String> =
            mapper.map(locale, location)
    }
}