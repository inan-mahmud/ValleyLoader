package com.ps.androidx.base

import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.ps.androidx.di.DaggerTestAndroidAppComponent
import com.ps.androidx.di.TestAndroidAppComponent
import com.ps.androidx.ui.main.MainActivity
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import java.io.InputStream

/**
 * @Author: Prokash Sarkar
 * @Date: 6/9/19
 */

abstract class TestAndroidBase {

    private lateinit var baseApplication: BaseApplication
    private lateinit var testAppComponent: TestAndroidAppComponent
    private lateinit var mockServer: MockWebServer

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(MainActivity::class.java, true, false)

    @Rule
    @JvmField
    var executorRule = CountingTaskExecutorRule()

    @Before
    open fun setUp() {
        configureMockServer()
        configureDi()

        activityRule.launchActivity(null)
        /*val intent = Intent(InstrumentationRegistry.getInstrumentation()
            .targetContext, MainActivity::class.java)
        activityRule.launchActivity(intent)*/
    }

    @After
    open fun tearDown() {
        stopMockServer()
    }

    open fun configureDi() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        baseApplication = instrumentation.targetContext.applicationContext as BaseApplication

        testAppComponent = DaggerTestAndroidAppComponent.builder()
            .application(baseApplication)
            .build()

        testAppComponent.inject(baseApplication)
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
        val inputStream: InputStream = InstrumentationRegistry.getInstrumentation().context.resources.assets.open(path)
        return inputStream.bufferedReader().use { it.readText() }
    }

}