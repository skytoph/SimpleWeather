package com.github.skytoph.simpleweather.data.search.mapper

import com.github.skytoph.simpleweather.core.Mapper
import com.github.skytoph.simpleweather.presentation.search.model.SearchItemUi

interface SearchItemDataToUiMapper : Mapper<SearchItemUi> {
    fun map(id: String, title: String, subtitle: String): SearchItemUi
    fun map(message: String): SearchItemUi

    class Base : SearchItemDataToUiMapper {
        override fun map(id: String, title: String, subtitle: String) =
            SearchItemUi.Location(id, title, subtitle)

        override fun map(message: String) = SearchItemUi.Fail(message)
    }

}
