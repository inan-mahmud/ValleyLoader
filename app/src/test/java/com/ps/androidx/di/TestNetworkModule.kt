package com.ps.androidx.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ps.androidx.network.ApiService
import com.ps.androidx.util.AppConfig
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 * Created by Prokash Sarkar on 6/11/2019.
 * https://github.com/prokash-sarkar
 */

@Module
class TestNetworkModule {

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(AppConfig.HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(AppConfig.HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(AppConfig.HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
        .build()

    @Provides
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(AppConfig.BASE_TEST_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

}