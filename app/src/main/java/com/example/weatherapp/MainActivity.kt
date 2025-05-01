package com.example.weatherapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.WeatherResponse
import com.example.weatherapp.ui.ForecastActivity
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.example.weatherapp.viewmodel.WeatherViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: WeatherViewModel
    private lateinit var zipCodeInput: TextInputEditText
    private lateinit var zipCodeInputLayout: TextInputLayout
    private lateinit var viewForecastButton: Button
    private lateinit var loader: ProgressBar
    private lateinit var errorText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        zipCodeInput = findViewById(R.id.zipCodeInput)
        zipCodeInputLayout = findViewById(R.id.zipCodeInputLayout)
        viewForecastButton = findViewById(R.id.viewForecastButton)
        loader = findViewById(R.id.loader)
        errorText = findViewById(R.id.errorText)

        // Initialize ViewModel
        val factory = WeatherViewModelFactory()
        viewModel = ViewModelProvider(this, factory)[WeatherViewModel::class.java]

        // Setup forecast button click listener
        viewForecastButton.setOnClickListener {
            val zipCode = zipCodeInput.text.toString()
            if (isValidZipCode(zipCode)) {
                val intent = Intent(this, ForecastActivity::class.java).apply {
                    putExtra("zip_code", zipCode)
                }
                startActivity(intent)
            } else {
                zipCodeInputLayout.error = getString(R.string.invalid_zip_code)
            }
        }

        // Observe weather data
        viewModel.weatherData.observe(this) { weather ->
            updateUI(weather)
        }

        // Observe loading state
        viewModel.isLoading.observe(this) { isLoading ->
            loader.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observe errors
        viewModel.error.observe(this) { error ->
            error?.let {
                showErrorDialog(it)
                errorText.apply {
                    text = it
                    visibility = View.VISIBLE
                }
            } ?: run {
                errorText.visibility = View.GONE
            }
        }

        // Load initial weather data
        viewModel.loadWeather("New York", getString(R.string.api_key))
    }

    private fun isValidZipCode(zipCode: String): Boolean {
        return zipCode.length == 5 && zipCode.all { it.isDigit() }
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
            weather.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: ""
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

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.error_dialog_title))
            .setMessage(message)
            .setPositiveButton(getString(R.string.error_dialog_ok)) { dialog, _ -> dialog.dismiss() }
            .show()
    }
} 