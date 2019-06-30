package com.ps.valleyloader.listener

import com.ps.valleyloader.cache.ByteCache
import com.ps.valleyloader.utils.DownloadRequest

/**
 * @Author: Prokash Sarkar
 * @Date: 6/26/19
 */

interface ByteCacheListener {
    fun onByteFound(byteCache: ByteCache, byteArray: ByteArray, key: String, downloadRequest: DownloadRequest)

    fun onByteNotFound(byteCache: ByteCache, key: String?, downloadRequest: DownloadRequest)
}
