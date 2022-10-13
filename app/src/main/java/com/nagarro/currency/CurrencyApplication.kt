package com.nagarro.currency

import android.app.Application
import com.nagarro.currency.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class CurrencyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@CurrencyApplication)
            modules(appComponent)
        }
    }
}