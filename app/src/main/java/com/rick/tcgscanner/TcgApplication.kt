package com.rick.tcgscanner

import android.app.Application
import com.rick.tcgscanner.data.di.AppContainer

class TcgApplication : Application() {

    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}
