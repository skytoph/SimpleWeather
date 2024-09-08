package com.github.skytoph.simpleweather.data.location.cloud

import com.github.skytoph.simpleweather.data.location.mapper.PlaceCloudToDataMapper
import javax.inject.Inject

interface PlaceCloudDataSource {
    suspend fun place(placeId: String): PlaceData
    suspend fun place(coordinates: Pair<Double, Double>): PlaceData

    class Base @Inject constructor(
        private val coordinatesDataSource: PlaceCoordinatesDataSource,
        private val mapperData: PlaceCloudToDataMapper,
    ) : PlaceCloudDataSource {

        override suspend fun place(placeId: String): PlaceData =
            mapperData.map(coordinatesDataSource.find(placeId))

        override suspend fun place(coordinates: Pair<Double, Double>): PlaceData =
            mapperData.map(coordinatesDataSource.find(coordinates))
    }
}