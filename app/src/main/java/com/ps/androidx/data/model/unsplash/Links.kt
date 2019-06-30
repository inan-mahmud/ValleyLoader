package com.ps.androidx.data.model.unsplash

/**
 * Created by Prokash Sarkar on 6/27/2019.
 * https://github.com/prokash-sarkar
 */

import com.google.gson.annotations.SerializedName

data class Links(

    @field:SerializedName("self")
    var self: String,

    @field:SerializedName("html")
    var html: String,

    @field:SerializedName("photos")
    var photos: String,

    @field:SerializedName("likes")
    var likes: String

)