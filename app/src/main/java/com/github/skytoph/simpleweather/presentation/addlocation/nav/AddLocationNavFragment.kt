package com.github.skytoph.simpleweather.presentation.addlocation.nav

import androidx.annotation.IdRes
import com.github.skytoph.simpleweather.core.presentation.navigation.NavAnimation
import com.github.skytoph.simpleweather.core.presentation.navigation.NavigationScreen
import com.github.skytoph.simpleweather.core.presentation.navigation.ShowStrategy
import com.github.skytoph.simpleweather.presentation.addlocation.AddLocationFragment

class AddLocationNavFragment(
    @IdRes container: Int,
    private val id: String,
    strategy: ShowStrategy = ShowStrategy.Replace
) : NavigationScreen(
    TAG,
    AddLocationFragment::class.java,
    container,
    strategy,
    NavAnimation.Slide
) {

    override fun fragment() = AddLocationFragment.newInstance(id)

    companion object {
        const val TAG = "AddLocationFragment"
    }
}