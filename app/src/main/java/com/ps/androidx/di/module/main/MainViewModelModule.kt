package com.ps.androidx.di.module.main

import androidx.lifecycle.ViewModel
import com.ps.androidx.di.ViewModelKey
import com.ps.androidx.ui.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


/**
 * Created by Prokash Sarkar on 5/22/2019.
 * https://github.com/prokash-sarkar
 */

@Module
abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel
}