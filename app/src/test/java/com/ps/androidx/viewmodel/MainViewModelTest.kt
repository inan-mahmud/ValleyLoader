package com.ps.androidx.viewmodel

import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.ps.androidx.data.Resource
import com.ps.androidx.data.model.unsplash.Unsplash
import com.ps.androidx.paging.UnsplashDataSourceFactory
import com.ps.androidx.repository.UnsplashRepository
import com.ps.androidx.ui.main.MainViewModel
import com.ps.androidx.util.InstantExecutorExtension
import com.ps.androidx.util.LiveDataTestUtil.Companion.getValue
import com.ps.androidx.util.RxSchedulerExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


/**
 * Created by Prokash Sarkar on 6/28/2019.
 * https://github.com/prokash-sarkar
 */

/**
 * In JUnit 5 the @Rule and @TestRunner are merged into @ExtendWith
 */
@ExtendWith(InstantExecutorExtension::class, RxSchedulerExtension::class)
class MainViewModelTest {

    private val unsplashRepository: UnsplashRepository = mock()
    private val unsplashDataSourceFactory: UnsplashDataSourceFactory = mock()
    private lateinit var viewModel: MainViewModel

    @BeforeEach
    fun init() {
        viewModel = MainViewModel(unsplashRepository, unsplashDataSourceFactory)
    }

    @Test
    fun checkIfListLoaded() {
        val mockResponse: List<Unsplash> = mock()

        val liveData =
            MutableLiveData<Resource<List<Unsplash>>>()
                .apply { value = Resource.success(mockResponse) }

        whenever(unsplashRepository.fetchUnsplashData(1)).thenReturn(liveData)

        viewModel.fetchUnsplashData(1)

        assertEquals(
            Resource.success(mockResponse),
            getValue(viewModel.observeUnsplashData())
        )
    }

}