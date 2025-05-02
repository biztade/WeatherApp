package com.example.weatherapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Main(
    val temp: Double,
    @SerialName("feels_like")
    val feelsLike: Double,
    @SerialName("temp_min")
    val tempMin: Double,
    @SerialName("temp_max")
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int
) : Parcelable

@Parcelize
@Serializable
data class Wind(
    val speed: Double,
    val deg: Int
) : Parcelable

@Parcelize
@Serializable
data class Clouds(
    val all: Int
) : Parcelable

@Parcelize
@Serializable
data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
) : Parcelable

@Parcelize
@Serializable
data class Coordinates(
    val lon: Double,
    val lat: Double
) : Parcelable

@Parcelize
@Serializable
data class Sys(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Long,
    val sunset: Long
) : Parcelable 