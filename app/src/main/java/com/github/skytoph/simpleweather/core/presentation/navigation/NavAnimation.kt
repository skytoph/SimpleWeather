package com.github.skytoph.simpleweather.core.presentation.navigation

import androidx.fragment.app.FragmentTransaction
import com.github.skytoph.simpleweather.R

sealed interface NavAnimation {

    fun addAnimation(transaction: FragmentTransaction): FragmentTransaction = transaction

    object Crossfade : NavAnimation {

        override fun addAnimation(transaction: FragmentTransaction): FragmentTransaction =
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
    }

    object Slide : NavAnimation {

        override fun addAnimation(transaction: FragmentTransaction): FragmentTransaction =
            transaction.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
    }

    object None : NavAnimation
}