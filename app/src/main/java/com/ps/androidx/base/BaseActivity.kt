package com.ps.androidx.base

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.ps.androidx.R
import com.ps.androidx.network.observer.ConnectionObserver
import dagger.android.support.DaggerAppCompatActivity


/**
 * Created by Prokash Sarkar on 5/22/2019.
 * https://github.com/prokash-sarkar
 */

abstract class BaseActivity : DaggerAppCompatActivity() {

    @IdRes
    protected abstract fun parentLayout(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val connectionLiveData = ConnectionObserver(this)
        connectionLiveData.observe(this, Observer { isConnected ->
            val message = if (isConnected) getString(R.string.hint_user_online)
            else getString(R.string.hint_user_offline)
            if (!isConnected) {
                showSnackBar(findViewById(parentLayout()), message)
            }
        })
    }

    fun showSnackBar(parentLayout: View, message: String) {
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.ok)) { }
            .setActionTextColor(ContextCompat.getColor(this, R.color.colorAccent))
            .show()
    }

}