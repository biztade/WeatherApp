package com.example.weatherapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.service.LocationService
import com.example.weatherapp.ui.ForecastActivity
import com.example.weatherapp.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: WeatherViewModel

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Location permission granted
                startLocationService()
            }
            else -> {
                Toast.makeText(
                    this,
                    R.string.location_permission_required,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private val notificationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(
                this,
                R.string.notification_permission_required,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.searchButton.setOnClickListener {
            val zipCode = binding.zipCodeInput.text.toString()
            if (zipCode.isNotEmpty()) {
                viewModel.getWeather(zipCode)
            }
        }

        binding.locationButton.setOnClickListener {
            checkAndRequestPermissions()
        }

        binding.forecastButton.setOnClickListener {
            val zipCode = binding.zipCodeInput.text.toString()
            if (zipCode.isNotEmpty()) {
                val intent = Intent(this, ForecastActivity::class.java).apply {
                    putExtra("zip_code", zipCode)
                }
                startActivity(intent)
            }
        }
    }

    private fun checkAndRequestPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                startLocationService()
            }
            else -> {
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Notification permission already granted
                }
                else -> {
                    notificationPermissionRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private fun startLocationService() {
        val serviceIntent = Intent(this, LocationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }

    private fun observeViewModel() {
        viewModel.weather.observe(this) { weather ->
            binding.loader.visibility = View.GONE
            binding.errorText.visibility = View.GONE

            binding.locationText.text = weather.name
            binding.temperatureText.text = "${weather.main.temp.toInt()}°F"
            binding.weatherConditionText.text = weather.weather.firstOrNull()?.main ?: ""
            binding.humidityText.text = "${weather.main.humidity}%"
            binding.windSpeedText.text = "${weather.wind.speed} mph"
        }

        viewModel.error.observe(this) { error ->
            binding.loader.visibility = View.GONE
            error?.let {
                binding.errorText.text = it
                binding.errorText.visibility = View.VISIBLE
            } ?: run {
                binding.errorText.visibility = View.GONE
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.loader.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
} 