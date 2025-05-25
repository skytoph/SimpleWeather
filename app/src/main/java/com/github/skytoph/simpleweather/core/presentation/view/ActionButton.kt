package com.github.skytoph.simpleweather.core.presentation.view

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
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
        setOnClickListener {
            setClickedStyle()
        }
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

        binding.actionButtonText.apply {
            setInAnimation(context, R.anim.fade_in)
            setOutAnimation(context, R.anim.fade_out)
            textEnabled?.let { setCurrentText(it) }
        }

        binding.actionButtonIcon.apply {
            setInAnimation(context, R.anim.fade_in)
            setOutAnimation(context, R.anim.fade_out)
            iconEnabled?.let { setImageDrawable(it) }
        }

        backgroundColorEnabled?.let { binding.root.background.setTint(it) }
    }

    fun setClickedStyle() {
        isEnabled = false
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
}