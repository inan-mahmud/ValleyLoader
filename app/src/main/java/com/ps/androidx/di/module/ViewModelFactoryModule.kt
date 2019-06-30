package com.ps.androidx.di.module

import androidx.lifecycle.ViewModelProvider
import com.ps.androidx.di.ViewModelFactory
import dagger.Binds
import dagger.Module

/**
 * Created by Prokash Sarkar on 5/22/2019.
 * https://github.com/prokash-sarkar
 */

@Module
internal abstract class ViewModelFactoryModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}