package com.example.weatherapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.data.*
import com.example.weatherapp.api.WeatherService
import com.example.weatherapp.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class WeatherViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var weatherService: WeatherService
    private lateinit var viewModel: WeatherViewModel
    private val apiKey = RetrofitClient.API_KEY

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        weatherService = mock()
        viewModel = WeatherViewModel(weatherService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getWeather success updates weather data`() = runTest {
        // Given
        val zipCode = "55105"
        val mockResponse = WeatherResponse(
            coord = Coordinates(0.0, 0.0),
            weather = listOf(Weather(800, "Clear", "clear sky", "01d")),
            base = "stations",
            main = Main(72.0, 70.0, 68.0, 75.0, 1015, 65),
            visibility = 10000,
            wind = Wind(5.0, 180),
            clouds = Clouds(0),
            dt = 1616788800,
            sys = Sys(1, 1, "US", 1616788800, 1616832000),
            timezone = -18000,
            id = 1L,
            name = "St. Paul",
            cod = 200
        )

        whenever(weatherService.getCurrentWeatherByZip(zipCode, apiKey)).thenReturn(mockResponse)

        // When
        viewModel.getWeather(zipCode)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(mockResponse, viewModel.weather.value)
        assertNull(viewModel.error.value)
        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `getWeather failure updates error message`() = runTest {
        // Given
        val zipCode = "invalid"
        val errorMessage = "City not found"

        whenever(weatherService.getCurrentWeatherByZip(zipCode, apiKey)).thenThrow(RuntimeException(errorMessage))

        // When
        viewModel.getWeather(zipCode)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(errorMessage, viewModel.error.value)
        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `getWeatherByLocation success updates weather data`() = runTest {
        // Given
        val latitude = 44.9537
        val longitude = -93.0900
        val mockResponse = WeatherResponse(
            coord = Coordinates(longitude, latitude),
            weather = listOf(Weather(800, "Clear", "clear sky", "01d")),
            base = "stations",
            main = Main(72.0, 70.0, 68.0, 75.0, 1015, 65),
            visibility = 10000,
            wind = Wind(5.0, 180),
            clouds = Clouds(0),
            dt = 1616788800,
            sys = Sys(1, 1, "US", 1616788800, 1616832000),
            timezone = -18000,
            id = 1L,
            name = "St. Paul",
            cod = 200
        )

        whenever(weatherService.getCurrentWeather("$latitude,$longitude", apiKey)).thenReturn(mockResponse)

        // When
        viewModel.getWeatherByLocation(latitude, longitude)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(mockResponse, viewModel.weather.value)
        assertNull(viewModel.error.value)
        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `getWeatherByLocation failure updates error message`() = runTest {
        // Given
        val latitude = 0.0
        val longitude = 0.0
        val errorMessage = "Location not found"

        whenever(weatherService.getCurrentWeather("$latitude,$longitude", apiKey))
            .thenThrow(RuntimeException(errorMessage))

        // When
        viewModel.getWeatherByLocation(latitude, longitude)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(errorMessage, viewModel.error.value)
        assertEquals(false, viewModel.isLoading.value)
    }
} 