package com.ps.valleyloader.utils

import android.app.ActivityManager
import android.content.Context
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.ps.valleyloader.base.ValleyTestBase
import com.ps.valleyloader.downloader.ValleyDownloader
import com.ps.valleyloader.listener.ValleyDownloaderListener
import com.ps.valleyloader.listener.ValleyLoaderListener
import org.apache.commons.io.FileUtils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.HttpURLConnection


/**
 * Created by Prokash Sarkar on 6/29/2019.
 * https://github.com/prokash-sarkar
 */

class ValleyLoaderTest : ValleyTestBase() {

    private val context: Context = mock()
    private lateinit var valleyLoader: ValleyLoader
    private val loaderListener: ValleyLoaderListener = mock()
    private val downloaderListener: ValleyDownloaderListener = mock()
    private lateinit var downloadRequest: DownloadRequest
    private lateinit var valleyDownloader: ValleyDownloader

    override fun isMockServerEnabled(): Boolean = true

    @BeforeEach
    fun init() {
        val activityManager: ActivityManager = mock()

        whenever(context.getSystemService(Context.ACTIVITY_SERVICE))
            .thenReturn(activityManager)

        whenever(activityManager.memoryClass)
            .thenReturn(500)

        valleyLoader = ValleyLoader.getInstance(context)
        downloadRequest = DownloadRequest(AppConfig.BASE_TEST_URL, loaderListener)
        valleyDownloader = ValleyDownloader(AppConfig.BASE_TEST_URL, downloaderListener, downloadRequest)
    }

    @Test
    fun downloadImage() {

    }

    @Test
    fun downloadByteArray() {
        // Prepare data
        mockHttpResponseForImage("mindvalley.jpg", HttpURLConnection.HTTP_OK)

        valleyLoader.download(AppConfig.BASE_TEST_URL, loaderListener)
        valleyLoader.test()

        //By having a Callback we are telling okHttp to invoke the request
        //and call the callback asynchronously
        //This means that our test will exit before anything happens without a lock
        Thread.sleep(1000)

        val file = getFile("mindvalley.jpg")
        val byteArray: ByteArray = FileUtils.readFileToByteArray(file)

        verify(downloaderListener).onDownloadSuccess(valleyDownloader, byteArray, downloadRequest)
    }
}