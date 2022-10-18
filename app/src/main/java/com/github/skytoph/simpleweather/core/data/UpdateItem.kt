package com.github.skytoph.simpleweather.core.data

interface UpdateItem<T> {
    suspend fun update(data: T): T
    suspend fun updateLocation(data: T, placeId: String): T
}
