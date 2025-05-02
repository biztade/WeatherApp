package com.example.weatherapp.network

import com.example.weatherapp.data.WeatherResponse
import com.example.weatherapp.data.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    suspend fun getWeather(
        @Query("zip") zipCode: String,
        @Query("appid") apiKey: String = RetrofitClient.API_KEY,
        @Query("units") units: String = "imperial"
    ): WeatherResponse

    @GET("forecast")
    suspend fun getForecast(
        @Query("zip") zipCode: String,
        @Query("appid") apiKey: String = RetrofitClient.API_KEY,
        @Query("units") units: String = "imperial"
    ): ForecastResponse

    @GET("weather")
    suspend fun getWeatherByLocation(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String = RetrofitClient.API_KEY,
        @Query("units") units: String = "imperial"
    ): WeatherResponse

    @GET("forecast")
    suspend fun getForecastByLocation(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String = RetrofitClient.API_KEY,
        @Query("units") units: String = "imperial"
    ): ForecastResponse
} 