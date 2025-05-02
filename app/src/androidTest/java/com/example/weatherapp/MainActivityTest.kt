package com.example.weatherapp

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    @Test
    fun testInitialUIState() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // Check if all UI elements are visible
            onView(withId(R.id.zipCodeInput))
                .check(matches(isDisplayed()))
            onView(withId(R.id.searchButton))
                .check(matches(isDisplayed()))
            onView(withId(R.id.locationButton))
                .check(matches(isDisplayed()))
            onView(withId(R.id.forecastButton))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun testSearchWeatherWithValidZipCode() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // Enter valid zip code
            onView(withId(R.id.zipCodeInput))
                .perform(typeText("55105"))

            // Click search button
            onView(withId(R.id.searchButton))
                .perform(click())

            // Check if loader is visible
            onView(withId(R.id.loader))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun testSearchWeatherWithInvalidZipCode() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // Enter invalid zip code
            onView(withId(R.id.zipCodeInput))
                .perform(typeText("invalid"))

            // Click search button
            onView(withId(R.id.searchButton))
                .perform(click())

            // Check if error message is displayed
            onView(withId(R.id.errorText))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun testLocationButtonClick() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // Click location button
            onView(withId(R.id.locationButton))
                .perform(click())

            // Check if loader is visible
            onView(withId(R.id.loader))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun testForecastButtonClick() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // Enter valid zip code
            onView(withId(R.id.zipCodeInput))
                .perform(typeText("55105"))

            // Click forecast button
            onView(withId(R.id.forecastButton))
                .perform(click())

            // Check if ForecastActivity is launched
            onView(withId(R.id.forecastList))
                .check(matches(isDisplayed()))
        }
    }
} 