package com.ps.androidx.data.model.unsplash

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.annotations.SerializedName
import com.ps.androidx.R
import com.ps.valleyloader.utils.ValleyLoader


/**
 * Created by Prokash Sarkar on 6/27/2019.
 * https://github.com/prokash-sarkar
 */

data class Unsplash(

        @field:SerializedName("id")
        val id: String,

        @field:SerializedName("created_at")
        val createdAt: String,

        @field:SerializedName("width")
        val width: Int,

        @field:SerializedName("height")
        val height: Int,

        @field:SerializedName("color")
        val color: String,

        @field:SerializedName("likes")
        val likes: Int,

        @field:SerializedName("liked_by_user")
        val likedByUser: Boolean,

        @field:SerializedName("user")
        val user: User,

        @field:SerializedName("current_user_collections")
        val currentUserCollections: List<Any>,

        @field:SerializedName("urls")
        val urls: Urls,

        @field:SerializedName("categories")
        val categories: List<Category>,

        @field:SerializedName("links")
        val links: Links__

)

@BindingAdapter("avatarUrl")
fun ImageView.setAvatarImageUrl(url: String) {
    val options = RequestOptions()
            .placeholder(R.drawable.shape_primary_solid)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.shape_primary_solid)
            .apply(RequestOptions.circleCropTransform())

    Glide.with(this).load(url)
            .apply(options)
            .into(this)
}

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(url: String) {
    ValleyLoader.getInstance(context)
            .download(url, this)
}