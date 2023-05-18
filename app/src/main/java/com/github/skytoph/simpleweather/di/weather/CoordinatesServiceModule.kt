package com.github.skytoph.simpleweather.di.weather

import com.github.skytoph.simpleweather.data.location.cloud.PlaceCoordinatesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoordinatesServiceModule {

    @Provides
    @Singleton
    fun service(retrofit: Retrofit): PlaceCoordinatesService =
        retrofit.create(PlaceCoordinatesService::class.java)
}