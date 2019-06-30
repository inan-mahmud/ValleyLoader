package com.ps.androidx.data.model.unsplash

import com.google.gson.annotations.SerializedName


/**
 * Created by Prokash Sarkar on 6/27/2019.
 * https://github.com/prokash-sarkar
 */

data class Category(

    @field:SerializedName("id")
    var id: Int,

    @field:SerializedName("title")
    var title: String,

    @field:SerializedName("photo_count")
    var photoCount: Int,

    @field:SerializedName("links")
    var links: Links_

)