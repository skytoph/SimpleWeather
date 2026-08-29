package com.github.skytoph.simpleweather.data.location

import com.google.android.libraries.places.api.model.Place


interface ChoosePlace {
    fun <T> choosePlace(places: List<T>, predicate: (T?, Int) -> Boolean?): T?
    fun choosePlace(places: List<Place>): Place?

    abstract class Abstract : ChoosePlace {

        override fun choosePlace(places: List<Place>): Place? =
            choosePlace(places) { place, i ->
                place?.placeTypes?.contains(TARGET_TYPES[i]) == true
            }

        override fun <T> choosePlace(
            places: List<T>,
            predicate: (T?, Int) -> Boolean?
        ): T? {
            var place: T? = null
            var i = 0
            while (place == null && i < TARGET_TYPES.size) {
                place = places.find { predicate(it, i) == true }
                i++
            }
            return place ?: places.firstOrNull()
        }

        protected companion object {
            val TARGET_TYPES = listOf(
                "sublocality",
                "locality",
                "administrative_area_level_2",
                "administrative_area_level_1"
            )
            val TARGET_SEARCH_TYPES = listOf("sublocality", "locality")
        }
    }

}