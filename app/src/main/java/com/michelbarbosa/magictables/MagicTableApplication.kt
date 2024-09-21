package com.michelbarbosa.magictables

import android.app.Application
import com.michelbarbosa.magictables.data.cache.AppCache
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MagicTableApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCache.start(this)
    }

    override fun onTerminate() {
        AppCache.wipeOut()
        super.onTerminate()
    }
}