package com.github.skytoph.simpleweather.presentation.weather

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skytoph.simpleweather.domain.weather.WeatherInteractor
import com.github.skytoph.simpleweather.presentation.addlocation.LoadingState
import com.github.skytoph.simpleweather.presentation.addlocation.communication.WeatherLoadingCommunication
import com.github.skytoph.simpleweather.presentation.favorites.communication.RefreshCommunication
import com.github.skytoph.simpleweather.presentation.weather.communication.WeatherCommunication
import com.github.skytoph.simpleweather.presentation.weather.model.WeatherUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    state: SavedStateHandle,
    private val interactor: WeatherInteractor,
    private val weatherCommunication: WeatherCommunication,
    private val loadingCommunication: WeatherLoadingCommunication.Update,
    private val refreshCommunication: RefreshCommunication.Observe,
) : ViewModel() {

    private val placeId = state.get<String>(PLACE_ID_KEY)!!
    private val favorite = state.get<Boolean>(FAVORITE_KEY)!!

    fun getWeather(firstCreated: Boolean) {
        if (!firstCreated) return
        if (favorite) loadingCommunication.show(LoadingState.Favorite)

        viewModelScope.launch(Dispatchers.IO) {
            val weather = interactor.getWeather(placeId, favorite)
            withContext(Dispatchers.Main) {
                weather.show()
                weather.show(loadingCommunication)
            }
        }
    }

    private suspend fun WeatherUi.show() {
        withContext(Dispatchers.Main) {
            this@show.show(weatherCommunication)
        }
    }

    fun refreshFromCache() = viewModelScope.launch(Dispatchers.IO) {
        if (favorite) interactor.getWeather(placeId, true).show()
    }

    fun observe(owner: LifecycleOwner, observer: Observer<WeatherUi>) =
        weatherCommunication.observe(owner, observer)

    fun observeRefresh(owner: LifecycleOwner, observer: Observer<Boolean>) =
        refreshCommunication.observe(owner, observer)

    companion object {
        const val PLACE_ID_KEY = "placeId"
        const val FAVORITE_KEY = "favorite"
    }
}