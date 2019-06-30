package com.ps.androidx.di.module

import com.ps.androidx.di.module.main.MainFragmentBuildersModule
import com.ps.androidx.di.module.main.MainScope
import com.ps.androidx.di.module.main.MainViewModelModule
import com.ps.androidx.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Prokash Sarkar on 5/22/2019.
 * https://github.com/prokash-sarkar
 */

@Module
abstract class ActivityBuildersModule {

    @MainScope
    @ContributesAndroidInjector(
        modules = [
            MainViewModelModule::class,
            MainFragmentBuildersModule::class]
    )
    internal abstract fun contributeMainActivity(): MainActivity
}