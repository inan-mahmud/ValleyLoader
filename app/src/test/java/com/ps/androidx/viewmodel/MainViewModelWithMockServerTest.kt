package com.ps.androidx.viewmodel

import com.ps.androidx.base.TestBase
import com.ps.androidx.data.Resource
import com.ps.androidx.paging.UnsplashDataSource
import com.ps.androidx.paging.UnsplashDataSourceFactory
import com.ps.androidx.repository.UnsplashRepository
import com.ps.androidx.ui.main.MainViewModel
import com.ps.androidx.util.InstantExecutorExtension
import com.ps.androidx.util.LiveDataTestUtil
import com.ps.androidx.util.RxSchedulerExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.net.HttpURLConnection


/**
 * Created by Prokash Sarkar on 6/30/2019.
 * https://github.com/prokash-sarkar
 */

/**
 * In JUnit 5 the @Rule and @TestRunner are merged into @ExtendWith
 */
@ExtendWith(InstantExecutorExtension::class, RxSchedulerExtension::class)
class MainViewModelWithMockServerTest : TestBase() {

    private lateinit var unsplashRepository: UnsplashRepository
    private lateinit var unsplashDataSource: UnsplashDataSource
    private lateinit var unsplashDataSourceFactory: UnsplashDataSourceFactory
    private lateinit var viewModel: MainViewModel

    override fun isMockServerEnabled(): Boolean = true

    @BeforeEach
    fun init() {
        unsplashRepository = UnsplashRepository(apiService)
        unsplashDataSource = UnsplashDataSource(unsplashRepository)
        unsplashDataSourceFactory = UnsplashDataSourceFactory(unsplashDataSource)
        viewModel = MainViewModel(unsplashRepository, unsplashDataSourceFactory)
    }

    @Test
    fun fetchUnsplashData_whenSuccess() {
        // Prepare data
        mockHttpResponse("fetchUnsplashData_whenSuccess.json", HttpURLConnection.HTTP_OK)

        // Pre-test
        assertEquals(
            null, this.viewModel.observeUnsplashData().value,
            "Result should be null because the stream not started yet"
        )

        // Execute View Model
        this.viewModel.fetchUnsplashData(1)

        // Checks
        //val expectedContentTest = LiveDataTestUtil.getValue(viewModel.observeUnsplashData())
        val expectedContent = LiveDataTestUtil.getValue(unsplashRepository.fetchUnsplashData(1))

        assertEquals(Resource.Status.SUCCESS, expectedContent?.status, "Status not matched")
        assertNull(expectedContent?.message, "Message should be null")
        assertNotNull(expectedContent?.data, "Data should not be null")
        assertEquals(10, expectedContent?.data?.size, "List size not matched")
        assertEquals("4kQA1aQK8-Y", expectedContent?.data?.get(0)?.id, "User Id not matched")
        assertNotNull(expectedContent?.data?.get(0)?.user, "User object must not be null")
        assertNotNull(expectedContent?.data?.get(0)?.urls, "Urls object must not be null")
        assertNotNull(expectedContent?.data?.get(0)?.categories, "Categories object must not be null")
        assertNotNull(expectedContent?.data?.get(0)?.links, "Links object must not be null")
    }

    @Test
    fun fetchUnsplashData_whenError() {
        // Prepare data
        mockHttpResponse("fetchUnsplashData_whenError.json", HttpURLConnection.HTTP_UNAUTHORIZED)

        // Pre-test
        assertEquals(
            null, this.viewModel.observeUnsplashData().value,
            "Result should be null because the stream not started yet"
        )

        // Execute View Model
        this.viewModel.fetchUnsplashData(1)

        // Checks
        //val expectedContentTest = LiveDataTestUtil.getValue(viewModel.observeUnsplashData())
        val expectedContent = LiveDataTestUtil.getValue(unsplashRepository.fetchUnsplashData(1))

        assertEquals(Resource.Status.ERROR, expectedContent?.status, "Status not matched")
        assertEquals(
            "Invalid API key: You must be granted a valid key.",
            expectedContent?.message,
            "Message should not be null"
        )
        assertNull(expectedContent?.data, "Data should be null")
    }

}