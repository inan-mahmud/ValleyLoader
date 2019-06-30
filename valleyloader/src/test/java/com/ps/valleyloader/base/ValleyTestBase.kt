package com.ps.valleyloader.base

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Buffer
import org.apache.commons.io.FileUtils
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.io.File
import java.io.IOException

/**
 * @Author: Prokash Sarkar
 * @Date: 6/28/19
 */

abstract class ValleyTestBase {

    lateinit var mockServer: MockWebServer

    @BeforeEach
    open fun setUp() {
        configureMockServer()
    }

    @AfterEach
    open fun tearDown() {
        stopMockServer()
    }

    abstract fun isMockServerEnabled(): Boolean

    open fun configureMockServer() {
        if (isMockServerEnabled()) {
            mockServer = MockWebServer()
            mockServer.start(9191)
        }
    }

    open fun stopMockServer() {
        if (isMockServerEnabled()) {
            mockServer.shutdown()
        }
    }

    open fun mockHttpResponseForNoContent(responseCode: Int) = mockServer.enqueue(
        MockResponse()
            .setResponseCode(responseCode)
    )

    open fun mockHttpResponseForJson(fileName: String, responseCode: Int) = mockServer.enqueue(
        MockResponse()
            .setResponseCode(responseCode)
            .setBody(getJson(fileName))
    )

    open fun mockHttpResponseForImage(fileName: String, responseCode: Int) = mockServer.enqueue(
        MockResponse()
            .addHeader("Content-Type:image/png")
            .setResponseCode(responseCode)
            .setBody(getBuffer(fileName))
    )

    private fun getJson(path: String): String {
        val file = getFile(path)
        return String(file.readBytes())
    }

    @Throws(IOException::class)
    fun getBuffer(path: String): Buffer {
        val file = getFile(path)
        val fileData = FileUtils.readFileToByteArray(file)
        val buffer = Buffer()
        buffer.write(fileData)
        return buffer
    }

    fun getFile(path: String): File {
        val resourcesDirectory = "src/test/resources/$path"
        return File(resourcesDirectory)
    }

}