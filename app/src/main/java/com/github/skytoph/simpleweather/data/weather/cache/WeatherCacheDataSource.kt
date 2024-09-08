package com.github.skytoph.simpleweather.data.weather.cache

import com.github.skytoph.simpleweather.core.data.DataBase
import com.github.skytoph.simpleweather.core.data.RealmProvider
import com.github.skytoph.simpleweather.core.data.SaveItem
import com.github.skytoph.simpleweather.core.data.UpdateItemTime
import com.github.skytoph.simpleweather.core.exception.DataIsNotCachedException
import com.github.skytoph.simpleweather.core.exception.NoCachedDataException
import com.github.skytoph.simpleweather.data.weather.cache.mapper.WeatherDataDBMapper
import com.github.skytoph.simpleweather.data.weather.cache.model.WeatherDB
import com.github.skytoph.simpleweather.data.weather.model.WeatherData
import com.github.skytoph.simpleweather.data.weather.model.identifier.IdentifierData
import com.github.skytoph.simpleweather.data.weather.update.UpdateWeatherMapper
import io.realm.Realm
import javax.inject.Inject
import javax.inject.Singleton

interface WeatherCacheDataSource : SaveItem<WeatherData>,
    UpdateItemTime<WeatherData, IdentifierData> {

    fun read(id: String): WeatherDB
    fun readAll(): List<WeatherDB>
    fun readAllIDs(): List<String>
    fun remove(id: String)
    fun updateLocation(id: String, data: WeatherData, locations: Map<String, String>)

    @Singleton
    class Base @Inject constructor(
        private val realmProvider: RealmProvider,
        private val mapper: WeatherDataDBMapper,
        private val updateMapper: UpdateWeatherMapper,
    ) : WeatherCacheDataSource {

        override fun read(id: String): WeatherDB = realmProvider.provide().use { realm ->
            return findRealmObject(realm, id)?.let { realm.copyFromRealm(it) }
                ?: throw DataIsNotCachedException(id)
        }

        override fun readAll(): List<WeatherDB> = realmProvider.provide().use { realm ->
            return realm.where(WeatherDB::class.java).findAll().let { realm.copyFromRealm(it) }
                ?.sortedBy { it.identifier!!.priority }
                ?: throw NoCachedDataException()
        }

        override fun readAllIDs(): List<String> = readAll().map { it.id }

        override fun remove(id: String) = realmProvider.provide().use { realm ->
            realm.executeTransaction {
                findRealmObject(realm, id)?.deleteFromRealm()
            }
        }

        override fun saveOrUpdate(id: String, data: WeatherData) =
            realmProvider.provide().use { realm ->
                realm.executeTransaction {
                    val weather = findRealmObject(realm, id)
                    weather?.deleteFromRealm()
                    it.insert(data.map(mapper, DataBase(it)))
                }
            }

        override fun updateLocation(id: String, data: WeatherData, locations: Map<String, String>) =
            saveOrUpdate(id, updateMapper.update(data, locations))

        override suspend fun updateTime(data: WeatherData, id: IdentifierData) =
            saveOrUpdate(id.map(), updateMapper.update(data))

        private fun findRealmObject(realm: Realm, value: String) =
            realm.where(WeatherDB::class.java).equalTo(WeatherDB.FIELD_ID, value).findFirst()
    }
}