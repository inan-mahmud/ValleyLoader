package com.ps.valleyloader.utils

/**
 * Created by Prokash Sarkar on 5/22/2019.
 * https://github.com/prokash-sarkar
 */

class AppConfig {

    companion object {

        val LOGTAG = "ValleyLoader"

        val BASE_TEST_URL = "http://localhost:9191/"

        val HTTP_CACHE_DIR = "okhttp_cache"
        val HTTP_CACHE_SIZE: Long = 10 * 1024 * 1024 //10MB
        val HTTP_CONNECT_TIMEOUT: Long = 60
        val HTTP_READ_TIMEOUT: Long = 60
        val HTTP_WRITE_TIMEOUT: Long = 60

    }

}