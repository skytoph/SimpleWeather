package com.github.skytoph.simpleweather.core.presentation

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(
    private val spaceTop: Int = 0,
    private val spaceLeft: Int = 0,
    private val spaceBottom: Int = 0,
    private val spaceRight: Int = 0,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) = with(outRect) {
        top = spaceTop
        left = spaceLeft
        if (parent.getChildAdapterPosition(view) != state.itemCount - 1)
            bottom = spaceBottom
        if (parent.getChildAdapterPosition(view) != state.itemCount - 1)
            right = spaceRight
    }
}