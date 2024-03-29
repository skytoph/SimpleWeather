package com.github.skytoph.simpleweather.di.search

import android.content.Context
import com.github.skytoph.simpleweather.BuildConfig
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlacesClientModule {

    @Provides
    @Singleton
    fun places(@ApplicationContext context: Context): PlacesClient {
        Places.initialize(context, BuildConfig.MAPS_API_KEY)
        return Places.createClient(context)
    }
}