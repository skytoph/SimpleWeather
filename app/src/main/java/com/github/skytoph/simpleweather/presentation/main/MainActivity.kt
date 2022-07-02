package com.github.skytoph.simpleweather.presentation.main

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.skytoph.simpleweather.app.WeatherApp
import com.github.skytoph.simpleweather.core.presentation.navigation.MainViewModel
import com.github.skytoph.simpleweather.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = (application as WeatherApp).mainViewModel

        viewModel.observeNavigation(this) { screen ->
            screen.show(supportFragmentManager)
        }
        viewModel.observeProgress(this) { visibility ->
            visibility.apply(binding.progressBar)
        }
    }
}