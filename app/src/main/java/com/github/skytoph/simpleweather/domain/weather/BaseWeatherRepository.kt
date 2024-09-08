package com.github.skytoph.simpleweather.domain.weather

import com.github.skytoph.simpleweather.core.exception.FavoritesLimitException
import com.github.skytoph.simpleweather.data.location.cloud.PlaceData
import com.github.skytoph.simpleweather.data.weather.WeatherCache
import com.github.skytoph.simpleweather.data.weather.cache.WeatherCacheDataSource
import com.github.skytoph.simpleweather.data.weather.cache.mapper.WeatherDBToDataMapper
import com.github.skytoph.simpleweather.data.weather.cloud.WeatherCloudDataSource
import com.github.skytoph.simpleweather.data.weather.model.WeatherData
import com.github.skytoph.simpleweather.domain.weather.mapper.CompareTimeWithCurrent
import com.github.skytoph.simpleweather.domain.weather.mapper.UpdatedLately
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseWeatherRepository @Inject constructor(
    private val cacheDataSource: WeatherCacheDataSource,
    private val cloudDataSource: WeatherCloudDataSource,
    private val cacheMapper: WeatherDBToDataMapper,
    private val cachedWeather: WeatherCache,
    private val timeMapper: CompareTimeWithCurrent,
) : WeatherRepository.Base {

    override fun cachedIDs(): List<String> =
        cacheDataSource.readAllIDs()

    override suspend fun getCachedWeather(id: String): WeatherData =
        cacheDataSource.read(id).map(cacheMapper)

    override suspend fun getCachedAndUpdateLocation(
        id: String, location: String, language: String
    ): WeatherData = getCachedWeather(id).let { data ->
        if (location.isBlank() || data.containsLocation(language)) data
        else {
            cacheDataSource.updateLocation(id, data, mapOf(language to location))
            getCachedWeather(id)
        }
    }

    override suspend fun isCached(id: String): Boolean = try {
        getCachedWeather(id).let { true }
    } catch (exception: Exception) {
        false
    }

    override suspend fun getCloudWeather(location: PlaceData): WeatherData =
        cloudDataSource.fetch(location).also { cachedWeather.cache(it) }

    override suspend fun updateCloudWeather(id: String): WeatherData =
        updateAndSave(getCachedWeather(id))

    override suspend fun checkReachingLimit(limit: Int) {
        if (cachedIDs().size >= limit) throw FavoritesLimitException(limit)
    }

    override suspend fun saveWeather() = cachedWeather.save(cacheDataSource)

    override suspend fun refreshAll(criteria: UpdatedLately): Boolean =
        cacheDataSource.readAll().map { it.map(cacheMapper) }.map { forecast ->
            val updated = !forecast.updatedLately(timeMapper, criteria)
            if (updated) updateAndSave(forecast)
            updated
        }.contains(true)

    private suspend fun updateAndSave(data: WeatherData): WeatherData =
        data.update(cloudDataSource).also { it.save(cacheDataSource) }

    override suspend fun delete(id: String) =
        cacheDataSource.remove(id)
}