package com.ps.valleyloader.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import com.ps.valleyloader.cache.ByteCache
import com.ps.valleyloader.downloader.ValleyDownloader
import com.ps.valleyloader.listener.ByteCacheListener
import com.ps.valleyloader.listener.ValleyDownloaderListener
import com.ps.valleyloader.listener.ValleyLoaderListener
import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


/**
 * @Author: Prokash Sarkar
 * @Date: 6/26/19
 */

class ValleyLoader private constructor(private val mContext: Context) : ByteCacheListener, ValleyDownloaderListener {

    private val mFailedUrls = ArrayList<String>()
    private val mCacheListeners = ArrayList<ValleyLoaderListener>()
    private val mCacheUrls = ArrayList<String>()
    private val mDownloadersMap = HashMap<String, ValleyDownloader>()
    private val mDownloadRequests = ArrayList<DownloadRequest>()
    private val mDownloadListeners = ArrayList<ValleyLoaderListener>()
    private val mDownloaders = ArrayList<ValleyDownloader>()

    private val LISTENER_NOT_FOUND = -1

    companion object : SingletonHolder<ValleyLoader, Context>(::ValleyLoader)

    fun download(url: String?, listener: ValleyLoaderListener?) {
        if (url == null || listener == null || mFailedUrls.contains(url)) {
            return
        }

        mCacheListeners.add(listener)
        mCacheUrls.add(url)
        // Check for any cached data
        // Returns ByteCacheListener.onByteFound
        // or Returns ByteCacheListener.onByteNotFound
        ByteCache.getInstance(mContext)
            .queryCache(
                getCacheKey(url), this,
                DownloadRequest(url, listener)
            )
    }

    fun download(url: String, imageView: ImageView) {
        cancel(imageView)
        download(url, ImageManagerListener(imageView))
    }

    fun getCacheKey(url: String): String? {
        try {
            val md = MessageDigest.getInstance("SHA-1")
            md.update(url.toByteArray(charset("UTF-8")), 0, url.length)
            return String.format("%x", BigInteger(md.digest()))
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return null
    }

    private fun getListenerIndex(listener: ValleyLoaderListener, url: String): Int {
        for (index in mCacheListeners.indices) {
            if (mCacheListeners[index] === listener && mCacheUrls[index] == url) {
                return index
            }
        }
        return LISTENER_NOT_FOUND
    }

    override fun onByteFound(
        byteCache: ByteCache,
        byteArray: ByteArray,
        key: String,
        downloadRequest: DownloadRequest
    ) {
        val url = downloadRequest.url
        val listener = downloadRequest.listener

        val idx = getListenerIndex(listener, url)
        if (idx == LISTENER_NOT_FOUND) {
            // Request has since been canceled
            return
        }

        listener.onByteSuccess(this, byteArray, url)

        mCacheListeners.removeAt(idx)
        mCacheUrls.removeAt(idx)
    }

    override fun onByteNotFound(byteCache: ByteCache, key: String?, downloadRequest: DownloadRequest) {
        val url = downloadRequest.url
        val listener = downloadRequest.listener

        val idx = getListenerIndex(listener, url)
        if (idx == LISTENER_NOT_FOUND) {
            // Request has since been canceled
            return
        }
        mCacheListeners.removeAt(idx)
        mCacheUrls.removeAt(idx)

        // Share the same downloader for identical URLs so we don't download the
        // same URL several times
        var downloader = mDownloadersMap[url]
        if (downloader == null) {
            downloader = ValleyDownloader(url, this, downloadRequest)
            downloader.start()
            mDownloadersMap[url] = downloader
        }
        mDownloadRequests.add(downloadRequest)
        mDownloadListeners.add(listener)
        mDownloaders.add(downloader)
    }

    fun test(){

    }

    override fun onDownloadSuccess(
        downloader: ValleyDownloader,
        byteArray: ByteArray,
        downloadRequest: DownloadRequest
    ) {
        InputStreamHandlingTask(downloader, downloadRequest).execute(byteArray)
    }

    override fun onDownloadFailure(downloader: ValleyDownloader, downloadRequest: DownloadRequest) {
        for (idx in mDownloaders.indices.reversed()) {
            val aDownloader = mDownloaders[idx]
            if (aDownloader === downloader) {
                val listener = mDownloadListeners[idx]
                listener.onByteFailure(this, downloadRequest.url)
                mDownloaders.removeAt(idx)
                mDownloadListeners.removeAt(idx)
            }
        }
        mDownloadersMap.remove(downloadRequest.url)
    }

    private inner class InputStreamHandlingTask constructor(
        internal var mDownloader: ValleyDownloader,
        internal var mDownloadRequest: DownloadRequest
    ) : AsyncTask<ByteArray, Void, ByteArray>() {

        override fun doInBackground(vararg params: ByteArray): ByteArray? {
            val response = params[0]

            // Store the byteArray in the cache
            val sharedByteCache = ByteCache.getInstance(mContext)
            val cacheKey = getCacheKey(mDownloadRequest.url)
            sharedByteCache.storeToMemory(response, cacheKey!!)

            return response
        }

        override fun onPostExecute(byteArray: ByteArray?) {
            // Notify all the downloadListener with this downloader
            for (idx in mDownloaders.indices.reversed()) {
                val aDownloader = mDownloaders[idx]
                if (aDownloader === mDownloader) {
                    val listener = mDownloadListeners[idx]
                    if (byteArray != null) {
                        listener.onByteSuccess(this@ValleyLoader, byteArray, mDownloadRequest.url)
                    } else {
                        listener.onByteFailure(this@ValleyLoader, mDownloadRequest.url)
                    }
                    mDownloaders.removeAt(idx)
                    mDownloadListeners.removeAt(idx)
                }
            }
            if (byteArray == null) {
                mFailedUrls.add(mDownloadRequest.url)
            }
            mDownloadersMap.remove(mDownloadRequest.url)
        }

    }

    fun cancel(listener: ValleyLoaderListener) {
        val idx: Int = mCacheListeners.indexOf(listener)
        while (idx != -1) {
            mCacheListeners.removeAt(idx)
            mCacheUrls.removeAt(idx)
        }

        while (idx != -1) {
            val downloader = mDownloaders[idx]

            mDownloadRequests.removeAt(idx)
            mDownloadListeners.removeAt(idx)
            mDownloaders.removeAt(idx)

            if (!mDownloaders.contains(downloader)) {
                // No more listeners are waiting for this download, cancel it
                downloader.cancel()
                mDownloadersMap.remove(downloader.url)
            }
        }
    }

    fun cancel(imageView: ImageView) {
        val queue = LinkedList<ValleyLoaderListener>()
        for (listener in mCacheListeners) {
            if (listener is ImageManagerListener && listener.mImageView == imageView) {
                queue.add(listener)
            }
        }
        for (listener in mDownloadListeners) {
            if (listener is ImageManagerListener && listener.mImageView == imageView) {
                queue.add(listener)
            }
        }
        for (listener in queue) {
            cancel(listener)
        }
    }

    private class ImageManagerListener(val mImageView: ImageView) : ValleyLoaderListener {

        override fun onByteSuccess(loader: ValleyLoader, byteArray: ByteArray, url: String) {
            try {
                val bitmap = BitmapFactory.decodeStream(byteArray.inputStream())
                mImageView.setImageBitmap(bitmap)
            } catch (e: Exception) {
                //Log.e("onByteSuccess", "Failed to decode Bitmap stream for $url")
            }
        }

        override fun onByteFailure(loader: ValleyLoader, url: String) {
            //Log.e("onByteFailure", "Failed to download $url")
        }
    }

}
