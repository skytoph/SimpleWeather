package com.github.skytoph.simpleweather.presentation.addlocation

import androidx.annotation.IdRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skytoph.simpleweather.domain.favorites.FavoritesInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddLocationViewModel(
    private val favoritesInteractor: FavoritesInteractor,
    private val navigator: AddLocationNavigator,
) : ViewModel() {

    fun showWeather(@IdRes container: Int, id: String, favorite: Boolean) {
        navigator.showWeather(container, id, favorite)
    }

    fun saveWeather(@IdRes searchResultsContainer: Int, id: String) =
        viewModelScope.launch(Dispatchers.IO) {
            favoritesInteractor.saveFavorite(id)
            navigator.showFavorites(searchResultsContainer)
        }
}