package com.example.photo_catalog.Timer

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import timber.log.Timber

class ActiveTimer(lifecycle: Lifecycle): PhotoCatalogTimer(lifecycle) {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startTimer(){
        runnable = Runnable {
            secondsCount++
            Timber.i("Timer(Main) is it: $secondsCount")
            handler.postDelayed(runnable,1000)
        }
        handler.postDelayed(runnable, 1000)
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopTimer(){
        handler.removeCallbacks(runnable)
    }
}