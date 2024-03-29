package com.ps.androidx.util

import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.CoreMatchers
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Created by Prokash Sarkar on 6/22/2019.
 * https://github.com/prokash-sarkar
 */

/**
 * Waiting adapter showing items inside its RecyclerView
 * before continue the test.
 */
fun waitForAdapterChangeWithPagination(recyclerView: RecyclerView, testRule: CountingTaskExecutorRule, count: Int) {
    val latch = CountDownLatch(count)
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
        recyclerView.adapter?.registerAdapterDataObserver(
            object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    latch.countDown()
                }

                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    latch.countDown()
                }
            })
    }
    testRule.drainTasks(1, TimeUnit.SECONDS)
    if (recyclerView.adapter?.itemCount ?: 0 > 0) {
        return //already loaded
    }
    ViewMatchers.assertThat(latch.await(1, TimeUnit.SECONDS), CoreMatchers.`is`(true))

}