package com.example.katarzkubat.smack.Model

import java.sql.Timestamp

/**
 * Created by katarz.kubat on 06.02.2018.
 */
class Message constructor(val message: String, val userName: String, val channelId: String,
                          val userAvatar: String, val avatarColor: String,
                          val id: String, val timeStamp: String) {
}