package com.ps.androidx.network

import com.ps.androidx.network.exception.NoConnectivityException
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by Prokash Sarkar on 5/25/2019.
 * https://github.com/prokash-sarkar
 */

/**
 * General utility functions to parse error message from various throwable and response types
 */

class ApiErrorHandler {

    companion object {
        private const val ERROR_TIMEOUT = "Server Timeout!"
        private const val ERROR_UNABLE_TO_REACH = "Unable to reach server!"
        private const val ERROR_NO_INTERNET_CONNECTION = "No internet connection!"
        private const val ERROR_DATA_CONVERSION = "Failed to parse data!"
        private const val ERROR_UNKNOWN = "An unknown error occurred!"

        /**
         * Returns a readable error message from Throwable object
         *
         * @param t       throwable object
         * @return error message in String
         */
        fun getErrorByThrowable(t: Throwable?): String {
            if (t?.message != null) {
                return if (t is HttpException) {
                    getErrorFromJson(t.response().errorBody()) // We had non-2XX http error
                } else return when (t) {
                    is SocketTimeoutException -> // Connection timeout
                        ERROR_TIMEOUT
                    is UnknownHostException -> // Remote host is currently unreachable
                        ERROR_UNABLE_TO_REACH
                    is NoConnectivityException -> // A network error happened
                        ERROR_NO_INTERNET_CONNECTION
                    is IOException -> // A conversion error happened
                        ERROR_DATA_CONVERSION
                    else -> t.message.toString()
                }
            } else {
                return ERROR_UNKNOWN
            }
        }

        /**
         * Parses an error message from JSONObject
         *
         * @param responseBody ResponseBody object
         * @return String value from JSONObject
         */
        private fun getErrorFromJson(responseBody: ResponseBody?): String {
            return try {
                val jsonObject = JSONObject(responseBody!!.string())
                // TODO(PS): CHANGE THIS FROM THE ACTUAL API ERROR RESPONSE
                jsonObject.getString("status_message")
            } catch (e: Exception) {
                e.message.toString()
            }
        }
    }

}