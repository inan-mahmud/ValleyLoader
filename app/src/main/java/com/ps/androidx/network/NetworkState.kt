package com.ps.androidx.network

/**
 * Created by Prokash Sarkar on 6/18/2019.
 * https://github.com/prokash-sarkar
 */
data class NetworkState(val state: State) {

    lateinit var message: String

    constructor(message: String, state: State) : this(state) {
        this.message = message
    }

    enum class State {
        LOADING,
        SUCCESS,
        ERROR
    }
}
