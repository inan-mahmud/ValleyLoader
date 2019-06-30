package com.ps.androidx.base

import com.ps.androidx.di.DaggerTestAppComponent
import com.ps.androidx.di.TestAppComponent
import com.ps.androidx.network.ApiService
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.io.File
import javax.inject.Inject

/**
 * @Author: Prokash Sarkar
 * @Date: 6/9/19
 */

abstract class TestBase {

    lateinit var testAppComponent: TestAppComponent
    lateinit var mockServer: MockWebServer

    @Inject
    lateinit var apiService: ApiService

    @BeforeEach
    open fun setUp() {
        configureMockServer()
        configureDi()
    }

    @AfterEach
    open fun tearDown() {
        stopMockServer()
    }

    open fun configureDi() {
        testAppComponent = DaggerTestAppComponent.builder()
            .build()
        testAppComponent.inject(this)
    }

    abstract fun isMockServerEnabled(): Boolean

    open fun configureMockServer() {
        if (isMockServerEnabled()) {
            mockServer = MockWebServer()
            mockServer.start(9090)
        }
    }

    open fun stopMockServer() {
        if (isMockServerEnabled()) {
            mockServer.shutdown()
        }
    }

    open fun mockHttpResponse(fileName: String, responseCode: Int) = mockServer.enqueue(
        MockResponse()
            .setResponseCode(responseCode)
            .setBody(getJson(fileName))
    )

    private fun getJson(path: String): String {
        val resourcesDirectory = "src/test/resources/$path"
        val file = File(resourcesDirectory)
        return String(file.readBytes())
    }

}