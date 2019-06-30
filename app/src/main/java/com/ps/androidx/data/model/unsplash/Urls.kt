package com.ps.androidx.data.model.unsplash

import com.google.gson.annotations.SerializedName


/**
 * Created by Prokash Sarkar on 6/27/2019.
 * https://github.com/prokash-sarkar
 */

class Urls(

    @field:SerializedName("raw")
    var raw: String,

    @field:SerializedName("full")
    var full: String,

    @field:SerializedName("regular")
    var regular: String,

    @field:SerializedName("small")
    var small: String,

    @field:SerializedName("thumb")
    var thumb: String

)