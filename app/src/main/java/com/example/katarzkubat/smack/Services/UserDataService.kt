package com.example.katarzkubat.smack.Services

import android.graphics.Color
import com.example.katarzkubat.smack.Controller.App
import java.util.*

/**
 * Created by katarz.kubat on 01.02.2018.
 */
object UserDataService {

    var id = ""
    var avatarColor = ""
    var avatarName = ""
    var email = ""
    var name = ""

    fun logOut() {

        var id = ""
        var avatarColor = ""
        var avatarName = ""
        var email = ""
        var name = ""
        App.sharPrefs.authToken = ""
        App.sharPrefs.userEmail = ""
        App.sharPrefs.isLoggedIn = false

        MessageService.clearMessages()
        MessageService.clearChannels()
    }

    fun returnAvatarColor(component: String) : Int {

        val  strippedColor = component
                .replace("[", "")
                .replace(",", "")
                .replace("]", "")

        var r = 0
        var g = 0
        var b = 0

        val scanner = Scanner(strippedColor)

        if(scanner.hasNext()) {
            r = (scanner.nextDouble() * 225).toInt()
            g = (scanner.nextDouble() * 225).toInt()
            b = (scanner.nextDouble() * 225).toInt()
        }
        return Color.rgb(r, g, b)
    }
}