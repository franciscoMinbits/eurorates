package com.dollareuro.rates.application

import android.app.Application
import com.dollareuro.rates.module.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Ratespplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Ratespplication)
            modules(appModules)
        }
    }
}