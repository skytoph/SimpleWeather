package com.github.skytoph.simpleweather.data.location.cloud

import com.github.skytoph.simpleweather.BuildConfig
import com.github.skytoph.simpleweather.data.location.cloud.model.PlaceCoordinatesCloud
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceCoordinatesService {

    @GET(value = "/maps/api/geocode/json?key=${BuildConfig.GEOCODING_API_KEY}")
    fun getPlace(
        @Query(value = "place_id", encoded = true) placeId: String,
        @Query(value = "language", encoded = true) language: String
    ): Call<PlaceCoordinatesCloud>

    @GET(value = "/maps/api/geocode/json?result_type=political&key=${BuildConfig.GEOCODING_API_KEY}")
    fun getPlaceByLatLng(
        @Query(value = "latlng", encoded = true) latlng: String,
        @Query(value = "language", encoded = true) language: String
    ): Call<PlaceCoordinatesCloud>
}