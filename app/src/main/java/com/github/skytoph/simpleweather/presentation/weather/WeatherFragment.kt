package com.github.skytoph.simpleweather.presentation.weather

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.github.skytoph.simpleweather.R
import com.github.skytoph.simpleweather.core.presentation.BaseFragment
import com.github.skytoph.simpleweather.core.presentation.MarginItemDecoration
import com.github.skytoph.simpleweather.core.presentation.broadcast.BroadcastReceiverAction
import com.github.skytoph.simpleweather.core.presentation.view.visibility.Visibility
import com.github.skytoph.simpleweather.databinding.FragmentWeatherBinding
import com.github.skytoph.simpleweather.presentation.weather.WeatherViewModel.Companion.FAVORITE_KEY
import com.github.skytoph.simpleweather.presentation.weather.WeatherViewModel.Companion.PLACE_ID_KEY
import com.github.skytoph.simpleweather.presentation.weather.adapter.forecast.DailyForecastAdapter
import com.github.skytoph.simpleweather.presentation.weather.adapter.forecast.HourlyForecastAdapter
import com.github.skytoph.simpleweather.presentation.weather.adapter.warning.NotScrollableLayoutManager
import com.github.skytoph.simpleweather.presentation.weather.adapter.warning.WarningAdapter
import com.github.skytoph.simpleweather.presentation.weather.model.ForecastUi
import com.github.skytoph.simpleweather.presentation.weather.model.WarningUi
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WeatherFragment : BaseFragment<WeatherViewModel, FragmentWeatherBinding>() {

    override val viewModel by viewModels<WeatherViewModel>()

    private val tickReceiver = BroadcastReceiverAction {
        if (lifecycle.currentState == Lifecycle.State.RESUMED)
            viewModel.refreshFromCache()
    }

    override val bindingInflation: (inflater: LayoutInflater, container: ViewGroup?, attachToParent: Boolean) -> FragmentWeatherBinding
        get() = FragmentWeatherBinding::inflate

    private lateinit var warningAdapter: WarningAdapter
    private lateinit var hourlyForecastAdapter: HourlyForecastAdapter
    private lateinit var dailyForecastAdapter: DailyForecastAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        warningAdapter = WarningAdapter()
        binding.warningsRecyclerview.apply {
            adapter = warningAdapter
            layoutManager = NotScrollableLayoutManager(context)
            addItemDecoration(MarginItemDecoration(spaceBottom = resources.getDimensionPixelSize(R.dimen.warning_item_bottom_margin)))
        }

        hourlyForecastAdapter = HourlyForecastAdapter()
        binding.forecastHourlyRecyclerview.apply {
            adapter = hourlyForecastAdapter
            hourlyForecastAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT
            addItemDecoration(MarginItemDecoration(spaceRight = resources.getDimensionPixelSize(R.dimen.forecast_item_right_margin)))
        }

        dailyForecastAdapter = DailyForecastAdapter()
        binding.forecastDailyRecyclerview.apply {
            adapter = dailyForecastAdapter
            layoutManager = NotScrollableLayoutManager(context)
            addItemDecoration(MarginItemDecoration(spaceBottom = resources.getDimensionPixelSize(R.dimen.forecast_item_bottom_margin)))
        }

        viewModel.observe(this) { weather ->
            binding.apply {
                weather.show(
                    weatherError,
                    locationView,
                    indicatorsView,
                    sunriseSunsetView,
                    weatherAttribution,
                    listOf(forecastDailyRecyclerview, forecastHourlyRecyclerview),
                    showLists
                )
            }
        }
        viewModel.observeRefresh(this) { refresh ->
            if (refresh) viewModel.refreshFromCache()
        }
        viewModel.getWeather(savedInstanceState == null)
    }

    override fun onResume() {
        requireContext().registerReceiver(tickReceiver, IntentFilter(Intent.ACTION_TIME_TICK))
        super.onResume()
    }

    override fun onPause() {
        requireContext().unregisterReceiver(tickReceiver)
        super.onPause()
    }

    private val showLists: (List<WarningUi>, List<ForecastUi.Hourly>, List<ForecastUi.Daily>) -> Unit =
        { warnings, hourly, daily ->
            binding.apply {
                warningAdapter.submitList(warnings)
                hourlyForecastAdapter.submitList(hourly)
                dailyForecastAdapter.submitList(daily)
                if (hourly.isEmpty()) Visibility.Gone().apply(forecastHourlyRecyclerview)
            }
        }

    companion object {
        fun newInstance(placeId: String, favorite: Boolean): WeatherFragment =
            WeatherFragment().apply {
                arguments = bundleOf(PLACE_ID_KEY to placeId, FAVORITE_KEY to favorite)
            }
    }
}