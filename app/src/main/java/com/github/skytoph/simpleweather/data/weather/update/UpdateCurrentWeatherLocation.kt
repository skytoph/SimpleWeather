package com.github.skytoph.simpleweather.data.weather.update

import com.github.skytoph.simpleweather.data.weather.model.WeatherData

interface UpdateCurrentWeatherLocation {
    fun update(weatherId: Int, temperature: Double, locations: Map<String, String>): WeatherData
}