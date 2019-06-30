package com.ps.androidx.di.module

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module


/**
 * Created by Prokash Sarkar on 5/23/2019.
 * https://github.com/prokash-sarkar
 */

@Module
abstract class ContextModule {

    @Binds
    internal abstract fun provideContext(application: Application): Context
}