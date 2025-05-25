package com.github.skytoph.simpleweather.presentation.addlocation

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skytoph.simpleweather.domain.addlocation.AddLocationInteractor
import com.github.skytoph.simpleweather.presentation.addlocation.communication.WeatherLoadingCommunication
import com.github.skytoph.simpleweather.presentation.addlocation.nav.AddLocationNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddLocationViewModel @Inject constructor(
    state: SavedStateHandle,
    private val interactor: AddLocationInteractor,
    private val navigator: AddLocationNavigator,
    private val loadingCommunication: WeatherLoadingCommunication.Observe,
) : ViewModel() {

    private val placeId: String = state[PLACE_ID_KEY]!!

    fun showWeather(
        fragmentManager: FragmentManager, @IdRes container: Int, callback: (Boolean) -> Unit
    ) = viewModelScope.launch(Dispatchers.IO) {
        val favorite = interactor.isFavorite(placeId)
        withContext(Dispatchers.Main) {
            navigator.showWeather(fragmentManager, container, placeId, favorite.also(callback))
        }
    }

    fun saveWeather(successCallback: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        interactor.save {
            withContext(Dispatchers.Main) {
                successCallback()
            }
        }
    }

    fun observe(owner: LifecycleOwner, observer: Observer<LoadingState>) =
        loadingCommunication.observe(owner, observer)

    fun goBack() = navigator.goBack()

    companion object {
        const val PLACE_ID_KEY = "placeId"
    }
}