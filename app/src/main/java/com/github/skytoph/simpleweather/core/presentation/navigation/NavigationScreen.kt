package com.github.skytoph.simpleweather.core.presentation.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

abstract class NavigationScreen(
    private val id: String,
    private val fragmentClass: Class<out Fragment>,
    @IdRes private val container: Int,
    private val strategy: ShowStrategy,
    private val animation: NavAnimation = NavAnimation.Crossfade
) : ShowScreen {

    override fun show(fragmentManager: FragmentManager) =
        strategy.show(id, fragment(), container, animation, fragmentManager)

    protected open fun fragment(): Fragment = fragmentClass.newInstance()
}