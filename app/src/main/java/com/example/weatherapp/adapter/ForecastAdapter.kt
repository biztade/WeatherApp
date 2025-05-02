package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.ForecastItem
import com.example.weatherapp.util.WeatherIconUtil
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ForecastAdapter : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {
    private var forecastItems: List<ForecastItem> = emptyList()

    fun updateForecast(newForecast: List<ForecastItem>) {
        // Filter to show only one forecast per day (at noon)
        forecastItems = newForecast.filter { item ->
            val date = Date(item.dt * 1000)
            val hour = SimpleDateFormat("HH", Locale.getDefault()).format(date).toInt()
            hour == 12
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_forecast, parent, false)
        return ForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(forecastItems[position])
    }

    override fun getItemCount() = forecastItems.size

    class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateText: TextView = itemView.findViewById(R.id.dateText)
        private val weatherDescription: TextView = itemView.findViewById(R.id.weatherDescription)
        private val weatherIcon: ImageView = itemView.findViewById(R.id.weatherIcon)
        private val highTemp: TextView = itemView.findViewById(R.id.highTemp)
        private val lowTemp: TextView = itemView.findViewById(R.id.lowTemp)

        private val dateFormat = SimpleDateFormat("EEEE, MMM d", Locale.getDefault())

        fun bind(forecastItem: ForecastItem) {
            // Format date
            val date = Date(forecastItem.dt * 1000)
            dateText.text = dateFormat.format(date)

            // Set weather description
            val weather = forecastItem.weather.firstOrNull()
            weatherDescription.text = weather?.description?.replaceFirstChar { it.uppercase() }

            // Set weather icon
            weather?.icon?.let { iconCode ->
                WeatherIconUtil.setWeatherIcon(weatherIcon, iconCode)
            }

            // Set temperatures
            highTemp.text = "${forecastItem.main.tempMax.toInt()}°F"
            lowTemp.text = "${forecastItem.main.tempMin.toInt()}°F"
        }
    }
} 