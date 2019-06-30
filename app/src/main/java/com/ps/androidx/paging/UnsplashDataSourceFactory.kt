package com.ps.androidx.paging

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.ps.androidx.data.model.unsplash.Unsplash
import com.ps.androidx.network.NetworkState
import javax.inject.Inject

/**
 * Created by Prokash Sarkar on 6/27/2019.
 * https://github.com/prokash-sarkar
 */

open class UnsplashDataSourceFactory @Inject
constructor(private val movieDataSource: UnsplashDataSource) : DataSource.Factory<Int, Unsplash>() {

    override fun create(): DataSource<Int, Unsplash> {
        return movieDataSource
    }

    fun getUnsplashDataSource(): UnsplashDataSource {
        return movieDataSource
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return movieDataSource.networkState
    }

}