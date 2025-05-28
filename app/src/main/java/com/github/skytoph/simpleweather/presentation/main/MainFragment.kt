package com.github.skytoph.simpleweather.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.view.forEach
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import com.github.skytoph.simpleweather.R
import com.github.skytoph.simpleweather.core.presentation.BaseFragment
import com.github.skytoph.simpleweather.core.presentation.anim.fadeOut
import com.github.skytoph.simpleweather.core.presentation.view.SearchView
import com.github.skytoph.simpleweather.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<MainContentViewModel, FragmentMainBinding>() {

    override val viewModel by viewModels<MainContentViewModel>()

    override val bindingInflation: (inflater: LayoutInflater, container: ViewGroup?, attachToParent: Boolean) -> FragmentMainBinding
        get() = FragmentMainBinding::inflate

    private lateinit var searchView: SearchView
    private lateinit var expandListener: SearchView.OnExpandListener
    private lateinit var collapseListener: SearchView.OnCollapseListener
    private lateinit var queryListener: SearchQueryListener
    private lateinit var toolbar: Toolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = binding.appbarMain.toolbarMain
        toolbar.inflateMenu(R.menu.home_menu)

        searchView = binding.appbarMain.searchView

        expandListener = SearchView.OnExpandListener {
            binding.appbarMain.tabLayoutDots.fadeOut(View.GONE)
            viewModel.showSearch()
            toolbar.hideMenu()
            toolbar.addBackNavigation { searchView.collapse() }
        }
        collapseListener = SearchView.OnCollapseListener {
            hideKeyboard()
            toolbar.removeNavigationIcon()
            toolbar.showMenu()
            viewModel.goBack()
        }

        queryListener = SearchQueryListener { query ->
            viewModel.startLoading()
            viewModel.getPredictions(query)
            if (query.isEmpty()) searchView.hideCloseButton()
            else searchView.showClearButton()
        }

        val activity = requireActivity()
        activity.onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (searchView.isExpanded && !searchView.isFocused)
                        searchView.collapse()
                    else {
                        viewModel.goBack()
                        isEnabled = false
                        activity.onBackPressedDispatcher.onBackPressed()
                    }
                }
            })

        toolbar.menu.findItem(R.id.action_settings).setOnMenuItemClickListener {
            viewModel.showSettings(R.id.fragment_container)
            true
        }

        viewModel.observe(viewLifecycleOwner) { state ->
            state.show(childFragmentManager, R.id.weather_fragment_container)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (searchView.isExpanded) {
            toolbar.hideMenu()
            toolbar.addBackNavigation { searchView.collapse() }
        } else {
            toolbar.removeNavigationIcon()
            toolbar.showMenu()
        }
    }

    override fun onResume() {
        super.onResume()
        searchView.addListeners(expandListener, collapseListener, queryListener)
        if (searchView.isExpanded) toolbar.hideMenu()
    }

    override fun onPause() {
        super.onPause()
        searchView.addListeners(null, null, null)
    }

    private fun Toolbar.showMenu() {
        this.updatePadding(0, 0, 0, 0)
        this.menu.showAll()
    }

    private fun Toolbar.hideMenu() {
        val toolbarPadding = resources.getDimensionPixelSize(R.dimen.toolbar_collapsed_end_padding)
        this.updatePadding(0, 0, toolbarPadding, 0)
        this.menu.hideAll()
    }

    private fun Menu.hideAll() {
        forEach { item -> item.isVisible = false }
    }

    private fun Menu.showAll() {
        forEach { item -> item.isVisible = true }
    }
}