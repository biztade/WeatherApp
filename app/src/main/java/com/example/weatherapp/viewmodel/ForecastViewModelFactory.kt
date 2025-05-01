package com.example.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.api.WeatherService

class ForecastViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForecastViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ForecastViewModel(WeatherService.create()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 