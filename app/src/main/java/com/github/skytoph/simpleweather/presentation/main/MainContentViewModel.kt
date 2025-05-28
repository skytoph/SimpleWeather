package com.github.skytoph.simpleweather.presentation.main

import androidx.annotation.IdRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skytoph.simpleweather.domain.search.SearchInteractor
import com.github.skytoph.simpleweather.domain.search.SearchResultsDomainToUiMapper
import com.github.skytoph.simpleweather.presentation.main.communication.MainState
import com.github.skytoph.simpleweather.presentation.main.communication.MainStateCommunication
import com.github.skytoph.simpleweather.presentation.main.nav.MainContentNavigator
import com.github.skytoph.simpleweather.presentation.search.communication.SearchCommunication
import com.github.skytoph.simpleweather.presentation.search.communication.SearchLoadingCommunication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainContentViewModel @Inject constructor(
    private val navigator: MainContentNavigator,
    private val interactor: SearchInteractor,
    private val uiMapper: SearchResultsDomainToUiMapper,
    private val searchCommunication: SearchCommunication.Update,
    private val searchLoading: SearchLoadingCommunication.Update,
    private val stateCommunication: MainStateCommunication,
) : ViewModel() {

    init {
        interactor.startSession()
        getPredictions("")
        showFavorites()
    }

    fun getPredictions(query: String) = viewModelScope.launch(Dispatchers.IO) {
        interactor.search(query) { predictions ->
            searchCommunication.show(uiMapper.map(predictions))
            searchLoading.show(false)
        }
    }

    private fun showFavorites() = stateCommunication.show(MainState.Favorites(navigator))

    fun showSearch() = stateCommunication.show(MainState.Search(navigator))

    fun showSettings(@IdRes container: Int) =
        stateCommunication.show(MainState.Settings(navigator, container))

    fun observe(owner: LifecycleOwner, observer: Observer<MainState>) =
        stateCommunication.observe(owner, observer)

    fun goBack() = navigator.goBack()

    fun startLoading() = searchLoading.show(true)
}