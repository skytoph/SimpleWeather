package com.github.skytoph.simpleweather.presentation.settings.nav

import androidx.annotation.IdRes
import com.github.skytoph.simpleweather.core.presentation.navigation.NavAnimation
import com.github.skytoph.simpleweather.core.presentation.navigation.NavigationScreen
import com.github.skytoph.simpleweather.core.presentation.navigation.ShowStrategy
import com.github.skytoph.simpleweather.presentation.settings.SettingsFragment

class SettingsNavFragment(
    @IdRes private val container: Int,
    strategy: ShowStrategy =ShowStrategy.Replace,
) : NavigationScreen(
    TAG,
    SettingsFragment::class.java,
    container,
    strategy,
    NavAnimation.Slide
) {

    companion object {
        const val TAG = "SettingsNavigationScreen"
    }
}