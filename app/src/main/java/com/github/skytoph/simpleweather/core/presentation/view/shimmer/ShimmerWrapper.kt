package com.github.skytoph.simpleweather.core.presentation.view.shimmer

import android.content.Context
import android.util.AttributeSet
import com.facebook.shimmer.ShimmerFrameLayout
import com.github.skytoph.simpleweather.core.presentation.anim.fadeIn
import com.github.skytoph.simpleweather.core.presentation.anim.fadeOut

class ShimmerWrapper @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ShimmerFrameLayout(context, attrs) {

    fun show(loadingVisible: Boolean, layoutVisible: Boolean = true) {
        if (loadingVisible && !isShimmerStarted) {
            showShimmer(false)
            startShimmer()
        } else if (!loadingVisible && isShimmerVisible) {
            stopShimmer()
            hideShimmer()
        }
        if (layoutVisible) fadeIn() else fadeOut(GONE)
    }
}