package com.github.skytoph.simpleweather.di.search.location

import com.github.skytoph.simpleweather.domain.search.LocationsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class LocationsRepositoryModule {

    @Binds
    abstract fun repository(repository: LocationsRepository.Base): LocationsRepository
}