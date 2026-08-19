package com.example.alkewallet

import android.app.Application
import com.example.alkewallet.model.FakeDatabase

class LauncherMockData : Application() {

    override fun onCreate() {
        super.onCreate()

        FakeDatabase.inicializar()
    }
}