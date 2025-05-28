package com.github.skytoph.simpleweather.core.presentation.view

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewAnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.core.animation.doOnEnd
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.github.skytoph.simpleweather.core.presentation.anim.fadeIn
import com.github.skytoph.simpleweather.core.presentation.anim.fadeOut
import com.github.skytoph.simpleweather.databinding.SearchViewBinding
import com.github.skytoph.simpleweather.presentation.main.SearchQueryListener

class SearchView(
    context: Context,
    attrs: AttributeSet
) : FrameLayout(context, attrs) {

    private val binding: SearchViewBinding = SearchViewBinding.inflate(LayoutInflater.from(context), this, true)
    private var expandListener: OnExpandListener? = null
    private var collapseListener: OnCollapseListener? = null
    private var queryListener: SearchQueryListener? = null

    val isExpanded: Boolean
        get() = binding.searchExpandedView.isVisible

    init {
        binding.searchCollapsedView.setOnClickListener { expand() }
        binding.clearSearchButton.setOnClickListener { binding.searchInputText.setText("") }
    }

    fun addListeners(onExpand: OnExpandListener?, onCollapse: OnCollapseListener?, queryChange: SearchQueryListener?) {
        addOnExpandListener(onExpand)
        addOnCollapseListener(onCollapse)
        addOnQueryChangeListener(queryChange)
    }

    fun addOnExpandListener(onExpand: OnExpandListener?) {
        expandListener = onExpand
    }

    fun addOnCollapseListener(onCollapse: OnCollapseListener?) {
        collapseListener = onCollapse
    }

    fun addOnQueryChangeListener(listener: SearchQueryListener?) {
        if (listener == null)
            binding.searchInputText.removeTextChangedListener(queryListener)
        else binding.searchInputText.addTextChangedListener(listener)
        queryListener = listener
    }

    fun showClearButton() {
        if (!binding.clearSearchButton.isVisible)
            binding.clearSearchButton.fadeIn()
    }

    fun hideCloseButton() {
        if (binding.clearSearchButton.isVisible)
            binding.clearSearchButton.fadeOut(INVISIBLE)
    }

    fun expand() {
        expandListener?.onExpand()
        binding.searchCollapsedView.setOnClickListener(null)
        binding.searchInputText.setText("")
        binding.searchExpandedView.visibility = VISIBLE

        val animation = ViewAnimationUtils.createCircularReveal(
            binding.searchExpandedView,
            (binding.searchCollapsedIcon.right + binding.searchCollapsedIcon.left) / 2,
            (binding.searchCollapsedIcon.top + binding.searchCollapsedIcon.bottom) / 2,
            0f, width.toFloat()
        )
        animation.interpolator = FastOutSlowInInterpolator()
        animation.duration = resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
        animation.start()
        animation.doOnEnd {
            binding.searchInputText.requestFocus()
            showKeyboard()
        }
    }

    fun collapse() {
        collapseListener?.onCollapse()
        binding.searchCollapsedView.setOnClickListener { expand() }

        val animation = ViewAnimationUtils.createCircularReveal(
            binding.searchExpandedView,
            (binding.searchCollapsedIcon.right + binding.searchCollapsedIcon.left) / 2,
            (binding.searchCollapsedIcon.top + binding.searchCollapsedIcon.bottom) / 2,
            width.toFloat(), 0f
        )
        animation.duration = resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
        animation.interpolator = FastOutSlowInInterpolator()
        animation.start()
        animation.doOnEnd {
            binding.searchExpandedView.visibility = INVISIBLE
            binding.searchInputText.setText("")
            animation.removeAllListeners()
        }
    }

    private fun showKeyboard() {
        val inputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.searchInputText, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = bundleOf(
            KEY_EXPANDED to isExpanded,
            KEY_STATE to super.onSaveInstanceState(),
        )
        bundle.putSparseParcelableArray(KEY_CHILD_VIEW_STATE, saveChildViewStates())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            if (state.getBoolean(KEY_EXPANDED, false) == true) {
                binding.searchExpandedView.visibility = VISIBLE
                binding.searchInputText.requestFocus()
                showKeyboard()
            } else {
                binding.searchExpandedView.visibility = INVISIBLE
                binding.searchInputText.setText("")
            }
            val childViewsState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                state.getSparseParcelableArray(KEY_CHILD_VIEW_STATE, Parcelable::class.java)
            else state.getSparseParcelableArray(KEY_CHILD_VIEW_STATE)
            childViewsState?.let { restoreChildViewsState(it) }

            super.onRestoreInstanceState(state.getParcelable(KEY_STATE))
        } else super.onRestoreInstanceState(state)
    }

    private fun saveChildViewStates(): SparseArray<Parcelable> {
        val container = SparseArray<Parcelable>()
        children.forEach { view -> view.saveHierarchyState(container) }
        return container
    }

    private fun restoreChildViewsState(state: SparseArray<Parcelable>) = children.forEach { child ->
        child.restoreHierarchyState(state)
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable?>?) {
        dispatchFreezeSelfOnly(container)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable?>?) {
        dispatchThawSelfOnly(container)
    }

    fun interface OnExpandListener {
        fun onExpand()
    }

    fun interface OnCollapseListener {
        fun onCollapse()
    }

    private companion object {
        const val KEY_EXPANDED = "key_expanded"
        const val KEY_STATE = "key_state"
        const val KEY_CHILD_VIEW_STATE = "key_child_view_state"
    }
}