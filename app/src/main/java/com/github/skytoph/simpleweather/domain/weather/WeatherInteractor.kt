package com.github.skytoph.simpleweather.domain.weather

import com.github.skytoph.simpleweather.core.domain.FunctionHandler
import com.github.skytoph.simpleweather.core.provider.LocaleProvider
import com.github.skytoph.simpleweather.data.location.cloud.PlaceCloudDataSource
import com.github.skytoph.simpleweather.data.weather.mapper.WeatherDataToDomainMapper
import com.github.skytoph.simpleweather.domain.weather.mapper.WeatherDomainToUiMapper
import com.github.skytoph.simpleweather.presentation.weather.model.WeatherUi
import javax.inject.Inject

interface WeatherInteractor {
    suspend fun getWeather(id: String, favorite: Boolean): WeatherUi

    class Base @Inject constructor(
        private val repository: WeatherRepository.Update,
        private val domainMapper: WeatherDataToDomainMapper,
        private val uiMapper: WeatherDomainToUiMapper,
        private val handler: FunctionHandler<WeatherUi>,
        private val placeCloudDataSource: PlaceCloudDataSource,
        private val locale: LocaleProvider,
    ) : WeatherInteractor {

        override suspend fun getWeather(id: String, favorite: Boolean): WeatherUi =
            handler.handle {
                val weather = if (favorite) {
                    val data = repository.getCachedWeather(id)
                    val language = locale.locale().language
                    if (data.containsLocation(language)) data
                    else try {
                        repository.getCachedAndUpdateLocation(
                            id, placeCloudDataSource.place(id).mapToName(), language
                        )
                    } catch (exception: Exception) {
                        data
                    }
                } else repository.getCloudWeather(placeCloudDataSource.place(id))
                weather.map(domainMapper).map(uiMapper)
            }
    }
}