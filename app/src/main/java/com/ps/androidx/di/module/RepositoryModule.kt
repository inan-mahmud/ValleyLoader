package com.ps.androidx.di.module

import com.ps.androidx.network.ApiService
import com.ps.androidx.repository.UnsplashRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @Author: Prokash Sarkar
 * @Date: 6/10/19
 */

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepository(apiService: ApiService) = UnsplashRepository(apiService)

}