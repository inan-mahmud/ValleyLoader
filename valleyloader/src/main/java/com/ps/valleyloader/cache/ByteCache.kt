package com.ps.valleyloader.cache

import android.app.ActivityManager
import android.content.Context
import androidx.collection.LruCache
import com.ps.valleyloader.listener.ByteCacheListener
import com.ps.valleyloader.utils.DownloadRequest
import com.ps.valleyloader.utils.SingletonHolder

/**
 * @Author: Prokash Sarkar
 * @Date: 6/26/19
 */

open class ByteCache private constructor(context: Context) {

    private val mMemoryCache: LruCache<String, ByteArray>
    private var mCacheSize: Int = 0

    val cacheSize: Int
        get() {
            return mCacheSize
        }

    companion object : SingletonHolder<ByteCache, Context>(::ByteCache)

    init {
        // Get memory class of this device
        val memClass = (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).memoryClass

        // Use 1/8th of the available memory for this memory cache
        mCacheSize = 1024 * 1024 * memClass / 8

        mMemoryCache = object : LruCache<String, ByteArray>(mCacheSize) {
            override fun sizeOf(key: String, byteArray: ByteArray): Int {
                // The cache size will be measured in array size
                return byteArray.size
            }
        }
    }

    /**
     *
     * @param cacheKey as key
     * @param listener to notify the data availability
     * @param downloadRequest to pass the requested listener
     */
    fun queryCache(cacheKey: String?, listener: ByteCacheListener, downloadRequest: DownloadRequest) {
        if (cacheKey == null) {
            listener.onByteNotFound(this, cacheKey, downloadRequest)
            return
        }

        val cachedByte = mMemoryCache.get(cacheKey)
        if (cachedByte != null) {
            listener.onByteFound(this, cachedByte, cacheKey, downloadRequest)
            return
        }

        listener.onByteNotFound(this, cacheKey, downloadRequest)
    }

    /**
     *
     * Stores a new input to the cache
     *
     * @param byteArray as value
     * @param cacheKey as key
     */
    fun storeToMemory(byteArray: ByteArray, cacheKey: String) {
        mMemoryCache.put(cacheKey, byteArray)
    }

    /**
     *
     * Sets a size to limit the cache size
     *
     * @param cacheSize for the memory
     */
    fun setCacheSize(cacheSize: Int) {
        this.mCacheSize = cacheSize
    }

    /**
     * Clears out all memory cache
     */
    fun clear() {
        mMemoryCache.evictAll()
    }

}