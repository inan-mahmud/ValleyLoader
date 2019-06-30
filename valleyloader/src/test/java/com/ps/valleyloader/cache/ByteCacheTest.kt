package com.ps.valleyloader.cache

import android.app.ActivityManager
import android.content.Context
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.ps.valleyloader.listener.ByteCacheListener
import com.ps.valleyloader.listener.ValleyLoaderListener
import com.ps.valleyloader.utils.AppConfig
import com.ps.valleyloader.utils.DownloadRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


/**
 * Created by Prokash Sarkar on 6/29/2019.
 * https://github.com/prokash-sarkar
 */
class ByteCacheTest {

    private val context: Context = mock()
    private lateinit var byteCache: ByteCache
    private val loaderListener: ValleyLoaderListener = mock()
    private val byteCacheListener: ByteCacheListener = mock()
    private lateinit var downloadRequest: DownloadRequest

    @BeforeEach
    fun init() {

        val activityManager: ActivityManager = mock()

        whenever(context.getSystemService(Context.ACTIVITY_SERVICE))
            .thenReturn(activityManager)

        whenever(activityManager.memoryClass)
            .thenReturn(500)

        byteCache = ByteCache.getInstance(context)

        downloadRequest = DownloadRequest(AppConfig.BASE_TEST_URL, loaderListener)
    }

    @Test
    fun queryCache() {
        val cachedByte = ByteArray(1)
        val key = "test"

        byteCache.storeToMemory(cachedByte, key)

        byteCache.queryCache(key, byteCacheListener, downloadRequest)

        verify(byteCacheListener).onByteFound(byteCache, cachedByte, key, downloadRequest)
    }

    @Test
    fun storeToMemory() {
        val cachedByte = ByteArray(1)
        val key = "test"

        byteCache.storeToMemory(cachedByte, key)

        byteCache.queryCache(key, byteCacheListener, downloadRequest)

        verify(byteCacheListener).onByteFound(byteCache, cachedByte, key, downloadRequest)
    }

    @Test
    fun setCacheSize() {
        byteCache.setCacheSize(500)
        assertEquals(byteCache.cacheSize, 500)
    }

    @Test
    fun clear() {
        val cachedByte = ByteArray(1)
        val key = "test"

        byteCache.storeToMemory(cachedByte, key)

        byteCache.clear()

        byteCache.queryCache(key, byteCacheListener, downloadRequest)

        verify(byteCacheListener).onByteNotFound(byteCache, key, downloadRequest)
    }

}
