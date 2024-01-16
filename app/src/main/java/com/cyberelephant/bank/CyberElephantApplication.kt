package com.cyberelephant.bank

import android.app.Application
import com.cyberelephant.bank.core.di.cyberElephantModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class CyberElephantApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger()
                Timber.plant(Timber.DebugTree())
            }
            androidContext(this@CyberElephantApplication)
            modules(cyberElephantModule)
        }
    }
}
