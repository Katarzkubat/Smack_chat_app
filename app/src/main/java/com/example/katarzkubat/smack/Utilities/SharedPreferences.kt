package com.example.katarzkubat.smack.Utilities

import android.content.Context
import android.content.SharedPreferences
import com.android.volley.toolbox.Volley

/**
 * Created by katarz.kubat on 06.02.2018.
 */
class SharedPreferences(context: Context) {

    val PREFS_FILE_NAME = "peferences"
    val preferences: SharedPreferences = context.getSharedPreferences(PREFS_FILE_NAME, 0)

    val IS_LOGGED_IN = "isLoggedIn"
    val AUTH_TOKEN = "authToken"
    val USER_EMAIL = "userEmail"

    var isLoggedIn: Boolean
        get() = preferences.getBoolean(IS_LOGGED_IN, false)
        set(value) = preferences.edit().putBoolean(IS_LOGGED_IN, value).apply()

    var authToken: String
        get() = preferences.getString(AUTH_TOKEN, "")
        set(value) = preferences.edit().putString(AUTH_TOKEN, value).apply()

    var userEmail: String
        get() = preferences.getString(USER_EMAIL, "")
        set(value) = preferences.edit().putString(USER_EMAIL, value).apply()

    var requestQueue = Volley.newRequestQueue(context)

}