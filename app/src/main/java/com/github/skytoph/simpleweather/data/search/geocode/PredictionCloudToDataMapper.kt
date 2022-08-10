package com.github.skytoph.simpleweather.data.search.geocode

import com.github.skytoph.simpleweather.core.Mapper
import com.github.skytoph.simpleweather.data.search.SearchItemData
import javax.inject.Inject

interface PredictionCloudToDataMapper : Mapper<SearchItemData> {
    fun map(id: String, title: String, subtitle: String): SearchItemData
    fun map(exception: Exception): SearchItemData

    class Base @Inject constructor() : PredictionCloudToDataMapper {
        override fun map(id: String, title: String, subtitle: String): SearchItemData =
            SearchItemData.Location(id, title, subtitle)

        override fun map(exception: Exception): SearchItemData = SearchItemData.Fail(exception)
    }
}