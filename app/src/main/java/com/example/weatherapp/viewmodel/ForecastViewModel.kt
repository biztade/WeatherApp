package com.example.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.WeatherService
import com.example.weatherapp.data.ForecastResponse
import kotlinx.coroutines.launch

class ForecastViewModel(private val weatherService: WeatherService) : ViewModel() {
    private val _forecastData = MutableLiveData<ForecastResponse>()
    val forecastData: LiveData<ForecastResponse> = _forecastData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadForecast(zipCode: String, apiKey: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val response = weatherService.getForecast(zipCode, apiKey)
                _forecastData.value = response
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load forecast"
            } finally {
                _isLoading.value = false
            }
        }
    }
} 