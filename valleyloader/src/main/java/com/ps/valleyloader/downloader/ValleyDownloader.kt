package com.ps.valleyloader.downloader

import com.ps.valleyloader.listener.ValleyDownloaderListener
import com.ps.valleyloader.utils.AppConfig
import com.ps.valleyloader.utils.DownloadRequest
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * @Author: Prokash Sarkar
 * @Date: 6/26/19
 */

class ValleyDownloader(val url: String, val listener: ValleyDownloaderListener, val downloadRequest: DownloadRequest) {

    private var call: Call? = null

    /**
     * Starts downloading a file and notifies via listeners
     */
    fun start() {
        val mRequest = Request.Builder()
            .url(url)
            .get()
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(AppConfig.HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(AppConfig.HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(AppConfig.HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
            //.addInterceptor(getHttpLoggingInterceptor())
            .build()

        call = client.newCall(mRequest)
        call?.enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                var byteArray: ByteArray? = response.body()?.bytes()

                if (call.isCanceled) {
                    byteArray = null
                }

                if (byteArray != null && byteArray.isNotEmpty()) {
                    listener.onDownloadSuccess(this@ValleyDownloader, byteArray, downloadRequest)
                } else {
                    listener.onDownloadFailure(this@ValleyDownloader, downloadRequest)
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                listener.onDownloadFailure(this@ValleyDownloader, downloadRequest)
            }
        })

    }

    private fun getHttpLoggingInterceptor() =
        HttpLoggingInterceptor { message -> println(message) }
            .setLevel(HttpLoggingInterceptor.Level.BODY)

    /**
     * Cancels the current ongoing call
     */
    fun cancel() {
        if (call != null) {
            call?.cancel()
        }
    }

}