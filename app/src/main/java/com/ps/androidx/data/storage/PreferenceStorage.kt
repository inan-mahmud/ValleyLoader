package com.ps.androidx.data.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.ps.androidx.util.AppConfig
import io.reactivex.Observable


/**
 * Created by Prokash Sarkar on 5/22/2019.
 * https://github.com/prokash-sarkar
 */

class PreferenceStorage(private val context: Context) : LocalStorage {

    override fun writeMessage(key: String, message: String) {
        context.getSharedPreferences(AppConfig.SHARED_PREFERENCES_NAME, MODE_PRIVATE)
            .edit().putString(key, message).apply()
    }

    override fun readMessage(key: String): String? {
        return context.getSharedPreferences(AppConfig.SHARED_PREFERENCES_NAME, MODE_PRIVATE)
            ?.getString(key, "")
    }

    override fun readObservableMessage(key: String): Observable<String>? {
        return Observable.fromCallable {
            context.getSharedPreferences(AppConfig.SHARED_PREFERENCES_NAME, MODE_PRIVATE)
                ?.getString(key, "")
        }
    }

}