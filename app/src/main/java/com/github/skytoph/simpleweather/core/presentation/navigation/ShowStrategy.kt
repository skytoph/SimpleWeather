package com.github.skytoph.simpleweather.core.presentation.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

abstract class ShowStrategy {
    abstract fun show(
        id: String,
        fragment: Fragment,
        @IdRes container: Int,
        animation: NavAnimation,
        fragmentManager: FragmentManager,
    )

    protected fun FragmentManager.baseTransaction(animation: NavAnimation): FragmentTransaction =
        animation.addAnimation(beginTransaction())

    object Add : ShowStrategy() {
        override fun show(
            id: String,
            fragment: Fragment,
            @IdRes container: Int,
            animation: NavAnimation,
            fragmentManager: FragmentManager,
        ) {
            fragmentManager.baseTransaction(animation)
                .replace(container, fragment, id)
                .commit()
        }
    }

    object Replace : ShowStrategy() {
        override fun show(
            id: String,
            fragment: Fragment,
            container: Int,
            animation: NavAnimation,
            fragmentManager: FragmentManager,
        ) {
            fragmentManager.baseTransaction(animation)
                .replace(container, fragment, id)
                .addToBackStack(id)
                .commit()
        }
    }

    class AddChild(private val childFragmentManager: FragmentManager) : ShowStrategy() {
        override fun show(
            id: String,
            fragment: Fragment,
            @IdRes container: Int,
            animation: NavAnimation,
            fragmentManager: FragmentManager,
        ) {
            childFragmentManager.beginTransaction()
                .replace(container, fragment, id)
                .commit()
        }
    }

    class ReplaceChild(
        private val childFragmentManager: FragmentManager,
    ) : ShowStrategy() {
        override fun show(
            id: String,
            fragment: Fragment,
            @IdRes container: Int,
            animation: NavAnimation,
            fragmentManager: FragmentManager,
        ) {
            childFragmentManager.beginTransaction()
                .replace(container, fragment, id)
                .addToBackStack(id)
                .commit()
        }
    }
}
