package com.ps.androidx.data.model.unsplash

import com.google.gson.annotations.SerializedName


/**
 * Created by Prokash Sarkar on 6/27/2019.
 * https://github.com/prokash-sarkar
 */

data class ProfileImage(

    @field:SerializedName("small")
    var small: String,

    @field:SerializedName("medium")
    var medium: String,

    @field:SerializedName("large")
    var large: String

)