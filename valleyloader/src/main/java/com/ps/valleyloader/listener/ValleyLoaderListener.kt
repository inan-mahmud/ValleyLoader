package com.ps.valleyloader.listener

import com.ps.valleyloader.utils.ValleyLoader

/**
 * @Author: Prokash Sarkar
 * @Date: 6/26/19
 */

interface ValleyLoaderListener {
    fun onByteSuccess(loader: ValleyLoader, byteArray: ByteArray, url: String)

    fun onByteFailure(loader: ValleyLoader, url: String)
}