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

        binding.appbarAddLocation.toolbarWithTitle.apply {
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener { viewModel.goBack() }
        }

        val button = binding.messageButton
        val shimmerView = binding.placeholder.placeholderShimmer
        viewModel.observe(this) { state ->
            binding.apply {
                state.show(weatherAddContainer, button, errorView, shimmerView)
            }
        }

        viewModel.showWeather(childFragmentManager, R.id.weather_add_container) { favorite ->
            if (!favorite) button.setOnClickListener {
                viewModel.saveWeather { button.setClickedStyle() }
            }
        }
    }

    companion object {
        fun newInstance(placeId: String): AddLocationFragment =
            AddLocationFragment().apply {
                arguments = bundleOf(PLACE_ID_KEY to placeId)
            }
    }
}