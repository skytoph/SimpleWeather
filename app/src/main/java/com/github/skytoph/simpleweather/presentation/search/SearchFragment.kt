package com.github.skytoph.simpleweather.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import com.github.skytoph.simpleweather.R
import com.github.skytoph.simpleweather.core.presentation.BaseFragment
import com.github.skytoph.simpleweather.core.presentation.MarginItemDecoration
import com.github.skytoph.simpleweather.databinding.FragmentSearchBinding
import com.github.skytoph.simpleweather.presentation.search.adapter.SearchHistoryAdapter
import com.github.skytoph.simpleweather.presentation.search.adapter.SearchLocationAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment :
    BaseFragment<SearchPredictionViewModel, FragmentSearchBinding>() {

    override val viewModel by viewModels<SearchPredictionViewModel>()

    override val bindingInflation: (inflater: LayoutInflater, container: ViewGroup?, attachToParent: Boolean) -> FragmentSearchBinding
        get() = FragmentSearchBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val showDetails: (id: String, title: String) -> Unit = { id, title ->
            hideKeyboard()
            viewModel.showDetails(R.id.fragment_container, id, title)
        }

        val historyAdapter = SearchHistoryAdapter(showDetails)
        binding.searchHistoryRecyclerview.apply {
            adapter = historyAdapter
            addItemDecoration(
                MarginItemDecoration(spaceRight = resources.getDimensionPixelSize(R.dimen.history_item_right_margin)))

            viewModel.observeHistory(this@SearchFragment) { history ->
                historyAdapter.submitList(history)
                doOnPreDraw { scrollToPosition(0) }
            }
        }

        val predictionAdapter = SearchLocationAdapter(showDetails)
        binding.searchPredictionRecyclerView.apply {
            adapter = predictionAdapter
            addItemDecoration(
                MarginItemDecoration(spaceBottom = resources.getDimensionPixelSize(R.dimen.prediction_item_bottom_margin)))
        }
        viewModel.observe(this) { locations ->
            predictionAdapter.submitList(locations.toMutableList())
        }

        if (savedInstanceState == null) viewModel.refreshHistory()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) viewModel.refreshHistory()
        super.onHiddenChanged(hidden)
    }

    override fun onDestroy() {
        requireActivity().findViewById<Toolbar>(R.id.toolbar).menu.findItem(R.id.action_search)
            .collapseActionView()
        super.onDestroy()
    }
}