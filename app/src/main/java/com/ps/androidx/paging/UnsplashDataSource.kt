package com.ps.androidx.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.ps.androidx.data.model.unsplash.Unsplash
import com.ps.androidx.network.ApiErrorHandler
import com.ps.androidx.network.NetworkState
import com.ps.androidx.repository.UnsplashRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Prokash Sarkar on 6/27/2019.
 * https://github.com/prokash-sarkar
 */

class UnsplashDataSource @Inject
constructor(private val unsplashRepository: UnsplashRepository) : PageKeyedDataSource<Int, Unsplash>() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    // LiveData
    val networkState = MutableLiveData<NetworkState>()

    // Keep reference of the last query for retrying
    private var retryQuery: (() -> Any)? = null

    companion object {
        const val PAGE_SIZE = 10
        const val FIRST_PAGE = 1
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Unsplash>) {
        Timber.d("Fetching Initial Page: $FIRST_PAGE")

        retryQuery = { loadInitial(params, callback) }

        networkState.postValue(NetworkState(NetworkState.State.LOADING))

        compositeDisposable.add(
            unsplashRepository.fetchPagedUnsplashData(FIRST_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ results ->
                    retryQuery = null
                    networkState.postValue(NetworkState(NetworkState.State.SUCCESS))
                    callback.onResult(results, null, FIRST_PAGE + 1)
                }, { processErrors(it) })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Unsplash>) {
        Timber.d("Fetching Next Page: ${params.key}")

        retryQuery = { loadAfter(params, callback) }

        networkState.postValue(NetworkState(NetworkState.State.LOADING))

        compositeDisposable.add(
            unsplashRepository.fetchPagedUnsplashData(params.key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(2, TimeUnit.SECONDS)
                .subscribe({ results ->
                    //val key = if (total_pages > params.key) params.key + 1 else total_pages
                    val key = FIRST_PAGE
                    retryQuery = null
                    networkState.postValue(NetworkState(NetworkState.State.SUCCESS))
                    callback.onResult(results, key)
                }, { processErrors(it) })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Unsplash>) {
        Timber.d("Fetching Previous Page: ${params.key}")

        retryQuery = { loadBefore(params, callback) }

        networkState.postValue(NetworkState(NetworkState.State.LOADING))

        compositeDisposable.add(
            unsplashRepository.fetchPagedUnsplashData(params.key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(2, TimeUnit.SECONDS)
                .subscribe({ results ->
                    //val key = if (params.key > 1) params.key - 1 else 0
                    val key = FIRST_PAGE
                    retryQuery = null
                    networkState.postValue(NetworkState(NetworkState.State.SUCCESS))
                    callback.onResult(results, key)
                }, { processErrors(it) })
        )
    }

    private fun processErrors(it: Throwable?) {
        networkState.postValue(
            NetworkState(
                ApiErrorHandler.getErrorByThrowable(it),
                NetworkState.State.ERROR
            )
        )
    }

    fun retry() {
        val prevQuery = retryQuery
        retryQuery = null
        prevQuery?.invoke()
    }

    fun clear() {
        compositeDisposable.clear()
    }

}