package com.ps.androidx.network.interceptor

import android.content.Context
import com.ps.androidx.network.observer.ConnectionObserver
import com.ps.androidx.network.exception.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by Prokash Sarkar on 5/25/2019.
 * https://github.com/prokash-sarkar
 */

class NetworkConnectionInterceptor(val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (ConnectionObserver.isConnectedToInternet(context)) {
            chain.proceed(chain.request())
        } else {
            throw NoConnectivityException()
        }
    }

}