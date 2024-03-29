package com.github.skytoph.simpleweather.di.search

import com.github.skytoph.simpleweather.data.search.geocode.PredictionService
import com.github.skytoph.simpleweather.di.core.RetrofitWeather
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object SearchCloudModule {

    @Provides
    fun service(@RetrofitWeather retrofit: Retrofit): PredictionService =
        retrofit.create(PredictionService::class.java)
}