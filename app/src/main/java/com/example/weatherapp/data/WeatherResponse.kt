package com.example.weatherapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class WeatherResponse(
    val coord: @RawValue Coordinates,
    val weather: @RawValue List<Weather>,
    val base: String,
    val main: @RawValue Main,
    val visibility: Int,
    val wind: @RawValue Wind,
    val clouds: @RawValue Clouds,
    val dt: Long,
    val sys: @RawValue Sys,
    val timezone: Int,
    val id: Long,
    val name: String,
    val cod: Int
) : Parcelable