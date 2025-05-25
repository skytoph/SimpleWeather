package com.github.skytoph.simpleweather.presentation.addlocation

import android.view.View
import android.widget.TextView
import com.github.skytoph.simpleweather.core.presentation.view.ActionButton
import com.github.skytoph.simpleweather.core.presentation.view.shimmer.ShimmerWrapper

sealed class LoadingState {
    abstract fun show(
        content: View, button: ActionButton, errorView: TextView, progress: ShimmerWrapper,
    )

    abstract class Abstract(
        private val contentVisibility: Int,
        private val buttonVisibility: Int,
        private val errorVisibility: Int,
        private val progressVisible: Boolean,
    ) : LoadingState() {
        override fun show(
            content: View, button: ActionButton, errorView: TextView, progress: ShimmerWrapper,
        ) {
            content.visibility = contentVisibility
            button.visibility = buttonVisibility
            errorView.visibility = errorVisibility
            progress.show(progressVisible, progressVisible)
        }
    }

    object Favorite : Abstract(
        contentVisibility = View.VISIBLE,
        buttonVisibility = View.VISIBLE,
        errorVisibility = View.GONE,
        progressVisible = false
    ) {
        override fun show(
            content: View, button: ActionButton, errorView: TextView, progress: ShimmerWrapper,
        ) {
            super.show(content, button, errorView, progress)
            button.setClickedStyle()
        }
    }

    object Initial : Abstract(
        contentVisibility = View.VISIBLE,
        buttonVisibility = View.INVISIBLE,
        errorVisibility = View.INVISIBLE,
        progressVisible = true
    )

    object Success : Abstract(
        contentVisibility = View.VISIBLE,
        buttonVisibility = View.VISIBLE,
        errorVisibility = View.INVISIBLE,
        progressVisible = false
    )

    object Fail : Abstract(
        contentVisibility = View.GONE,
        buttonVisibility = View.GONE,
        errorVisibility = View.VISIBLE,
        progressVisible = false
    )
}