package com.example.weatherapp

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        // Observe weather data
        viewModel.weatherData.observe(this) { weather ->
            updateUI(weather)
        }

        // Observe loading state
        viewModel.isLoading.observe(this) { isLoading ->
            findViewById<View>(R.id.loader).visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observe errors
        viewModel.error.observe(this) { error ->
            findViewById<TextView>(R.id.errorText).apply {
                text = error
                visibility = View.VISIBLE
            }
        }

        // For now, hardcode the city and API key
        viewModel.fetchWeather("St. Paul", "7eca67a15ebf3fc6374e4527a6f8a0fe")
    }

    private fun updateUI(weather: WeatherResponse) {
        // Update location
        findViewById<TextView>(R.id.address).text = weather.name

        // Update time
        val dateFormat = SimpleDateFormat("MMM d, yyyy h:mm a", Locale.getDefault())
        findViewById<TextView>(R.id.updated_at).text = getString(
            R.string.updated_at,
            dateFormat.format(Date(weather.dt * 1000))
        )

        // Update temperature
        findViewById<TextView>(R.id.temp).text = getString(
            R.string.temperature,
            weather.main.temp.toInt()
        )

        // Update feels like
        findViewById<TextView>(R.id.feels_like).text = getString(
            R.string.feels_like,
            weather.main.feelsLike.toInt()
        )

        // Update weather status
        findViewById<TextView>(R.id.status).text = getString(
            R.string.weather_status,
            weather.weather.firstOrNull()?.description?.capitalize() ?: ""
        )

        // Update temperature range
        findViewById<TextView>(R.id.temp_range).text = getString(
            R.string.temp_range,
            weather.main.tempMin.toInt(),
            weather.main.tempMax.toInt()
        )

        // Update humidity
        findViewById<TextView>(R.id.humidity).text = getString(
            R.string.humidity,
            weather.main.humidity
        )

        // Update pressure
        findViewById<TextView>(R.id.pressure).text = getString(
            R.string.pressure,
            weather.main.pressure
        )
    }
} 