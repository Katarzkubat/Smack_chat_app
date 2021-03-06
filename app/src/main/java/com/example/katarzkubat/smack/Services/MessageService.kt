package com.example.katarzkubat.smack.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.katarzkubat.smack.Controller.App
import com.example.katarzkubat.smack.Model.Channel
import com.example.katarzkubat.smack.Model.Message
import com.example.katarzkubat.smack.Utilities.URL_GET_CHANNELS
import com.example.katarzkubat.smack.Utilities.URL_GET_MESSAGES
import org.json.JSONException

/**
 * Created by katarz.kubat on 05.02.2018.
 */
object MessageService {

    val channels = ArrayList<Channel>()
    val messages = ArrayList<Message>()

    fun getChannels(complete: (Boolean) -> Unit) {

        val channelRequest = object : JsonArrayRequest(Method.GET, URL_GET_CHANNELS, null,
                Response.Listener { response ->
                    
                    try {

                        for (x in 0 until response.length()) {
                            val channel = response.getJSONObject(x)
                            val name = channel.getString("name")
                            val chanDescr = channel.getString("description")
                            val chanId = channel.getString("_id")

                            val newChannel = Channel(name, chanDescr, chanId)
                            this.channels.add(newChannel)
                        }
                        complete(true)

                    } catch (e: JSONException) {
                        Log.d("Error", "Exc" + e.localizedMessage)
                        complete(false)
                    }

                }, Response.ErrorListener { error ->

            Log.d("Error", "Channel not retrieved")
            complete(false)

        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.sharPrefs.authToken}")
                return headers
            }
        }

        App.sharPrefs.requestQueue.add(channelRequest)
    }

    fun getMessages(channelId: String, complete: (Boolean) -> Unit) {

        val url = "$URL_GET_MESSAGES$channelId"
        val messageRequest = object : JsonArrayRequest(Method.GET, url, null, Response.Listener { response ->
            clearMessages()
            try {
                for (x in 0 until response.length()) {
                    val message = response.getJSONObject(x)
                    val messageBody = message.getString("messageBody")
                    val channelId = message.getString("channelId")
                    val id = message.getString("_id")
                    val userName = message.getString("userName")
                    val userAvatar = message.getString("userAvatar")
                    val userAvatarColor = message.getString("userAvatarColor")
                    val timeStamp = message.getString("timeStamp")

                    val newMessage = Message(messageBody, channelId, id, userName, userAvatar, userAvatarColor, timeStamp)
                    this.messages.add(newMessage)
                }
                complete(true)

            } catch (e: JSONException) {
                Log.d("Error", "Exc" + e.localizedMessage)
                complete(false)
            }
        }, Response.ErrorListener { error ->

            Log.d("Error", "Channel not retrieved")
            complete(false)

        }){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.sharPrefs.authToken}")
                return headers
            }
        }

        App.sharPrefs.requestQueue.add(messageRequest)
    }

    fun clearMessages() {
        messages.clear()
    }

    fun clearChannels() {
        channels.clear()
    }

}