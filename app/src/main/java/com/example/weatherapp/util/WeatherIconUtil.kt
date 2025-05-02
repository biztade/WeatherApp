package com.example.weatherapp.util

import android.widget.ImageView
import com.example.weatherapp.R

object WeatherIconUtil {
    fun setWeatherIcon(imageView: ImageView, iconCode: String) {
        val iconResId = when (iconCode.take(2)) {
            "01" -> R.drawable.sun // clear sky
            "02" -> R.drawable.cloudy // few clouds
            "03" -> R.drawable.cloudy // scattered clouds
            "04" -> R.drawable.cloudy // broken clouds
            "09" -> R.drawable.rain // shower rain
            "10" -> R.drawable.rain // rain
            "11" -> R.drawable.rain // thunderstorm
            "13" -> R.drawable.windy // snow
            "50" -> R.drawable.windy // mist
            else -> R.drawable.sun // default
        }
        imageView.setImageResource(iconResId)
        imageView.setColorFilter(android.graphics.Color.WHITE)
    }
} 