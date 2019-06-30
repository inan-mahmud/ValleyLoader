package com.ps.androidx.base

import android.content.Context
import androidx.multidex.MultiDex
import com.ps.androidx.BuildConfig
import com.ps.androidx.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
//import leakcanary.LeakSentry
import timber.log.Timber

/**
 * Created by Prokash Sarkar on 5/22/2019.
 * https://github.com/prokash-sarkar
 */

open class BaseApplication : DaggerApplication() {

    private val appComponent = DaggerAppComponent.builder()
        .application(this)
        .build()

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        appComponent.inject(this)

        //LeakSentry.config = LeakSentry.config.copy(watchFragmentViews = true)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return appComponent
    }

}