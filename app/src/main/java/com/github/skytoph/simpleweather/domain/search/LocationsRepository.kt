package com.github.skytoph.simpleweather.domain.search

import com.github.skytoph.simpleweather.data.location.CurrentLocationCoordinates
import com.github.skytoph.simpleweather.data.search.cache.SearchHistoryCache
import com.github.skytoph.simpleweather.data.search.cache.SearchHistoryData
import com.github.skytoph.simpleweather.data.search.cache.mapper.SearchHistoryDataMapper
import com.github.skytoph.simpleweather.data.search.cache.mapper.SearchHistoryListDataMapper
import javax.inject.Inject

interface LocationsRepository {
    suspend fun saveSearchLocation(placeId: String, location: String)
    suspend fun clearSearchHistory()
    suspend fun currentPlace(): Pair<Double, Double>
    suspend fun searchHistory(): List<SearchHistoryData>

    class Base @Inject constructor(
        private val searchHistoryCache: SearchHistoryCache,
        private val currentLocation: CurrentLocationCoordinates,
        private val listMapper: SearchHistoryListDataMapper,
        private val mapper: SearchHistoryDataMapper,
    ) : LocationsRepository {

        override suspend fun searchHistory(): List<SearchHistoryData> =
            listMapper.map(searchHistoryCache.readAll())

        override suspend fun saveSearchLocation(placeId: String, location: String) =
            searchHistoryCache.saveOrUpdate(placeId, mapper.map(placeId, location))

        override suspend fun clearSearchHistory() =
            searchHistoryCache.clear()

        override suspend fun currentPlace(): Pair<Double, Double> =
            currentLocation.placeCoordinates()
    }
}
