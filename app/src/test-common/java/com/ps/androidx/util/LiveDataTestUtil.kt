package com.ps.androidx.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * @Author: Prokash Sarkar
 * @Date: 5/30/19
 */

class LiveDataTestUtil {

    companion object {

        /**
         * Observes a LiveData and returns it's value from onChanged()
         */
        @Throws(InterruptedException::class)
        fun <T> getValue(liveData: LiveData<T>): T? {

            val data = ArrayList<T>()

            // latch for blocking thread until data is set
            val latch = CountDownLatch(1)

            val observer = object : Observer<T> {
                override fun onChanged(t: T) {
                    data.add(t)
                    latch.countDown() // release the latch
                    liveData.removeObserver(this)
                }
            }

            liveData.observeForever(observer)

            try {
                latch.await(2, TimeUnit.SECONDS) // wait for onChanged to fire and set data
            } catch (e: InterruptedException) {
                throw InterruptedException("Latch failure")
            }

            return if (data.size > 0) {
                data[0]
            } else null
        }

    }

}