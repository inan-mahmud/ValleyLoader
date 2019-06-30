package com.ps.androidx.di.module

import android.content.Context
import com.ps.androidx.data.storage.LocalStorage
import com.ps.androidx.data.storage.PreferenceStorage
import dagger.Module
import dagger.Provides

/**
 * Created by Prokash Sarkar on 5/23/2019.
 * https://github.com/prokash-sarkar
 */

@Module
class StorageModule {

    @Provides
    fun provideLocalStorage(context: Context): LocalStorage {
        return PreferenceStorage(context)
    }
}