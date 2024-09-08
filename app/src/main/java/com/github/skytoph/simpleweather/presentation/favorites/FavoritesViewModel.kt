package com.github.skytoph.simpleweather.presentation.favorites

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.github.skytoph.simpleweather.data.work.UpdateWorker
import com.github.skytoph.simpleweather.domain.favorites.FavoritesInteractor
import com.github.skytoph.simpleweather.domain.work.UpdateForecastWork
import com.github.skytoph.simpleweather.presentation.favorites.communication.FavoritesState
import com.github.skytoph.simpleweather.presentation.favorites.communication.FavoritesStateCommunication
import com.github.skytoph.simpleweather.presentation.favorites.communication.RefreshCommunication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val interactor: FavoritesInteractor,
    private val stateCommunication: FavoritesStateCommunication,
    private val refreshCommunication: RefreshCommunication.Update,
    private val worker: UpdateForecastWork.Once,
) : ViewModel() {

    fun initialize(lifecycleOwner: LifecycleOwner) {
        if (interactor.favoriteIDs().isEmpty()) {
            requestPermissions()
        } else {
            worker.scheduleWork(UpdateWorker.CRITERIA_DAY)
            observeUpdatingWork(lifecycleOwner) {}
        }
    }

    fun initializeState() = stateCommunication.show(
        if (interactor.favoriteIDs().isEmpty()) FavoritesState.Empty else FavoritesState.Initial
    )

    fun initializeAdapter(submitFavorites: (List<String>) -> Unit) {
        submitFavorites(interactor.favoriteIDs())
        stateCommunication.show(FavoritesState.Initial)
    }

    fun refresh(lifecycleOwner: LifecycleOwner, hideRefreshing: () -> Unit = {}) =
        if (interactor.favoriteIDs().isEmpty()) {
            requestPermissions()
            hideRefreshing()
        } else {
            worker.scheduleWork(UpdateWorker.CRITERIA_HOUR)
            observeUpdatingWork(lifecycleOwner, hideRefreshing)
        }

    private fun observeUpdatingWork(lifecycleOwner: LifecycleOwner, hideRefreshing: () -> Unit) =
        worker.observeWork(lifecycleOwner) { info ->
            val state = info?.state
            if (state == WorkInfo.State.RUNNING)
                stateCommunication.show(FavoritesState.Progress(true))
            else {
                hideRefreshing()
                stateCommunication.show(FavoritesState.Progress(false))
            }
            if (state == WorkInfo.State.FAILED)
                interactor.handleUpdatingError()
            else if (state == WorkInfo.State.SUCCEEDED
                && info.outputData.getBoolean(UpdateWorker.ARG_RESULT, false)
            ) updateWeatherContent()
        }

    fun updateWeatherContent() = refreshCommunication.show(true)

    fun delete(id: String) = stateCommunication.show(FavoritesState.Delete {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.removeFavorite(id)
            withContext(Dispatchers.Main) { refreshFavorites() }
        }
    })

    private fun refreshFavorites(ids: List<String>? = null) {
        val favorites = ids ?: interactor.favoriteIDs()
        val state =
            if (favorites.isEmpty()) FavoritesState.Empty else FavoritesState.Base(favorites)
        stateCommunication.show(state)
    }

    fun observeState(lifecycleOwner: LifecycleOwner, handle: suspend (FavoritesState) -> Unit) {
        lifecycleOwner.lifecycleScope.launchWhenCreated {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                stateCommunication.handle(handle)
            }
        }
    }

    private fun requestPermissions() =
        stateCommunication.show(
            FavoritesState.AddCurrentLocation(
                showProgress = true,
                addCurrentLocation = { stateCommunication.show(FavoritesState.RequestPermission) },
                cancel = { stateCommunication.show(FavoritesState.Progress(false)) })
        )

    fun saveCurrentLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.saveCurrentLocation()
            withContext(Dispatchers.Main) {
                refreshFavorites()
                stateCommunication.show(FavoritesState.Progress(false))
            }
        }
    }

    fun savedPage(): Int = interactor.savedPage()

    fun saveCurrentPage(position: Int) = interactor.savePage(position)
}