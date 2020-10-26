package com.example.photo_catalog.Timer

import android.os.Handler
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import timber.log.Timber

open class PhotoCatalogTimer(lifecycle: Lifecycle) : LifecycleObserver {
    var secondsCount = 0

    var handler = Handler()
    lateinit var runnable: Runnable

    init {
        lifecycle.addObserver(this)
    }
}