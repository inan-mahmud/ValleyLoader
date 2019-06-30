package com.ps.androidx.di.module

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ps.androidx.data.storage.LocalStorage
import com.ps.androidx.network.APIServiceHolder
import com.ps.androidx.network.ApiService
import com.ps.androidx.network.authenticator.TokenAuthenticator
import com.ps.androidx.network.interceptor.ApiKeyInterceptor
import com.ps.androidx.network.interceptor.NetworkConnectionInterceptor
import com.ps.androidx.network.interceptor.TokenAuthenticatorInterceptor
import com.ps.androidx.util.AppConfig
import com.ps.androidx.util.gson.AnnotationExclusionStrategy
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 * Created by Prokash Sarkar on 5/22/2019.
 * https://github.com/prokash-sarkar
 */

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .setExclusionStrategies(AnnotationExclusionStrategy())
        .create()

    @Singleton
    @Provides
    fun provideCache(context: Context) = Cache(
        File(context.cacheDir, AppConfig.HTTP_CACHE_DIR), AppConfig.HTTP_CACHE_SIZE
    )

    @Singleton
    @Provides
    fun provideOkHttpClient(
        context: Context,
        loggingInterceptor: HttpLoggingInterceptor,
        //localStorage: LocalStorage,
        //tokenAuthenticator: TokenAuthenticator,
        cache: Cache
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(NetworkConnectionInterceptor(context))
            .addInterceptor(loggingInterceptor)
            //.addInterceptor(TokenAuthenticatorInterceptor(localStorage))
            //.addInterceptor(ApiKeyInterceptor())
            //.authenticator(tokenAuthenticator)
            .connectTimeout(AppConfig.HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(AppConfig.HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(AppConfig.HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
            .cache(cache)
            .build()


    @Singleton
    @Provides
    fun provideOkHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor { message -> Timber.d(message) }
        return logging.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(AppConfig.BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Singleton
    @Provides
    fun provideApiServiceHolder() = APIServiceHolder()

    @Singleton
    @Provides
    fun provideApiService(apiServiceHolder: APIServiceHolder, retrofit: Retrofit): ApiService {
        val apiService = retrofit.create(ApiService::class.java)
        apiServiceHolder.apiService = apiService
        return apiService
    }

}