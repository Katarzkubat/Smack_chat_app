package com.example.katarzkubat.smack.Services

import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pManager
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.katarzkubat.smack.Controller.App
import com.example.katarzkubat.smack.Utilities.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Method

/**
 * Created by katarz.kubat on 29.01.2018.
 */
object AuthService {

    fun registerUser(email: String, password: String, complete: (Boolean) -> Unit) {

        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val registerRequest = object : StringRequest(Request.Method.POST, URL_REGISTER, Response.Listener { response ->
            println(response)
            complete(true)
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Couldn't register user: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json;  charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        App.sharPrefs.requestQueue.add(registerRequest)
    }

    fun loginUser(email: String, password: String, complete: (Boolean) -> Unit) {

        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val loginRequest = object : JsonObjectRequest(Method.POST, URL_LOGIN, null, Response.Listener { response ->

            try {

                App.sharPrefs.userEmail = response.getString("user")
                App.sharPrefs.authToken = response.getString("token")
                App.sharPrefs.isLoggedIn = true
                complete(true)

            } catch (e: JSONException) {
                Log.d("JSON", "EXC" + e.localizedMessage)
                complete(false)
            }


        }, Response.ErrorListener { error ->

            Log.d("ERROR", "Couldn't login user: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json;  charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        App.sharPrefs.requestQueue.add(loginRequest)
    }

    fun createUser(name: String, email: String, avatarName: String, avatarColor: String,
                   complete: (Boolean) -> Unit) {

        val jsonBody = JSONObject()
        jsonBody.put("name", name)
        jsonBody.put("email", email)
        jsonBody.put("avatarName", avatarName)
        jsonBody.put("avatarColor", avatarColor)
        val requestBody = jsonBody.toString()

        val createRequest = object : JsonObjectRequest(Method.POST, URL_CREATE_USER, null, Response.Listener { response ->

            try {

                UserDataService.name = response.getString("name")
                UserDataService.email = response.getString("email")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.id = response.getString("_id")
                complete(true)

            } catch (e: JSONException) {
                Log.d("JSON", "EXC" + e.localizedMessage)
                complete(false)
            }

        }, Response.ErrorListener { error ->

            Log.d("ERROR", "Couldn't add user: $avatarName")
            complete(false)

        }) {
            override fun getBodyContentType(): String {
                return "application/json;  charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.sharPrefs.authToken}")
                return headers
            }
        }

        App.sharPrefs.requestQueue.add(createRequest)
    }

    fun findUserByEmail(context: Context, complete: (Boolean) -> Unit) {
        val findUserRequest = object : JsonObjectRequest(Method.GET, "$URL_GET_USER${App.sharPrefs.userEmail}", null,
           Response.Listener {  response ->
               try {
                   UserDataService.name = response.getString("name")
                   UserDataService.email = response.getString("email")
                   UserDataService.avatarName = response.getString("avatarName")
                   UserDataService.avatarColor = response.getString("avatarColor")
                   UserDataService.id = response.getString("_id")

                   val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                   LocalBroadcastManager.getInstance(context).sendBroadcast(userDataChange)
                   complete(true)

               } catch (e: JSONException) {
                   Log.d("JSON", "EXC " + e.localizedMessage)
               }
        }, Response.ErrorListener {  error ->
            Log.d("Error", "Could not find user")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json;  charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.sharPrefs.authToken}")
                return headers
            }
        }

        App.sharPrefs.requestQueue.add(findUserRequest)
    }
}