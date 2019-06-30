package com.ps.androidx.di.component

import android.app.Application
import com.ps.androidx.base.BaseApplication
import com.ps.androidx.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

/**
 * Created by Prokash Sarkar on 5/22/2019.
 * https://github.com/prokash-sarkar
 */

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ContextModule::class,
        ActivityBuildersModule::class,
        ViewModelFactoryModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        StorageModule::class
    ]
)
interface AppComponent : AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(instance: BaseApplication)

}