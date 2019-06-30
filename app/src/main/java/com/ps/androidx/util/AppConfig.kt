package com.ps.androidx.util

/**
 * Created by Prokash Sarkar on 5/22/2019.
 * https://github.com/prokash-sarkar
 */

class AppConfig {

    companion object {

        val LOGTAG = "AndroidX"

        val BASE_URL = "http://pastebin.com/raw/"
        val BASE_TEST_URL = "http://localhost:9090/"

        val DATABASE_NAME = "app_db"

        val SHARED_PREFERENCES_NAME = "shared_preferences"
        val PREFS_KEY_IS_LOGGED_IN = "is_logged_in"
        val PREFS_KEY_USER_ID = "user_id"
        val PREFS_KEY_ACCESS_TOKEN = "user_token"

        val HTTP_CACHE_DIR = "okhttp_cache"
        val HTTP_CACHE_SIZE: Long = 10 * 1024 * 1024 //10MB
        val HTTP_CONNECT_TIMEOUT: Long = 60
        val HTTP_READ_TIMEOUT: Long = 60
        val HTTP_WRITE_TIMEOUT: Long = 60

    }

}