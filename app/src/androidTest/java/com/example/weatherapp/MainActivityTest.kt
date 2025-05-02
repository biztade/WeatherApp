package com.example.weatherapp

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import org.hamcrest.CoreMatchers.not
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
            
            // Initially, the weather info section should be empty/default
            onView(withId(R.id.detailsContainer))
                .check(matches(isDisplayed()))
                
            // Initially, error text should not be visible
            onView(withId(R.id.errorText))
                .check(matches(not(isDisplayed())))
            
            // Initially, loader should not be visible
            onView(withId(R.id.loader))
                .check(matches(not(isDisplayed())))
        }
    }

    @Test
    fun testSearchWeatherWithValidZipCode() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // Enter valid zip code
            onView(withId(R.id.zipCodeInput))
                .perform(typeText("55105"), closeSoftKeyboard())

            // Click search button
            onView(withId(R.id.searchButton))
                .perform(click())

            // Check if loader is visible (initially)
            onView(withId(R.id.loader))
                .check(matches(isDisplayed()))
                
            // Wait for potential response (in a real scenario, this might involve idling resources)
            Thread.sleep(3000)
                
            // After loading, either weather data or error should be shown
            // This is a simplistic check - in a real app with a mock API we would verify exact data
            onView(withId(R.id.loader))
                .check(matches(not(isDisplayed())))
        }
    }

    @Test
    fun testSearchWeatherWithEmptyZipCode() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // Don't enter any zip code

            // Click search button
            onView(withId(R.id.searchButton))
                .perform(click())

            // Loader should not appear as the search won't be triggered
            onView(withId(R.id.loader))
                .check(matches(not(isDisplayed())))
                
            // Error text should not appear
            onView(withId(R.id.errorText))
                .check(matches(not(isDisplayed())))
        }
    }

    @Test
    fun testLocationButtonClick() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // Click location button
            onView(withId(R.id.locationButton))
                .perform(click())

            // With permissions granted in the test rule, this should trigger location service
            // Wait briefly to allow service to start
            Thread.sleep(1000)
            
            // This is difficult to test conclusively without mocking location services
            // In a real test, we would use IdlingResource to wait for the location to be available
        }
    }

    @Test
    fun testForecastButtonClick() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // Enter valid zip code
            onView(withId(R.id.zipCodeInput))
                .perform(typeText("55105"), closeSoftKeyboard())

            // Click forecast button
            onView(withId(R.id.forecastButton))
                .perform(click())

            // Check if ForecastActivity is launched by verifying one of its views is visible
            onView(withId(R.id.forecastList))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun testForecastButtonWithoutZipCode() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // Don't enter any zip code

            // Click forecast button
            onView(withId(R.id.forecastButton))
                .perform(click())

            // Activity should not change, so our main activity UI elements should still be visible
            onView(withId(R.id.zipCodeInput))
                .check(matches(isDisplayed()))
        }
    }
    
    @Test
    fun testWeatherDisplayElements() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // Check all the weather display elements are present
            onView(withId(R.id.locationText))
                .check(matches(isDisplayed()))
            onView(withId(R.id.temperatureText))
                .check(matches(isDisplayed()))
            onView(withId(R.id.weatherConditionText))
                .check(matches(isDisplayed()))
            onView(withId(R.id.humidityText))
                .check(matches(isDisplayed()))
            onView(withId(R.id.windSpeedText))
                .check(matches(isDisplayed()))
        }
    }
} 