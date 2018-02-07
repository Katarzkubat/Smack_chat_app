package com.example.katarzkubat.smack.Controller

import android.app.Application
import com.example.katarzkubat.smack.Utilities.SharedPreferences

/**
 * Created by katarz.kubat on 06.02.2018.
 */
class App : Application() {

    companion object {
        lateinit var sharPrefs : SharedPreferences

    }

    override fun onCreate() {

        sharPrefs = SharedPreferences(applicationContext)

        super.onCreate()
    }
}