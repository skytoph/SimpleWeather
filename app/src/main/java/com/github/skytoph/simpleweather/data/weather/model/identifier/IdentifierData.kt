package com.github.skytoph.simpleweather.data.weather.model.identifier

import com.github.skytoph.simpleweather.core.MappableTo
import com.github.skytoph.simpleweather.core.MappableToDB
import com.github.skytoph.simpleweather.core.data.DataBase
import com.github.skytoph.simpleweather.data.location.cloud.IdMapper
import com.github.skytoph.simpleweather.data.location.cloud.LocationCloudDataSource
import com.github.skytoph.simpleweather.data.weather.cache.mapper.identifier.IdentifierDBMapper
import com.github.skytoph.simpleweather.data.weather.cache.model.identifier.IdentifierDB
import com.github.skytoph.simpleweather.domain.weather.RefreshLocation
import com.github.skytoph.simpleweather.domain.weather.SaveState

data class IdentifierData(
    private val id: String,
    private val placeId: String,
    private val favorite: Boolean,
    private val priority: Int = 0,
) : SaveState, IdMapper.MappableToCoordinates,
    MappableToDB.Base<IdentifierDB, IdentifierDBMapper>,
    MappableTo<String> {

    override fun saveState(refreshLocation: RefreshLocation.SaveRefreshed) =
        refreshLocation.locationRefreshed(id)

    override fun map(mapper: IdMapper): Pair<Double, Double> = mapper.map(id)

    override fun map(mapper: IdentifierDBMapper, dataBase: DataBase): IdentifierDB =
        mapper.map(id, placeId, priority, dataBase)

    override fun map(): String = id

    suspend fun placeName(source: LocationCloudDataSource.PlaceNameSearch) = source.placeName(placeId)
}