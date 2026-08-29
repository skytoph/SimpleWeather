package com.github.skytoph.simpleweather.presentation.main

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.work.WorkInfo
import com.github.skytoph.simpleweather.R
import com.github.skytoph.simpleweather.core.presentation.communication.MessageCommunication
import com.github.skytoph.simpleweather.core.presentation.communication.ProgressCommunication
import com.github.skytoph.simpleweather.core.presentation.error.UiMessage
import com.github.skytoph.simpleweather.core.presentation.navigation.ShowScreen
import com.github.skytoph.simpleweather.data.work.UpdateWorker
import com.github.skytoph.simpleweather.domain.work.UpdateForecastWork
import com.github.skytoph.simpleweather.presentation.main.nav.MainNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val navigator: MainNavigator,
    private val progressCommunication: ProgressCommunication.Mutable,
    private val messageCommunication: MessageCommunication.Observe,
    private val worker: UpdateForecastWork.Periodically,
) : ViewModel() {

    fun showMain() = navigator.showMain(R.id.fragment_container)

    fun observeMessages(owner: LifecycleOwner, observer: Observer<UiMessage>) =
        messageCommunication.observe(owner, observer)

    fun observeNavigation(owner: LifecycleOwner, observer: Observer<ShowScreen>) =
        navigator.observe(owner, observer)

    fun observeProgress(owner: LifecycleOwner, observer: Observer<Boolean>) =
        progressCommunication.observe(owner, observer)

    fun scheduleUpdateForecast(owner: LifecycleOwner) {
        worker.scheduleWork(UpdateWorker.CRITERIA_DAY)
        worker.observeWork(owner) { info ->
            when (info?.state) {
                WorkInfo.State.RUNNING -> progressCommunication.show(true)
                WorkInfo.State.ENQUEUED -> progressCommunication.show(false)
                else -> {}
            }
        }
    }
}