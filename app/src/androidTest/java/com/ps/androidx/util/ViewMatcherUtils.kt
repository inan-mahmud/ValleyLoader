package com.ps.androidx.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

/**
 * Created by Prokash Sarkar on 6/22/2019.
 * https://github.com/prokash-sarkar
 */

/**
 * [Matcher] helper for [RecyclerView]
 * Count the number of its items
 */
fun hasItemCount(itemCount: Int): Matcher<View> {
    return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {

        override fun describeTo(description: Description) {
            description.appendText("has $itemCount items")
        }

        override fun matchesSafely(view: RecyclerView): Boolean {
            return view.adapter?.itemCount == itemCount
        }
    }
}