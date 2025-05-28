package com.github.skytoph.simpleweather.presentation.addlocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.github.skytoph.simpleweather.R
import com.github.skytoph.simpleweather.core.presentation.BaseFragment
import com.github.skytoph.simpleweather.databinding.FragmentAddLocationBinding
import com.github.skytoph.simpleweather.presentation.addlocation.AddLocationViewModel.Companion.PLACE_ID_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddLocationFragment : BaseFragment<AddLocationViewModel, FragmentAddLocationBinding>() {

    override val viewModel by viewModels<AddLocationViewModel>()

    override val bindingInflation: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAddLocationBinding =
        FragmentAddLocationBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appbarWithButton.toolbarWithTitle.addBackNavigation { viewModel.goBack() }

        val button = binding.appbarWithButton.messageButton
        val placeholder = binding.placeholder.placeholderShimmer
        viewModel.observe(this) { state ->
            binding.apply {
                state.show(weatherAddContainer, button, errorView, placeholder)
            }
        }

        if (savedInstanceState == null)
            viewModel.showWeather(this, childFragmentManager, R.id.weather_add_container)

        button.setOnClickListener {
            viewModel.saveWeather { button.setClickedStyle() }
        }
    }

    companion object {
        fun newInstance(placeId: String): AddLocationFragment =
            AddLocationFragment().apply {
                arguments = bundleOf(PLACE_ID_KEY to placeId)
            }
    }
}