package com.github.skytoph.simpleweather.presentation.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.github.skytoph.simpleweather.R
import com.github.skytoph.simpleweather.core.presentation.BaseFragment
import com.github.skytoph.simpleweather.databinding.FragmentAboutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutFragment : BaseFragment<AboutViewModel, FragmentAboutBinding>() {

    override val viewModel by viewModels<AboutViewModel>()

    override val bindingInflation: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAboutBinding =
        FragmentAboutBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appbarSettings.toolbarTitle.text = resources.getString(R.string.about_app)
        binding.appbarSettings.toolbarWithTitle.setNavigationIcon(R.drawable.ic_back)
        binding.appbarSettings.toolbarWithTitle.setNavigationOnClickListener { viewModel.goBack() }
    }
}