package com.github.skytoph.simpleweather.presentation.main

import android.text.Editable
import android.text.TextWatcher

class SearchQueryListener(private val search: (String) -> Unit) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun afterTextChanged(s: Editable?) = Unit

    override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {
        search(query?.toString() ?: "")
    }
}