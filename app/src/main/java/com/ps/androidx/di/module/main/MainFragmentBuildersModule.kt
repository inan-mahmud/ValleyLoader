package com.ps.androidx.di.module.main

import com.ps.androidx.ui.main.fragments.DetailsFragment
import com.ps.androidx.ui.main.fragments.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Prokash Sarkar on 5/22/2019.
 * https://github.com/prokash-sarkar
 */

@Module
abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector(modules = [])
    internal abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector(modules = [])
    internal abstract fun contributeDetailsFragment(): DetailsFragment
}