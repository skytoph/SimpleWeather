package com.github.skytoph.simpleweather.presentation.search.model

import android.widget.TextView
import androidx.annotation.StringRes
import com.github.skytoph.simpleweather.core.Matcher
import com.github.skytoph.simpleweather.presentation.search.adapter.SearchLocationAdapter

sealed interface SearchItemUi : Matcher<SearchItemUi> {

    fun show(titleTextView: TextView, subtitleTextView: TextView) = Unit
    fun select(listener: SearchLocationAdapter.LocationClickListener) = Unit

    data class Location(
        private val id: String,
        private val title: String,
        private val subtitle: String,
    ) : SearchItemUi {

        override fun show(titleTextView: TextView, subtitleTextView: TextView) {
            titleTextView.text = title
            subtitleTextView.text = subtitle
        }

        override fun select(listener: SearchLocationAdapter.LocationClickListener) =
            listener.open(id, title)

        override fun matches(item: SearchItemUi): Boolean = item is Location && id == item.id

        override fun contentMatches(item: SearchItemUi): Boolean =
            item is Location && title == item.title && subtitle == item.subtitle
    }

    data class Fail(@StringRes private val messageId: Int) : SearchItemUi {

        override fun show(titleTextView: TextView, subtitleTextView: TextView) {
            titleTextView.text = titleTextView.context.resources.getString(messageId)
        }

        override fun matches(item: SearchItemUi): Boolean =
            item is Fail && messageId == item.messageId

        override fun contentMatches(item: SearchItemUi): Boolean = matches(item)
    }

    object Attribution : SearchItemUi {

        override fun matches(item: SearchItemUi): Boolean = item is Attribution

        override fun contentMatches(item: SearchItemUi): Boolean = matches(item)
    }
}