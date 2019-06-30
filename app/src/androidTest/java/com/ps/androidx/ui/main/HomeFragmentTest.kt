package com.ps.androidx.ui.main

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.ps.androidx.R
import com.ps.androidx.base.TestAndroidBase
import com.ps.androidx.util.hasItemCount
import com.ps.androidx.util.waitForAdapterChangeWithPagination
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import org.junit.runner.RunWith
import java.net.HttpURLConnection

/**
 * Created by Prokash Sarkar on 6/22/2019.
 * https://github.com/prokash-sarkar
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
internal class HomeFragmentTest : TestAndroidBase() {

    override fun isMockServerEnabled(): Boolean = true

    @Test
    fun whenAuthenticationFails() {
        mockHttpResponse("fetchUnsplashData_whenError.json", HttpURLConnection.HTTP_UNAUTHORIZED)

        onView(withId(R.id.ivInfoImage))
            .check(matches(isDisplayed()))
        onView(withId(R.id.tvInfoText))
            .check(matches(isDisplayed()))
        onView(withId(R.id.tvInfoText))
            .check(matches(withText(containsString(getString(R.string.error_processing_request)))))
        onView(withId(R.id.btnRetry))
            .check(matches(isDisplayed()))
    }

    @Test
    fun whenDataFetchIsSuccessful() {
        mockHttpResponse("fetchUnsplashData_whenSuccess.json", HttpURLConnection.HTTP_OK)

        waitForAdapterChangeWithPagination(getRecyclerView(), executorRule, 1)
        onView(withId(R.id.rvImages)).check(matches((hasItemCount(10))))
    }

    @Test
    fun whenDataFetchHasNoContents() {
        mockHttpResponse("fetchUnsplashData_whenEmpty.json", HttpURLConnection.HTTP_OK)

        onView(withId(R.id.ivInfoImage))
            .check(matches(not(isDisplayed())))
        onView(withId(R.id.tvInfoText))
            .check(matches(isDisplayed()))
        onView(withId(R.id.tvInfoText))
            .check(matches(withText(containsString(getString(R.string.hint_no_result)))))
        onView(withId(R.id.btnRetry))
            .check(matches(not(isDisplayed())))
    }

    /**
     * Convenient access to String resources
     */
    private fun getString(id: Int) = activityRule.activity.getString(id)

    /**
     * Convenient access to [HomeFragment]'s RecyclerView
     */
    private fun getRecyclerView() = activityRule.activity.findViewById<RecyclerView>(R.id.rvImages)

}