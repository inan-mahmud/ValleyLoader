package com.ps.androidx.data

/**
 * Created by Prokash Sarkar on 5/22/2019.
 * https://github.com/prokash-sarkar
 */

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */

class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {
        fun <T> loading(data: T?): Resource<T> {
            return Resource(
                status = Status.LOADING,
                data = data,
                message = null
            )
        }

        fun <T> success(data: T?): Resource<T> {
            return Resource(
                status = Status.SUCCESS,
                data = data,
                message = null
            )
        }

        fun <T> error(msg: String?, data: T?): Resource<T> {
            return Resource(
                status = Status.ERROR,
                data = data,
                message = msg
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Resource<*>

        if (status != other.status) return false
        if (data != other.data) return false
        if (message != other.message) return false

        return true
    }

    override fun hashCode(): Int {
        var result = status.hashCode()
        result = 31 * result + (data?.hashCode() ?: 0)
        result = 31 * result + (message?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Resource(status=$status, data=$data, message=$message)"
    }

    enum class Status {
        LOADING,
        SUCCESS,
        ERROR
    }

}
