package com.example.weatherapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    @SerialName("list")
    val forecastList: List<ForecastItem>,
    val city: City
)

@Serializable
data class City(
    val id: Int,
    val name: String,
    val coord: Coordinates,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)

@Serializable
data class ForecastItem(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val pop: Double,
    @SerialName("dt_txt")
    val dtTxt: String
) 