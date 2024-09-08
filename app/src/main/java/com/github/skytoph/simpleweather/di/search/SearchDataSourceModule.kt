package com.github.skytoph.simpleweather.di.search

import com.github.skytoph.simpleweather.data.location.cloud.FindPlace
import com.github.skytoph.simpleweather.data.location.cloud.PlaceCloudDataSource
import com.github.skytoph.simpleweather.data.location.cloud.PlaceCoordinatesDataSource
import com.github.skytoph.simpleweather.data.location.mapper.PlaceCloudToDataMapper
import com.github.skytoph.simpleweather.data.search.SearchLocationDataSource
import com.github.skytoph.simpleweather.data.weather.mapper.LocationCloudToDataMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
abstract class SearchDataSourceModule {

    @Binds
    abstract fun dataSource(dataSource: SearchLocationDataSource.Base): SearchLocationDataSource

    @Binds
    abstract fun placeNameSearch(source: PlaceCloudDataSource.Base): PlaceCloudDataSource

    @Binds
    abstract fun coordinatesDataSource(source: PlaceCoordinatesDataSource.Base): PlaceCoordinatesDataSource

    @Binds
    abstract fun placeMapper(mapper: PlaceCloudToDataMapper.Base): PlaceCloudToDataMapper

    @Binds
    abstract fun locationCloudToDataMapper(mapper: LocationCloudToDataMapper.Base): LocationCloudToDataMapper

    @Binds
    abstract fun findPlace(source: FindPlace.Base): FindPlace
}