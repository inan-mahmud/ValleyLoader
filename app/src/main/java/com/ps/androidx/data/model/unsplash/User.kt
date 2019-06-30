package com.ps.androidx.data.model.unsplash

import com.google.gson.annotations.SerializedName


/**
 * Created by Prokash Sarkar on 6/27/2019.
 * https://github.com/prokash-sarkar
 */

class User(

    @field:SerializedName("id")
    var id: String,

    @field:SerializedName("username")
    var username: String,

    @field:SerializedName("name")
    var name: String,

    @field:SerializedName("profile_image")
    var profileImage: ProfileImage,

    @field:SerializedName("links")
    var links: Links

)