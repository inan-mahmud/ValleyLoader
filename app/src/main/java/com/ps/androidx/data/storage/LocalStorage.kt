package com.ps.androidx.data.storage

import io.reactivex.Observable

/**
 * Created by Prokash Sarkar on 5/22/2019.
 * https://github.com/prokash-sarkar
 */

interface LocalStorage {

    fun writeMessage(key: String, message: String)

    fun readMessage(key: String): String?

    fun readObservableMessage(key: String): Observable<String>?
}