package com.example.weatherapp

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ForecastActivityTest {
    @Test
    fun testInitialUIStateWithValidZipCode() {
        // Create intent with valid zip code
        val intent = Intent(ApplicationProvider.getApplicationContext(), ForecastActivity::class.java).apply {
            putExtra("zip_code", "55105")
        }

        ActivityScenario.launch<ForecastActivity>(intent).use {
            // Check if all UI elements are visible
            onView(withId(R.id.forecastList))
                .check(matches(isDisplayed()))
            onView(withId(R.id.loader))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun testInitialUIStateWithInvalidZipCode() {
        // Create intent with invalid zip code
        val intent = Intent(ApplicationProvider.getApplicationContext(), ForecastActivity::class.java).apply {
            putExtra("zip_code", "invalid")
        }

        ActivityScenario.launch<ForecastActivity>(intent).use {
            // Check if error message is displayed
            onView(withId(R.id.errorText))
                .check(matches(isDisplayed()))
            onView(withId(R.id.errorText))
                .check(matches(withText(R.string.error_invalid_zip)))
        }
    }

    @Test
    fun testInitialUIStateWithNoZipCode() {
        // Create intent without zip code
        val intent = Intent(ApplicationProvider.getApplicationContext(), ForecastActivity::class.java)

        ActivityScenario.launch<ForecastActivity>(intent).use {
            // Check if error message is displayed
            onView(withId(R.id.errorText))
                .check(matches(isDisplayed()))
            onView(withId(R.id.errorText))
                .check(matches(withText(R.string.error_invalid_zip)))
        }
    }
} 