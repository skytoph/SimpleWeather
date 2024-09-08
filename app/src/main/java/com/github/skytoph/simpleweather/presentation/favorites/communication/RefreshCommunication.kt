package com.github.skytoph.simpleweather.presentation.favorites.communication

import com.github.skytoph.simpleweather.core.presentation.communication.Communication
import javax.inject.Inject
import javax.inject.Singleton

class RefreshCommunication {

    interface Update : Communication.Update<Boolean>

    interface Observe : Communication.Observe<Boolean>

    interface Mutable : Update, Observe

    @Singleton
    class Base @Inject constructor() : Mutable, Communication.SingleUiUpdate<Boolean>()
}