package com.ps.androidx.network

import com.ps.androidx.data.model.RefreshTokenResponse
import com.ps.androidx.data.model.unsplash.Unsplash
import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


/**
 * Created by Prokash Sarkar on 5/22/2019.
 * https://github.com/prokash-sarkar
 */

interface ApiService {

    @POST("auth/token")
    @FormUrlEncoded
    fun refreshToken(
        @Field("userId") userId: String?,
        @Field("refreshToken") refreshToken: String?
    ): Call<RefreshTokenResponse>

    /*@GET("wgkJgazE")
    fun fetchUnsplashData(@Query("page") page: Int): Flowable<List<Unsplash>>*/

    @GET("wgkJgazE")
    //@GET("WNVCbBLE")
    fun fetchUnsplashData(): Flowable<List<Unsplash>>

}