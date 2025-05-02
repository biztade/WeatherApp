package com.example.weatherapp

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.example.weatherapp.ui.ForecastActivity

@RunWith(AndroidJUnit4::class)
class ForecastActivityTest {
    
    // Add the Espresso-Contrib dependency for RecyclerView testing
    // implementation 'androidx.test.espresso:espresso-contrib:3.5.1'
    
    private lateinit var intent: Intent
    
    @Before
    fun setup() {
        // Make sure we have the right context
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        
        // Create intent with valid zip code
        intent = Intent(ApplicationProvider.getApplicationContext(), ForecastActivity::class.java).apply {
            putExtra("zip_code", "55105")
        }
    }

    @Test
    fun testInitialUIStateWithValidZipCode() {
        ActivityScenario.launch<ForecastActivity>(intent).use {
            // Check if all UI elements are visible
            onView(withId(R.id.forecastList))
                .check(matches(isDisplayed()))
            
            // Initially loader should be visible since it's fetching data
            onView(withId(R.id.loader))
                .check(matches(isDisplayed()))
                
            // Error text should not be visible initially
            onView(withId(R.id.errorText))
                .check(matches(not(isDisplayed())))
            
            // Wait for data to potentially load or error (in a real test we would use IdlingResource)
            Thread.sleep(3000)
            
            // After waiting, the loader should be gone
            onView(withId(R.id.loader))
                .check(matches(not(isDisplayed())))
        }
    }

    @Test
    fun testInitialUIStateWithInvalidZipCode() {
        // Create intent with invalid zip code
        val invalidIntent = Intent(ApplicationProvider.getApplicationContext(), ForecastActivity::class.java).apply {
            putExtra("zip_code", "invalid")
        }

        ActivityScenario.launch<ForecastActivity>(invalidIntent).use {
            // Wait briefly to allow potential error to show
            Thread.sleep(1000)
            
            // Check if error message is displayed
            onView(withId(R.id.errorText))
                .check(matches(isDisplayed()))
                
            // RecyclerView should not be visible when there's an error
            onView(withId(R.id.forecastList))
                .check(matches(not(isDisplayed())))
        }
    }

    @Test
    fun testInitialUIStateWithNoZipCode() {
        // Create intent without zip code
        val emptyIntent = Intent(ApplicationProvider.getApplicationContext(), ForecastActivity::class.java)

        ActivityScenario.launch<ForecastActivity>(emptyIntent).use {
            // Check if error message is displayed
            onView(withId(R.id.errorText))
                .check(matches(isDisplayed()))
                
            // RecyclerView should not be visible when there's an error
            onView(withId(R.id.forecastList))
                .check(matches(not(isDisplayed())))
                
            // Loader should not be visible when there's an immediate error
            onView(withId(R.id.loader))
                .check(matches(not(isDisplayed())))
        }
    }
    
    @Test
    fun testRecyclerViewScrolling() {
        ActivityScenario.launch<ForecastActivity>(intent).use { scenario ->
            // Wait for data to potentially load
            Thread.sleep(3000)
            
            // We need to check if there are items before trying to scroll
            scenario.onActivity { activity ->
                val recyclerView = activity.findViewById<RecyclerView>(R.id.forecastList)
                if (recyclerView.adapter != null && recyclerView.adapter!!.itemCount > 0) {
                    // Try scrolling to a position
                    onView(withId(R.id.forecastList))
                        .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
                    
                    // Try scrolling to last position if there are items
                    val itemCount = recyclerView.adapter!!.itemCount
                    if (itemCount > 1) {
                        onView(withId(R.id.forecastList))
                            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(itemCount - 1))
                    }
                }
            }
        }
    }
    
    @Test
    fun testItemClickInRecyclerView() {
        ActivityScenario.launch<ForecastActivity>(intent).use { scenario ->
            // Wait for data to potentially load
            Thread.sleep(3000)
            
            // Try clicking on the first item if available
            scenario.onActivity { activity ->
                val recyclerView = activity.findViewById<RecyclerView>(R.id.forecastList)
                if (recyclerView.adapter != null && recyclerView.adapter!!.itemCount > 0) {
                    onView(withId(R.id.forecastList))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
                }
            }
        }
    }
} 