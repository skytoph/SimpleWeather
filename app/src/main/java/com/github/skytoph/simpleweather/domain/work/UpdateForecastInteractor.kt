package com.github.skytoph.simpleweather.domain.work

import com.github.skytoph.simpleweather.domain.weather.WeatherRepository
import com.github.skytoph.simpleweather.domain.weather.mapper.UpdatedLately
import javax.inject.Inject
import javax.inject.Singleton

interface UpdateForecastInteractor {
    suspend fun updateForecasts(criteria: UpdatedLately): Boolean

    @Singleton
    class Base @Inject constructor(
        private val repository: WeatherRepository.RefreshAll,
    ) : UpdateForecastInteractor {

        override suspend fun updateForecasts(criteria: UpdatedLately) =
            repository.refreshAll(criteria)
    }
}