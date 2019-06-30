package com.ps.androidx.network.interceptor

import com.ps.androidx.data.storage.LocalStorage
import com.ps.androidx.util.AppConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by Prokash Sarkar on 5/25/2019.
 * https://github.com/prokash-sarkar
 */

class TokenAuthenticatorInterceptor(private val localStorage: LocalStorage) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // Add default headers
        val requestBuilder = chain.request().newBuilder()
            //.addHeader("accept", "*/*")
            //.addHeader("accept-encoding:gzip", "gzip, deflate")
            //.addHeader("accept-language", "en-US,en;q=0.9")

        // Add custom headers
        /*val headers = mutableMapOf<String, String>()
        headers["key"] = "value"

        for ((key, value) in headers) {
            requestBuilder.addHeader(key, value)
        }*/

        // Add token from storage
        /*val token = localStorage.readMessage(AppConfig.PREFS_KEY_ACCESS_TOKEN)
        if (token != null) {
            requestBuilder.addHeader("Authorization", token)
        }*/

        return chain.proceed(requestBuilder.build())
    }

}