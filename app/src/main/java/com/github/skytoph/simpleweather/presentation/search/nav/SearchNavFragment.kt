package com.github.skytoph.simpleweather.presentation.search.nav

import androidx.annotation.IdRes
import com.github.skytoph.simpleweather.core.presentation.navigation.NavigationScreen
import com.github.skytoph.simpleweather.core.presentation.navigation.ShowStrategy
import com.github.skytoph.simpleweather.presentation.search.SearchFragment


class SearchNavFragment(
    @IdRes private val container: Int,
    strategy: ShowStrategy = ShowStrategy.Replace,
) : NavigationScreen(
    TAG,
    SearchFragment::class.java,
    container,
    strategy
) {

    companion object {
        const val TAG = "SearchNavigationFragment"
    }
}