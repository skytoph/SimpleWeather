package com.github.skytoph.simpleweather.core.presentation.view

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.core.view.children
import com.github.skytoph.simpleweather.R
import com.github.skytoph.simpleweather.databinding.ActionButtonBinding

class ActionButton : LinearLayout {

    private val binding: ActionButtonBinding = ActionButtonBinding.inflate(LayoutInflater.from(context), this, true)

    private var iconEnabled: Drawable? = null
    private var iconDisabled: Drawable? = null
    private var backgroundColorEnabled: Int? = null
    private var backgroundColorDisabled: Int? = null
    private var contentColorEnabled: Int? = null
    private var contentColorDisabled: Int? = null
    private var textEnabled: String? = null
    private var textDisabled: String? = null
    private var isButtonEnabled: Boolean = true

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ActionButton, 0, 0).apply {
            try {
                iconEnabled = getDrawable(R.styleable.ActionButton_iconEnabled)
                iconDisabled = getDrawable(R.styleable.ActionButton_iconDisabled)
                backgroundColorEnabled = getColor(R.styleable.ActionButton_backgroundColorEnabled, Color.GRAY)
                backgroundColorDisabled = getColor(R.styleable.ActionButton_backgroundColorDisabled, Color.GRAY)
                contentColorEnabled = getColor(R.styleable.ActionButton_contentColorEnabled, Color.BLACK)
                contentColorDisabled = getColor(R.styleable.ActionButton_backgroundColorDisabled, Color.WHITE)
                textEnabled = getString(R.styleable.ActionButton_textEnabled)
                textDisabled = getString(R.styleable.ActionButton_textDisabled)
            } finally {
                recycle()
            }
        }

        applyStyle()
    }

    private fun applyStyle() {
        if (isButtonEnabled) applyStyleEnabled()
        else applyStyleDisabled()
    }

    private fun applyStyleDisabled() {
        textDisabled?.let {
            binding.actionButtonText.setCurrentText(it)
            binding.actionButtonText.setText(it)
        }
        iconDisabled?.let { binding.actionButtonIcon.setImageDrawable(it) }
        backgroundColorDisabled?.let { binding.root.background.setTint(it) }
    }

    private fun applyStyleEnabled() {
        binding.actionButtonText.apply {
            textEnabled?.let { setCurrentText(it) }
            setInAnimation(context, R.anim.fade_in)
            setOutAnimation(context, R.anim.fade_out)
        }

        binding.actionButtonIcon.apply {
            iconEnabled?.let { setImageDrawable(it) }
            setInAnimation(context, R.anim.fade_in)
            setOutAnimation(context, R.anim.fade_out)
        }

        backgroundColorEnabled?.let { binding.root.background.setTint(it) }
    }

    fun setDisabledStyle() {
        isButtonEnabled = false
        applyStyleDisabled()
    }

    fun setClickedStyle() {
        isButtonEnabled = false
        binding.actionButtonIcon.setImageDrawable(iconDisabled)
        binding.actionButtonText.setText(textDisabled)

        ValueAnimator.ofObject(ArgbEvaluator(), backgroundColorEnabled, backgroundColorDisabled).apply {
            setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong())
            addUpdateListener { value ->
                binding.root.background.setTint(value.getAnimatedValue() as Int)
            }
        }.start()

        super.setOnClickListener(null)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = bundleOf(KEY_STATE to super.onSaveInstanceState())
        bundle.putSparseParcelableArray(KEY_CHILD_VIEW_STATE, saveChildViewStates())
        bundle.putBoolean(KEY_ENABLED, isButtonEnabled)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            val childViewsState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                state.getSparseParcelableArray(KEY_CHILD_VIEW_STATE, Parcelable::class.java)
            else state.getSparseParcelableArray(KEY_CHILD_VIEW_STATE)
            childViewsState?.let { restoreChildViewsState(it) }

            isButtonEnabled = state.getBoolean(KEY_ENABLED, true)
            applyStyle()

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

    private companion object {
        const val KEY_ENABLED = "key_enabled"
        const val KEY_STATE = "key_state"
        const val KEY_CHILD_VIEW_STATE = "key_child_view_state"
    }
}