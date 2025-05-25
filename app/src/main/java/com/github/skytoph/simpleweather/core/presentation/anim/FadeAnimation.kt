package com.github.skytoph.simpleweather.core.presentation.anim

import android.view.View

private const val ANIMATION_DURATION = 200L

fun View.fadeIn() {
    alpha = 0f
    visibility = View.VISIBLE
    animate().alpha(1f).setDuration(ANIMATION_DURATION).setListener(null)
}

fun View.fadeOut(contentVisibility: Int) {
    animate().alpha(0f).setDuration(ANIMATION_DURATION).withEndAction {
        visibility = contentVisibility
    }
}

