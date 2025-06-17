package com.example.aiartexample

import android.content.Context
import androidx.startup.Initializer
import org.koin.core.context.GlobalContext.startKoin

class KoinInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        startKoin { }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
