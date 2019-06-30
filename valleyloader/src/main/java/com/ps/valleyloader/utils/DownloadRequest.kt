package com.ps.valleyloader.utils

import com.ps.valleyloader.listener.ValleyLoaderListener

/**
 * @Author: Prokash Sarkar
 * @Date: 6/26/19
 */

/**
 * Acts as a wrapper class
 */
data class DownloadRequest(val url: String, val listener: ValleyLoaderListener)