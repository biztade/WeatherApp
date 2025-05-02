package com.example.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.WeatherService
import com.example.weatherapp.data.WeatherResponse
import kotlinx.coroutines.launch

class WeatherViewModel(private val weatherService: WeatherService) : ViewModel() {
    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse> = _weatherData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadWeather(cityName: String, apiKey: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val response = weatherService.getCurrentWeather(cityName, apiKey)
                _weatherData.value = response
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load weather"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadWeatherByZip(zipCode: String, apiKey: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val response = weatherService.getCurrentWeatherByZip(zipCode, apiKey)
                _weatherData.value = response
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load weather"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class WeatherViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(WeatherService.create()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 