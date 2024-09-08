package com.github.skytoph.simpleweather.data.location.cloud.model

import com.github.skytoph.simpleweather.core.MappableTo
import com.github.skytoph.simpleweather.data.location.cloud.IdMapper
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//"results": [
//        {
//            "address_components": [ ... ],
//            "formatted_address": "1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA",
//            "geometry": {
//                "location": {
//                    "lat": 37.4224428,
//                    "lng": -122.0842467
@JsonClass(generateAdapter = true)
data class PlaceJson(
    @Json(name = "results")
    val results: List<ResultJson>?
)

@JsonClass(generateAdapter = true)
data class ResultJson(
    @Json(name = "geometry")
    val geometry: GeometryJson?,
    @Json(name = "address_components")
    val components: List<AddressComponentJson>?,
    @Json(name = "place_id")
    val place_id: String?,
    @Json(name = "types")
    val types: List<String>?
)

@JsonClass(generateAdapter = true)
data class AddressComponentJson(
    @Json(name = "long_name")
    val longName: String?,
    @Json(name = "short_name")
    val shortName: String?
)

@JsonClass(generateAdapter = true)
data class GeometryJson(
    @Json(name = "location")
    val location: LocationJson?,
)

@JsonClass(generateAdapter = true)
data class LocationJson(
    @Json(name = "lat")
    val lat: Double,
    @Json(name = "lng")
    val lng: Double,
)

@JsonClass(generateAdapter = true)
data class PlaceCoordinatesCloud(
    val placeId: String,
    val lat: Double,
    val lng: Double,
    val types: List<String>,
    val name: String?,
) : IdMapper.MappableToStringId, IdMapper.MappableToCoordinates, MappableTo<String> {

    override fun map(): String = placeId
    override fun map(mapper: IdMapper): String = mapper.map(lat, lng)
    override fun mapToCoordinates(mapper: IdMapper): Pair<Double, Double> =
        mapper.mapToCoordinates(lat, lng)
}