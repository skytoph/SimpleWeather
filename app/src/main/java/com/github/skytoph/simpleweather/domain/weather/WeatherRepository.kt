package com.github.skytoph.simpleweather.domain.weather

import com.github.skytoph.simpleweather.data.location.cloud.PlaceData
import com.github.skytoph.simpleweather.data.weather.model.WeatherData
import com.github.skytoph.simpleweather.domain.weather.mapper.UpdatedLately

interface WeatherRepository {

    interface RefreshAll {
        suspend fun refreshAll(criteria: UpdatedLately = UpdatedLately.LastDay): Boolean
    }

    interface Save : GetCached {
        suspend fun saveWeather()
        suspend fun checkReachingLimit(limit: Int)
    }

    interface Delete {
        suspend fun delete(id: String)
    }

    interface Mutable : RefreshAll, Delete, Save, GetCloud {
        fun cachedIDs(): List<String>
    }

    interface GetCloud {
        suspend fun getCloudWeather(location: PlaceData): WeatherData
    }

    interface GetCached {
        suspend fun getCachedAndUpdateLocation(id: String, location: String, language: String): WeatherData
        suspend fun getCachedWeather(id: String): WeatherData
        suspend fun isCached(id: String): Boolean
    }

    interface Update : GetCloud, GetCached {
        suspend fun updateCloudWeather(id: String): WeatherData
    }

    interface Base : Mutable, Update, RefreshAll, Save
}