package com.github.skytoph.simpleweather.data.location.cloud

import com.github.skytoph.simpleweather.core.provider.LocaleProvider
import com.github.skytoph.simpleweather.data.location.cloud.model.PlaceCoordinatesCloud
import javax.inject.Inject

interface PlaceCoordinatesDataSource {
    suspend fun find(placeId: String): PlaceCoordinatesCloud
    suspend fun find(coordinates: Pair<Double, Double>): PlaceCoordinatesCloud

    class Base @Inject constructor(
        private val service: PlaceCoordinatesService,
        private val idMapper: IdMapper,
        private val locale: LocaleProvider
    ) : PlaceCoordinatesDataSource {

        override suspend fun find(placeId: String): PlaceCoordinatesCloud =
            service.getPlace(placeId, locale.locale().language).execute().body()!!

        override suspend fun find(coordinates: Pair<Double, Double>): PlaceCoordinatesCloud =
            service.getPlaceByLatLng(
                idMapper.map(coordinates.first, coordinates.second), locale.locale().language
            ).execute().body()!!
    }
}