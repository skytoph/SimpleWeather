package com.github.skytoph.simpleweather.data.location.mapper

import com.github.skytoph.simpleweather.core.Mapper
import com.github.skytoph.simpleweather.core.provider.LocaleProvider
import com.github.skytoph.simpleweather.data.location.cloud.PlaceData
import com.github.skytoph.simpleweather.data.location.cloud.model.LocationCloud
import com.github.skytoph.simpleweather.data.location.cloud.model.PlaceCoordinatesCloud
import com.github.skytoph.simpleweather.data.weather.mapper.LocationCloudToDataMapper
import javax.inject.Inject

interface PlaceCloudToDataMapper : Mapper<PlaceData> {
    fun map(data: PlaceCoordinatesCloud): PlaceData
    fun map(data: LocationCloud, placeId: String): PlaceData

    class Base @Inject constructor(
        private val mapper: LocationCloudToDataMapper,
        private val locale: LocaleProvider
    ) : PlaceCloudToDataMapper {

        override fun map(data: LocationCloud, placeId: String): PlaceData =
            data.map(object : PlaceDataMapper {
                override fun map(
                    name: String,
                    localNames: Map<String, String?>,
                    lat: Double,
                    lng: Double
                ) = PlaceData(placeId, mapper.map(localNames, name), lat, lng)
            })

        override fun map(data: PlaceCoordinatesCloud): PlaceData = PlaceData(
            data.placeId,
            mapOf(mapper.map(data.name ?: "", locale.locale())),
            data.lat,
            data.lng
        )
    }
}

interface PlaceDataMapper : Mapper<PlaceData> {
    fun map(name: String, localNames: Map<String, String?>, lat: Double, lng: Double): PlaceData
}