package com.ps.androidx.di

import android.app.Application
import com.ps.androidx.base.BaseApplication
import com.ps.androidx.di.component.AppComponent
import com.ps.androidx.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by Prokash Sarkar on 6/11/2019.
 * https://github.com/prokash-sarkar
 */

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ContextModule::class,
        ActivityBuildersModule::class,
        ViewModelFactoryModule::class,
        TestAndroidNetworkModule::class,
        RepositoryModule::class,
        StorageModule::class
    ]
)
interface TestAndroidAppComponent : AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): TestAndroidAppComponent
    }

    override fun inject(instance: BaseApplication)

}