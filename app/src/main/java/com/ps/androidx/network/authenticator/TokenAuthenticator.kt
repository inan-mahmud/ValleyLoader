package com.ps.androidx.network.authenticator

import com.ps.androidx.data.model.RefreshTokenResponse
import com.ps.androidx.data.storage.LocalStorage
import com.ps.androidx.network.APIServiceHolder
import com.ps.androidx.util.AppConfig
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject


/**
 * Created by Prokash Sarkar on 5/22/2019.
 * https://github.com/prokash-sarkar
 */

/**
 * OkHttpClient will try to execute the authenticator's authenticate method
 * if a request failed because of 401 unauthorized.
 */
class TokenAuthenticator @Inject
constructor(private val apiServiceHolder: APIServiceHolder, private val localStorage: LocalStorage) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {

        val userId = localStorage.readMessage(AppConfig.PREFS_KEY_USER_ID)
        val refreshToken = localStorage.readMessage(AppConfig.PREFS_KEY_ACCESS_TOKEN)

        // To prevent looping the request for new key
        // Check if "the failed request Authorization key" is different from new authorization key
        if (!response.request().header("Authorization").equals(refreshToken))
            return null

        val retrofitResponse = apiServiceHolder.apiService!!
            .refreshToken(userId, refreshToken)
            .execute()

        val refreshTokenResponse = retrofitResponse.body() as RefreshTokenResponse

        refreshTokenResponse.token.let {
            localStorage.writeMessage(AppConfig.PREFS_KEY_ACCESS_TOKEN, it)

            return response.request().newBuilder()
                .header("Authorization", it)
                .build()
        }
    }
}