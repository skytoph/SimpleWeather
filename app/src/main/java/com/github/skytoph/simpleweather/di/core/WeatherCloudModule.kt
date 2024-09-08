package com.github.skytoph.simpleweather.di.core

import com.github.skytoph.simpleweather.BuildConfig
import com.github.skytoph.simpleweather.data.location.cloud.LocationAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitWeather

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MoshiConverterWeather

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MoshiWeather

@Module
@InstallIn(SingletonComponent::class)
object WeatherCloudModule {

    @Provides
    @RetrofitWeather
    fun retrofit(@MoshiConverterWeather moshiConverter: MoshiConverterFactory): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .client(
                OkHttpClient.Builder()
                    .apply {
                        if (BuildConfig.DEBUG) addInterceptor(HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        })
                    }
                    .build()
            )
            .addConverterFactory(moshiConverter)
            .build()

    @Provides
    @MoshiConverterWeather
    fun moshiConverterFactory(@MoshiWeather moshi: Moshi): MoshiConverterFactory =
        MoshiConverterFactory.create(moshi)

    @Provides
    @MoshiWeather
    fun moshi(): Moshi =
        Moshi.Builder()
            .add(LocationAdapter())
            .addLast(KotlinJsonAdapterFactory())
            .build()
}