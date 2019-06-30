package com.ps.androidx.di

import com.ps.androidx.base.TestBase
import com.ps.androidx.di.module.RepositoryModule
import dagger.Component
import javax.inject.Singleton


/**
 * Created by Prokash Sarkar on 6/11/2019.
 * https://github.com/prokash-sarkar
 */

@Singleton
@Component(modules = [TestNetworkModule::class, RepositoryModule::class])
interface TestAppComponent {

    fun inject(baseTest: TestBase)

}