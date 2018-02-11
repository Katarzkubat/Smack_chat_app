package com.example.katarzkubat.smack.Controller

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.Toast
import com.example.katarzkubat.smack.R
import com.example.katarzkubat.smack.Services.AuthService
import com.example.katarzkubat.smack.Services.UserDataService
import com.example.katarzkubat.smack.Utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profile default"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        createSpinner.visibility = View.INVISIBLE
    }

    fun generateUserAvatar(view: View) {

        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)

        if(color == 0) {
            userAvatar = "light$avatar"
        } else {
            userAvatar = "dark$avatar"
        }

        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
        createAvatarImageView.setImageResource(resourceId)
    }

    fun generateColorClicked(view: View) {

        val random = Random()
        val r = random.nextInt(250)
        val g = random.nextInt(250)
        val b = random.nextInt(250)

        createAvatarImageView.setBackgroundColor(Color.rgb(r, g, b))

        val savedR = r.toDouble() / 255
        val savedG = g.toDouble() / 255
        val savedB = b.toDouble() / 255

        avatarColor = "[$savedR, $savedG, $savedB, 1]"
    }

    fun createUserClicked(view: View) {

        enableSpinner(true)

        val userName = createUserNameTxt.text.toString()
        val email = createEmailTxt.text.toString()
        val password = createPasswordTxt.text.toString()

        if(userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {

            AuthService.registerUser(email, password) { registerSuccess ->
                if (registerSuccess) {
                    AuthService.loginUser(email, password) { loginSuccess ->
                        if(loginSuccess) {
                            AuthService.createUser(userName, email, userAvatar, avatarColor) {createSuccess ->
                                if(createSuccess) {

                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)

                                    enableSpinner(false)
                                    finish()
                                } else {
                                    errorToast()
                                }
                            }
                        } else {
                            errorToast()
                        }
                    }
                } else {
                    errorToast()
                }
            }
        } else {
            Toast.makeText(this, "Make sure every inserted piece of data is correct.", Toast.LENGTH_LONG).show()
            enableSpinner(false)
        }

    }

    fun errorToast() {
        Toast.makeText(this, "Something went wrong, please try again.", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun enableSpinner(enabled: Boolean) {

        if (enabled) {
            createSpinner.visibility = View.VISIBLE
    } else {
            createSpinner.visibility = View.INVISIBLE
        }
        createUserBtn.isEnabled  = !enabled
        createAvatarImageView.isEnabled = !enabled
        backgroundColorBtn.isEnabled = !enabled
}
}
