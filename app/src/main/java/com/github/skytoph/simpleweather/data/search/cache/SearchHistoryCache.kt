package com.github.skytoph.simpleweather.data.search.cache

import com.github.skytoph.simpleweather.core.data.DataBase
import com.github.skytoph.simpleweather.core.data.RealmProvider
import com.github.skytoph.simpleweather.data.search.cache.mapper.SearchHistoryDataDBMapper
import com.github.skytoph.simpleweather.data.search.cache.model.SearchHistoryDB
import io.realm.Realm
import javax.inject.Inject

interface SearchHistoryCache {
    fun readAll(): List<SearchHistoryDB>
    fun saveOrUpdate(id: String, data: SearchHistoryData)
    fun clear()

    class Base @Inject constructor(
        private val realmProvider: RealmProvider,
        private val dbMapper: SearchHistoryDataDBMapper,
    ) : SearchHistoryCache {

        override fun readAll(): List<SearchHistoryDB> = realmProvider.provide().use { realm ->
            return realm.where(SearchHistoryDB::class.java).findAll()
                .let { realm.copyFromRealm(it) }?.sortedByDescending { it.time } ?: emptyList()
        }

        override fun saveOrUpdate(id: String, data: SearchHistoryData) =
            realmProvider.provide().use { realm ->
                realm.executeTransaction {
                    findRealmObject(realm, id)?.deleteFromRealm()
                    it.insert(data.map(dbMapper, DataBase(it)))
                }
            }

        override fun clear() = realmProvider.provide().use { realm ->
            realm.executeTransaction {
                realm.where(SearchHistoryDB::class.java).findAll().deleteAllFromRealm()
            }
        }

        private fun findRealmObject(realm: Realm, id: String) =
            realm.where(SearchHistoryDB::class.java).equalTo(SearchHistoryDB.FIELD_PLACE_ID, id)
                .findFirst()
    }
}