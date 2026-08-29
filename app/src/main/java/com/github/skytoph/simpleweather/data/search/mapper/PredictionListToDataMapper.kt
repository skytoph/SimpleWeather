package com.github.skytoph.simpleweather.data.search.mapper

import com.github.skytoph.simpleweather.core.Mapper
import com.github.skytoph.simpleweather.data.location.ChoosePlace
import com.github.skytoph.simpleweather.data.search.SearchItemData
import com.google.android.libraries.places.api.model.AutocompletePrediction
import javax.inject.Inject

interface PredictionListToDataMapper : Mapper<List<SearchItemData>> {
    fun map(list: List<AutocompletePrediction>): List<SearchItemData>
    fun map(exception: Exception): List<SearchItemData>

    class Base @Inject constructor(private val mapper: PredictionToDataMapper) :
        PredictionListToDataMapper,
        ChoosePlace.Abstract() {

        override fun map(list: List<AutocompletePrediction>): List<SearchItemData> =
            list.filter().map { mapper.map(it) }

        override fun map(exception: Exception) = listOf(SearchItemData.Fail(exception))

        private fun AutocompletePrediction.isDescribed() = this.getSecondaryText(null).isNotBlank()

        private fun List<AutocompletePrediction>.filter() =
            this.filter { place -> place.isDescribed() && place.types.any { it in TARGET_SEARCH_TYPES } }
    }
}