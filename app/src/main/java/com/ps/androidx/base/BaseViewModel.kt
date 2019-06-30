package com.ps.androidx.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Prokash Sarkar on 5/23/2019.
 * https://github.com/prokash-sarkar
 */

open class BaseViewModel : ViewModel() {

    protected var disposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        this.disposable.dispose()
        super.onCleared()
    }
}