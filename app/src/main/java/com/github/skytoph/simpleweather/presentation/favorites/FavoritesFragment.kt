package com.github.skytoph.simpleweather.presentation.favorites

import android.Manifest
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.os.LocaleListCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.github.skytoph.simpleweather.R
import com.github.skytoph.simpleweather.core.presentation.BaseFragment
import com.github.skytoph.simpleweather.databinding.FragmentFavoritesBinding
import com.github.skytoph.simpleweather.presentation.favorites.adapter.FavoritesAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FavoritesViewModel, FragmentFavoritesBinding>(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override val viewModel by viewModels<FavoritesViewModel>()

    private lateinit var adapter: FavoritesAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    private val permission: PermissionRequest =
        PermissionRequest.Base(Manifest.permission.ACCESS_COARSE_LOCATION) { viewModel.saveCurrentLocation() }

    override val bindingInflation: (inflater: LayoutInflater, container: ViewGroup?, attachToParent: Boolean) -> FragmentFavoritesBinding
        get() = FragmentFavoritesBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity()

        val deleteMenuItem =
            activity.findViewById<Toolbar>(R.id.toolbar_main).menu.findItem(R.id.action_delete)

        val progress = binding.shimmerFavorites
        progress.hideShimmer()

        viewModel.observeState(viewLifecycleOwner) { state ->
            state.show(
                binding.errorView,
                parentFragmentManager,
                deleteMenuItem,
                progress,
                tabLayout,
                requestPermission = { permission.request(requireContext()) },
                submitFavorites = { adapter.submitList(it) }
            )
        }

        viewModel.initializeAdapter { favorites ->
            adapter = FavoritesAdapter(this, favorites)

            viewPager = binding.viewPagerFavorites
            viewPager.adapter = adapter
            viewPager.offscreenPageLimit = 1

            tabLayout = activity.findViewById(R.id.tab_layout_dots)
            TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
        }
        deleteMenuItem?.setOnMenuItemClickListener {
            viewModel.delete(adapter.getItem(tabLayout.selectedTabPosition))
            true
        }

        binding.refresh.setOnRefreshListener {
            viewModel.refresh(viewLifecycleOwner) {
                binding.refresh.isRefreshing = false
            }
        }

        viewPager.doOnPreDraw { viewPager.setCurrentItem(viewModel.savedPage(), false) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
            ?.registerOnSharedPreferenceChangeListener(this)
        permission.register(this)
        if (savedInstanceState == null) lifecycleScope.launchWhenStarted {
            viewModel.initialize(viewLifecycleOwner)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.initializeState()
    }

    override fun onDestroy() {
        context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
            ?.unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroy()
    }

    override fun onPause() {
        viewModel.saveCurrentPage(viewPager.currentItem)
        super.onPause()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) =
        when (key) {
            resources.getString(R.string.key_units), resources.getString(R.string.key_time) ->
                viewModel.updateWeatherContent()
            resources.getString(R.string.key_language) -> {
                val language = sharedPreferences?.getString(key, resources.getString(R.string.language_eng))
                val locale = LocaleListCompat.forLanguageTags(language)
                AppCompatDelegate.setApplicationLocales(locale)
            }
            else -> Unit
        }
}