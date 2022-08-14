package com.github.skytoph.simpleweather.domain.search

import com.github.skytoph.simpleweather.core.ErrorType
import com.github.skytoph.simpleweather.core.Mapper
import com.github.skytoph.simpleweather.core.presentation.view.horizon.ResourceProvider
import com.github.skytoph.simpleweather.presentation.search.model.SearchItemUi
import javax.inject.Inject

interface SearchItemDomainToUiMapper : Mapper<SearchItemUi> {
    fun map(id: String, title: String, subtitle: String): SearchItemUi
    fun map(errorType: ErrorType): SearchItemUi

    class Base @Inject constructor(resourceProvider: ResourceProvider) :
        Mapper.ToUi<SearchItemUi>(resourceProvider), SearchItemDomainToUiMapper {

        override fun map(id: String, title: String, subtitle: String) =
            SearchItemUi.Location(id, title, subtitle)

        override fun map(errorType: ErrorType) = SearchItemUi.Fail(errorMessage(errorType))
    }
}