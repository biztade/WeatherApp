package com.example.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.WeatherResponse
import com.example.weatherapp.api.WeatherService
import com.example.weatherapp.network.RetrofitClient
import kotlinx.coroutines.launch

class WeatherViewModel(private val weatherService: WeatherService) : ViewModel() {
    private val _weather = MutableLiveData<WeatherResponse>()
    val weather: LiveData<WeatherResponse> = _weather

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getWeather(zipCode: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val response = weatherService.getCurrentWeatherByZip(zipCode, RetrofitClient.API_KEY)
                _weather.value = response
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getWeatherByLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val response = weatherService.getCurrentWeather("$latitude,$longitude", RetrofitClient.API_KEY)
                _weather.value = response
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class WeatherViewModelFactory(private val weatherService: WeatherService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(weatherService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 