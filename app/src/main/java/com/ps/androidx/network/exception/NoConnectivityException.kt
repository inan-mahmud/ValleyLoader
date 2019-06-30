package com.ps.androidx.network.exception

import java.io.IOException

/**
 * Created by Prokash Sarkar on 5/25/2019.
 * https://github.com/prokash-sarkar
 */

class NoConnectivityException : IOException() {

    override val message: String?
        get() = "No network available, please check your WiFi or Data connection!"
}