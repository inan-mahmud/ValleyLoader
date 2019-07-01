package com.ps.androidx.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ps.androidx.base.BaseViewModel
import com.ps.androidx.data.Resource
import com.ps.androidx.data.model.unsplash.Unsplash
import com.ps.androidx.network.NetworkState
import com.ps.androidx.paging.UnsplashDataSource
import com.ps.androidx.paging.UnsplashDataSourceFactory
import com.ps.androidx.repository.UnsplashRepository
import javax.inject.Inject

/**
 * Created by Prokash Sarkar on 6/25/2019.
 * https://github.com/prokash-sarkar
 */

class MainViewModel @Inject
constructor(
    private val unsplashRepository: UnsplashRepository,
    private val unsplashDataSourceFactory: UnsplashDataSourceFactory
) : BaseViewModel() {

    private var unsplashLiveData: MediatorLiveData<Resource<List<Unsplash>>> = MediatorLiveData()

    private var unsplashPagedList: LiveData<PagedList<Unsplash>>

    init {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(UnsplashDataSource.PAGE_SIZE)
            .build()

        unsplashPagedList = LivePagedListBuilder(unsplashDataSourceFactory, config)
            .build()
    }

    fun observePagedUnplashData(): LiveData<PagedList<Unsplash>> {
        return unsplashPagedList
    }

    fun observeUnsplashData(): LiveData<Resource<List<Unsplash>>> {
        return unsplashLiveData
    }

    fun observeNetworkState(): LiveData<NetworkState> {
        return unsplashDataSourceFactory.getNetworkState()
    }

    fun fetchUnsplashData(page: Int) {

        unsplashLiveData.value = Resource.loading(null)

        val source = unsplashRepository.fetchUnsplashData(page)
        unsplashLiveData.addSource(source) {
            unsplashLiveData.value = it
            unsplashLiveData.removeSource(source)
        }
    }

    fun retryFetchingUnsplashData() {
        unsplashDataSourceFactory.getUnsplashDataSource().retry()
    }

    override fun onCleared() {
        super.onCleared()
        unsplashDataSourceFactory.getUnsplashDataSource().clear()
    }
}