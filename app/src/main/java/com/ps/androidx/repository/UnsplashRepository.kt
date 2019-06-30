package com.ps.androidx.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import com.ps.androidx.data.Resource
import com.ps.androidx.data.model.unsplash.Unsplash
import com.ps.androidx.network.ApiErrorHandler
import com.ps.androidx.network.ApiService
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Prokash Sarkar on 5/22/2019.
 * https://github.com/prokash-sarkar
 */

open class UnsplashRepository
constructor(private val apiService: ApiService) {

    open fun fetchPagedUnsplashData(page: Int): Flowable<List<Unsplash>> {
        return apiService.fetchUnsplashData()
    }

    open fun fetchUnsplashData(page: Int): LiveData<Resource<List<Unsplash>>> {
        return LiveDataReactiveStreams.fromPublisher(
            apiService.fetchUnsplashData()
                .subscribeOn(Schedulers.io())
                .map { d ->
                    Resource.success(d)
                }
                .onErrorReturn { e ->
                    Resource.error(ApiErrorHandler.getErrorByThrowable(e), null)
                }
        )
    }
}