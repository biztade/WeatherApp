package com.example.weatherapp.network

import com.example.weatherapp.api.WeatherService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitClient {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val API_KEY = "7eca67a15ebf3fc6374e4527a6f8a0fe"

    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val weatherService: WeatherService = WeatherService.create()

    fun getApiUrl(endpoint: String, params: Map<String, String>): String {
        val paramString = params.entries.joinToString("&") { "${it.key}=${it.value}" }
        return "$BASE_URL$endpoint?$paramString&appid=$API_KEY&units=imperial"
    }
} 