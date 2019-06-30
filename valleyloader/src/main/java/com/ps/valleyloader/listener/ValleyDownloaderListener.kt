package com.ps.valleyloader.listener

import com.ps.valleyloader.downloader.ValleyDownloader
import com.ps.valleyloader.utils.DownloadRequest

/**
 * @Author: Prokash Sarkar
 * @Date: 6/26/19
 */

interface ValleyDownloaderListener {
    fun onDownloadSuccess(downloader: ValleyDownloader, byteArray: ByteArray, downloadRequest: DownloadRequest)

    fun onDownloadFailure(downloader: ValleyDownloader, downloadRequest: DownloadRequest)
}