package com.github.skytoph.simpleweather.presentation.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.github.skytoph.simpleweather.R
import com.github.skytoph.simpleweather.core.presentation.BaseFragment
import com.github.skytoph.simpleweather.databinding.FragmentFavoritesBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FavoritesViewModel, FragmentFavoritesBinding>() {

    override val viewModel by viewModels<FavoritesViewModel>()

    @Inject
    lateinit var adapter: FavoritesAdapter
    private lateinit var tabLayout: TabLayout

    override val bindingInflation: (inflater: LayoutInflater, container: ViewGroup?, attachToParent: Boolean) -> FragmentFavoritesBinding
        get() = FragmentFavoritesBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = binding.viewPagerFavorites
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 2

        tabLayout = requireActivity().findViewById(R.id.tab_layout_dots)
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()

        viewModel.observe(this) { favorites ->
            adapter.submitList(favorites)
        }

        binding.refresh.setOnRefreshListener {
            viewModel.refresh {
                binding.refresh.isRefreshing = false
            }
        }
    }

    override fun onResume() {
        viewModel.getFavorites()
        super.onResume()
    }
}