package com.github.skytoph.simpleweather.data.search

import android.content.Context
import com.github.skytoph.simpleweather.BuildConfig
import com.github.skytoph.simpleweather.core.exception.EmptyRequestException
import com.github.skytoph.simpleweather.core.exception.NoResultsException
import com.github.skytoph.simpleweather.data.search.geocode.PredictionCloud
import com.github.skytoph.simpleweather.data.search.geocode.PredictionService
import com.github.skytoph.simpleweather.data.search.mapper.PredictionListToDataMapper
import com.github.skytoph.simpleweather.data.search.mapper.SearchResultsCloudToDataMapper
import com.github.skytoph.simpleweather.data.search.mapper.SearchResultsDataToDomainMapper
import com.github.skytoph.simpleweather.domain.search.SearchItemDomain
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.UnknownHostException
import java.util.Locale
import javax.inject.Inject

interface SearchLocationDataSource {
    fun startSession(locale: Locale)
    suspend fun getPredictions(
        query: String,
        showResult: (List<SearchItemDomain>) -> Unit,
    )

    class Geocode @Inject constructor(
        private val service: PredictionService,
        private val dataMapper: SearchResultsCloudToDataMapper,
        private val domainMapper: SearchResultsDataToDomainMapper,
    ) : SearchLocationDataSource {

        override suspend fun getPredictions(
            query: String,
            showResult: (List<SearchItemDomain>) -> Unit,
        ) {
            val data: List<SearchItemData> = try {
                dataMapper.map(fetchFromCloud(query))
            } catch (e: ApiException) {
                dataMapper.map(UnknownHostException())
            } catch (e: Exception) {
                dataMapper.map(e)
            }
            showResult.invoke(domainMapper.map(data))
        }

        private fun fetchFromCloud(query: String): List<PredictionCloud> =
            if (query.isBlank()) throw EmptyRequestException()
            else service.getPrediction(query).execute().body()!!
                .also { if (it.isEmpty()) throw NoResultsException() }

        override fun startSession(locale: Locale) = Unit
    }

    class Base @Inject constructor(
        @ApplicationContext private val context: Context,
        private val dataMapper: PredictionListToDataMapper,
        private val domainMapper: SearchResultsDataToDomainMapper,
    ) : SearchLocationDataSource {

        private var token: AutocompleteSessionToken = AutocompleteSessionToken.newInstance()
        private lateinit var client: PlacesClient

        override fun startSession(locale: Locale) {
            Places.initializeWithNewPlacesApiEnabled(context, BuildConfig.MAPS_API_KEY, locale)
            client = Places.createClient(context)
        }

        override suspend fun getPredictions(
            query: String,
            showResult: (List<SearchItemDomain>) -> Unit,
        ) {
            if (query.isBlank())
                return showResult.invoke(domainMapper.map(dataMapper.map(EmptyRequestException())))

            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .setSessionToken(token)
                .setTypesFilter(listOf(PlaceTypes.REGIONS))
                .build()

            client.findAutocompletePredictions(request)
                .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                    val predictions = response.autocompletePredictions.toList()
                    val predictionsData =
                        if (predictions.isEmpty()) dataMapper.map(NoResultsException())
                        else dataMapper.map(predictions)
                    showResult.invoke(domainMapper.map(predictionsData))
                }
                .addOnFailureListener { exception ->
                    showResult.invoke(domainMapper.map(dataMapper.map(exception)))
                }
        }
    }

    class Mock @Inject constructor() : SearchLocationDataSource {
        override fun startSession(locale: Locale) = Unit

        override suspend fun getPredictions(
            query: String,
            showResult: (List<SearchItemDomain>) -> Unit,
        ) {
            showResult.invoke(
                listOf(
                    SearchItemDomain.Location("11,22", "Mumbai", "Some country"),
                    SearchItemDomain.Location("55,11", "Hyderabad", "Other country")
                )
            )
        }
    }
}