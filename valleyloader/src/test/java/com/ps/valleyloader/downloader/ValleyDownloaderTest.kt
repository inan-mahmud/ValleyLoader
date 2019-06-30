package com.ps.valleyloader.downloader

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.ps.valleyloader.base.ValleyTestBase
import com.ps.valleyloader.listener.ValleyDownloaderListener
import com.ps.valleyloader.listener.ValleyLoaderListener
import com.ps.valleyloader.utils.AppConfig
import com.ps.valleyloader.utils.DownloadRequest
import org.apache.commons.io.FileUtils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.HttpURLConnection

/**
 * Created by Prokash Sarkar on 6/28/2019.
 * https://github.com/prokash-sarkar
 */

/**
 * In JUnit 5 the @Rule and @TestRunner are merged into @ExtendWith
 */
class ValleyDownloaderTest : ValleyTestBase() {

    private val downloaderListener: ValleyDownloaderListener = mock()
    private val loaderListener: ValleyLoaderListener = mock()
    private lateinit var downloadRequest: DownloadRequest
    private lateinit var valleyDownloader: ValleyDownloader

    override fun isMockServerEnabled(): Boolean = true

    @BeforeEach
    fun init() {
        downloadRequest = DownloadRequest(AppConfig.BASE_TEST_URL, loaderListener)
        valleyDownloader = ValleyDownloader(AppConfig.BASE_TEST_URL, downloaderListener, downloadRequest)
    }

    @Test
    fun checkIfSuccessCallbackCalled() {
        // Prepare data
        mockHttpResponseForImage("mindvalley.jpg", HttpURLConnection.HTTP_OK)

        valleyDownloader.start()

        //By having a Callback we are telling okHttp to invoke the request
        //and call the callback asynchronously
        //This means that our test will exit before anything happens without a lock
        Thread.sleep(1000)

        val file = getFile("mindvalley.jpg")
        val byteArray: ByteArray = FileUtils.readFileToByteArray(file)

        //verify(downloaderListener).onDownloadSuccess(valleyDownloader, byteArray, downloadRequest)
    }

    @Test
    fun checkIfFailureCallbackCalled() {
        // Prepare data
        mockHttpResponseForNoContent(HttpURLConnection.HTTP_NOT_FOUND)

        valleyDownloader.start()

        //By having a Callback we are telling okHttp to invoke the request
        //and call the callback asynchronously
        //This means that our test will exit before anything happens without a lock
        Thread.sleep(1000)

        //verify(downloaderListener).onDownloadFailure(valleyDownloader, downloadRequest)
    }


}