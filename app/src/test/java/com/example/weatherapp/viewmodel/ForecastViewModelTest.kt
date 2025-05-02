package com.example.weatherapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.data.*
import com.example.weatherapp.api.WeatherService
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
class ForecastViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var weatherService: WeatherService
    private lateinit var viewModel: ForecastViewModel
    private val apiKey = "test_api_key"

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        weatherService = mock()
        viewModel = ForecastViewModel(weatherService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadForecast success updates forecast data`() = runTest {
        // Given
        val zipCode = "55105"
        val mockResponse = ForecastResponse(
            cod = "200",
            message = 0,
            cnt = 16,
            forecastList = listOf(
                ForecastItem(
                    dt = 1616788800,
                    main = Main(72.0, 70.0, 68.0, 75.0, 1015, 65),
                    weather = listOf(Weather(800, "Clear", "clear sky", "01d")),
                    clouds = Clouds(0),
                    wind = Wind(5.0, 180),
                    visibility = 10000,
                    pop = 0.0,
                    dtTxt = "2024-03-25 12:00:00"
                )
            ),
            city = City(
                id = 1,
                name = "St. Paul",
                coord = Coordinates(0.0, 0.0),
                country = "US",
                population = 300000,
                timezone = -18000,
                sunrise = 1616788800,
                sunset = 1616832000
            )
        )

        whenever(weatherService.getForecast(zipCode, apiKey)).thenReturn(mockResponse)

        // When
        viewModel.loadForecast(zipCode, apiKey)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(mockResponse, viewModel.forecastData.value)
        assertNull(viewModel.error.value)
        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `loadForecast failure updates error message`() = runTest {
        // Given
        val zipCode = "invalid"
        val errorMessage = "City not found"

        whenever(weatherService.getForecast(zipCode, apiKey))
            .thenThrow(RuntimeException(errorMessage))

        // When
        viewModel.loadForecast(zipCode, apiKey)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(errorMessage, viewModel.error.value)
        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `getForecastByLocation success updates forecast data`() = runTest {
        // Given
        val latitude = 44.9537
        val longitude = -93.0900
        val mockResponse = ForecastResponse(
            cod = "200",
            message = 0,
            cnt = 40,
            forecastList = listOf(
                ForecastItem(
                    dt = 1616788800,
                    main = Main(72.0, 70.0, 68.0, 75.0, 1015, 65),
                    weather = listOf(Weather(800, "Clear", "clear sky", "01d")),
                    clouds = Clouds(0),
                    wind = Wind(5.0, 180),
                    visibility = 10000,
                    pop = 0.0,
                    dtTxt = "2024-03-26 12:00:00"
                )
            ),
            city = City(
                id = 1,
                name = "St. Paul",
                coord = Coordinates(longitude, latitude),
                country = "US",
                population = 300000,
                timezone = -18000,
                sunrise = 1616788800,
                sunset = 1616832000
            )
        )

        whenever(weatherService.getForecastByLocation(latitude, longitude, apiKey)).thenReturn(mockResponse)

        // When
        viewModel.getForecastByLocation(latitude, longitude, apiKey)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(mockResponse, viewModel.forecastData.value)
        assertNull(viewModel.error.value)
        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `getForecastByLocation failure updates error message`() = runTest {
        // Given
        val latitude = 0.0
        val longitude = 0.0
        val errorMessage = "Location not found"

        whenever(weatherService.getForecastByLocation(latitude, longitude, apiKey))
            .thenThrow(RuntimeException(errorMessage))

        // When
        viewModel.getForecastByLocation(latitude, longitude, apiKey)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(errorMessage, viewModel.error.value)
        assertEquals(false, viewModel.isLoading.value)
    }
} 