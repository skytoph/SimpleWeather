package com.github.skytoph.simpleweather.domain.search

import com.github.skytoph.simpleweather.core.provider.LocaleProvider
import com.github.skytoph.simpleweather.data.search.SearchLocationDataSource
import javax.inject.Inject

interface SearchInteractor {
    fun startSession()
    suspend fun search(query: String, showResult: (List<SearchItemDomain>) -> Unit)

    class Base @Inject constructor(
        private val searchDataSource: SearchLocationDataSource,
        private val locale: LocaleProvider
    ) : SearchInteractor {

        override fun startSession() = searchDataSource.startSession(locale.locale())

        override suspend fun search(query: String, showResult: (List<SearchItemDomain>) -> Unit) =
            searchDataSource.getPredictions(query, showResult)
    }
}