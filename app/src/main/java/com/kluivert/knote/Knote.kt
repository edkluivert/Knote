package com.kluivert.knote

import android.app.Application
import com.kluivert.knote.di.appModule
import com.kluivert.knote.di.viewModelModule
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class Knote : Application() {

    @InternalCoroutinesApi
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Knote)
            modules(listOf(appModule, viewModelModule))
        }


    }

}