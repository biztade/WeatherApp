package com.example.weatherapp.ui

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.adapter.ForecastAdapter
import com.example.weatherapp.viewmodel.ForecastViewModel
import com.example.weatherapp.viewmodel.ForecastViewModelFactory

class ForecastActivity : AppCompatActivity() {
    private lateinit var viewModel: ForecastViewModel
    private lateinit var adapter: ForecastAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var loader: ProgressBar
    private lateinit var errorText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        // Initialize views
        recyclerView = findViewById(R.id.forecastList)
        loader = findViewById(R.id.loader)
        errorText = findViewById(R.id.errorText)

        // Setup RecyclerView
        adapter = ForecastAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ForecastActivity)
            adapter = this@ForecastActivity.adapter
        }

        // Initialize ViewModel
        val factory = ForecastViewModelFactory()
        viewModel = ViewModelProvider(this, factory)[ForecastViewModel::class.java]

        // Observe data
        viewModel.forecastData.observe(this) { forecast ->
            adapter.updateForecast(forecast.forecastList)
            recyclerView.visibility = View.VISIBLE
        }

        viewModel.isLoading.observe(this) { isLoading ->
            loader.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                recyclerView.visibility = View.GONE
                errorText.visibility = View.GONE
            }
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                errorText.apply {
                    text = it
                    visibility = View.VISIBLE
                }
                recyclerView.visibility = View.GONE
            } ?: run {
                errorText.visibility = View.GONE
            }
        }

        // Get zip code from intent and load forecast
        val zipCode = intent.getStringExtra("zip_code")
        if (zipCode != null) {
            viewModel.loadForecast(zipCode, getString(R.string.api_key))
        } else {
            errorText.apply {
                text = getString(R.string.error_invalid_zip)
                visibility = View.VISIBLE
            }
            recyclerView.visibility = View.GONE
        }
    }
} 