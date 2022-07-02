package com.github.skytoph.simpleweather.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.skytoph.simpleweather.R
import com.github.skytoph.simpleweather.app.WeatherApp
import com.github.skytoph.simpleweather.core.presentation.BaseFragment
import com.github.skytoph.simpleweather.core.presentation.TextEditorWatcher
import com.github.skytoph.simpleweather.databinding.FragmentMainBinding
import com.github.skytoph.simpleweather.presentation.search.SearchViewModel

class MainFragment : BaseFragment<SearchViewModel, FragmentMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (requireActivity().application as WeatherApp).searchViewModel
    }

    override fun binding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMainBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.showWeather(R.id.weather_fragment_container)

        val searchEditText = binding.editTextSearchLocation

        searchEditText.addTextChangedListener(object : TextEditorWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString() ?: ""
                viewModel.getPredictions(query)
            }
        })
        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                viewModel.showSearch(R.id.weather_fragment_container)
            else {
                hideKeyboard()
                viewModel.showWeather(R.id.weather_fragment_container)
            }
        }
    }
}